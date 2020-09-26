import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game implements GameRemote {

    private static final String REMOTE_REF_TRACKER = "tracker";
    private static String tracker_ip;
    private static int tracker_port;
    private static String playerId;
    private static GameStateVO gameState;
    private static TrackerRemote trackerRemoteObj;
    private static PlayerVO player;
    private static final Logger LOG = LoggerFactory.getLogger(Game.class);

    public static void main(String[] args) throws Exception {

        readArgs(args);
        trackerRemoteObj = getTrackerRemoteObj();

        try {
            Game game = new Game();
            GameRemote gameRemoteObj = (GameRemote) UnicastRemoteObject.exportObject(game, 0);
            GameInfoReqDTO gameInfoReq = new GameInfoReqDTO(gameRemoteObj, playerId);
            GameInfoResDTO gameInfoRes = trackerRemoteObj.getGameInfo(gameInfoReq);
            System.out.println("get gameInfoRes: " + gameInfoRes);
            if (!gameInfoRes.isValid()) {
                System.out.println("playerId already exists or no vacancy..");
                LOG.error("playerId already exists or no vacancy..");
                System.exit(0);
            }
            if (gameInfoRes.getPlayerList().size() == 1) {  // 1st player -> pServer: init game
                System.out.println("1st player->pServer: init the game");
                initGame(gameInfoRes.getN(), gameInfoRes.getK(), gameInfoRes.getPlayerList());
                LOG.debug("1st player->pServer: init the game");
            } else {    // joinGame
                // call joinGame one by one - LX
                player = new PlayerVO(gameRemoteObj, playerId, 0);
                joinGame(gameInfoRes.getPlayerList(), player);
                LOG.debug("player {}: join the game", player.getPlayerId());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        doScheduledPing();

        // player move: done -- LW
        LocalScanner scanner = new LocalScanner();
        Maze maze = new Maze(playerId);
        maze.refreshBoard(gameState);
        String[] inst = {"0", "1", "2", "3", "4"};
        while (true) {
            String token = scanner.nextToken();
            if (token.equalsIgnoreCase("9")) {
                MoveReqDTO moveReqDTO = new MoveReqDTO(playerId, Integer.parseInt(token));
                sendMoveRequest(gameState.getPlayerList(), moveReqDTO);
                trackerRemoteObj.removePlayer(player);
                maze.dispose();
                LOG.debug("player {} exit successfully", player);
                break;
            } else if (Arrays.asList(inst).contains(token)) {
                MoveReqDTO moveReqDTO = new MoveReqDTO(playerId, Integer.parseInt(token));
                sendMoveRequest(gameState.getPlayerList(), moveReqDTO);
                LOG.debug("player {} move {} successfully", player, moveReqDTO.getKeyboardInput());
            } else {
                continue;
            }
            maze.refreshBoard(gameState);
        }
    }

    private static void sendMoveRequest(List<PlayerVO> playerList, MoveReqDTO moveReqDTO) {
        for (int i = 0; i < playerList.size(); i++) {
            try {
                gameState = playerList.get(i).getGameRemoteObj().move(moveReqDTO);
                LOG.debug("player {}: move {}", playerList.get(i).getPlayerId(), moveReqDTO.getKeyboardInput());
                break;
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
                continue;
            }
        }
    }

    /**
     * set gameState
     */
    private static void joinGame(List<PlayerVO> playerList, PlayerVO player) {
        while (true) {
            try {
                gameState = playerList.get(0).getGameRemoteObj().joinGame(player);
                System.out.println("join game - gameState: " + gameState);
                LOG.debug("game state set after player {} join game", player);
                break;
            } catch (Exception ex) {
                playerList.remove(0);
                LOG.error(ex.getMessage(), ex);
            }
        }
    }


    private static void doScheduledPing() {
        Runnable pingTask = new Runnable() {
            @Override
            public void run() {
                try {
                    schedulePing();
                } catch (RemoteException e) {
                    LOG.debug(e.getMessage(), e);
                }
            }
        };
        ScheduledExecutorService scheduledService = Executors.newSingleThreadScheduledExecutor();
        scheduledService.scheduleAtFixedRate(pingTask, 500, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * primary ping 其它所有player，backup ping primary，具体处理未知
     * -- LX - done
     */
    private static void schedulePing() throws RemoteException {
        if (isPrimaryServer()) {
//            System.out.println(new Date() + ": scheduled ping....");
            for (int i = 1; i < gameState.getPlayerList().size(); i++) {
                PlayerVO player = gameState.getPlayerList().get(i);
                try {
                    player.getGameRemoteObj().ping();
                } catch (Exception ex) {
                    // for both bServer and normal player
                    gameState.removePlayer(player);
                    if (gameState.getPlayerList().size() > 1) {
                        gameState.getPlayerList().get(1).getGameRemoteObj().updateGameState(gameState);
                    }
                    trackerRemoteObj.removePlayer(player);
                }
            }
        } else if (isBackupServer()) {
            PlayerVO server = gameState.getPlayerList().get(0);
            try {
                server.getGameRemoteObj().ping();
            } catch (Exception ex) {
                gameState.removePlayer(server);
                if (gameState.getPlayerList().size() > 1) {
                    gameState.getPlayerList().get(1).getGameRemoteObj().updateGameState(gameState);
                }
                trackerRemoteObj.removePlayer(server);
            }
        }
    }

    private static TrackerRemote getTrackerRemoteObj() {
        TrackerRemote trackerRemoteObj = null;
        try {
            Registry registry = LocateRegistry.getRegistry(tracker_ip, 0);
            trackerRemoteObj = (TrackerRemote) registry.lookup(REMOTE_REF_TRACKER);
            System.out.println("get trackerRemoteObj...");
        } catch (RemoteException | NotBoundException ex) {
            System.err.println("readArgs error: " + ex.getMessage());
            System.exit(0);
        }
        return trackerRemoteObj;
    }

    /**
     * java Game [IP-address] [port-number] [player-id]
     * - IP-address is the well-known IP address of the Tracker
     * - port-number is the port over which the Tracker is listening
     * - player-id is the two-character name of the player
     */
    private static void readArgs(String[] args) {
        if (args != null && args.length == 3) {
            try {
                tracker_ip = args[0];
                tracker_port = Integer.parseInt(args[1]);
                playerId = args[2];
                if (playerId.length() != 2) {
                    exitGame(args);
                }
            } catch (Exception ex) {
                System.err.println("readArgs error: " + ex.getMessage());
                exitGame(args);
            }
        } else {
            exitGame(args);
        }
    }

    private static void exitGame(String[] args) {
        System.err.println("invalid args: " + args);
        System.exit(0);
    }

    public static void initGame(int N, int K, List<PlayerVO> playerList) {
        gameState = new GameStateVO(N, K, playerList);
    }

    /**
     * LW
     */
    @Override
    public GameStateVO joinGame(PlayerVO playerVO) throws RemoteException {
        gameState.addPlayer(playerVO);
        return gameState;
    }

    /**
     * JH
     *
     * @param moveRequest
     * @return server's game state
     * <p>
     * The player moves and refreshes its local state.
     * If it is primary server, inform backup server latest game state.
     * return latest game state.
     */
    @Override
    public GameStateVO move(MoveReqDTO moveRequest) throws RemoteException {
        Integer move = moveRequest.getKeyboardInput();
        String playerId = moveRequest.getPlayerId();

        // Update server's game state
        synchronized (gameState) { // sync game state

            // the player exits the game on its own initiative
            if (move == 9) {
                LOG.debug("player {} quit the game", playerId);
//                System.out.println("Player " + playerId + " quit the game.");
                for (PlayerVO player : gameState.getPlayerList()) {
                    if (player.getPlayerId().equalsIgnoreCase(playerId)) {
                        gameState.movePlayer(player, move);
                        gameState.removePlayer(player);
                    }
                }
                LOG.debug("player {} removed from the game state", playerId);

                //todo: inform bServer & tracker
            }

            // the player moves S/N/E/W or remain its position, then refresh its local state
            else if (move == 0 || move == 1 || move == 2 || move == 3 || move == 4) {
                for (PlayerVO player : gameState.getPlayerList()) {
                    if (player.getPlayerId().equalsIgnoreCase(playerId)) {
                        boolean score = gameState.movePlayer(player, move);
                        LOG.debug("player {} move: {}", playerId, move);
                        if (score == true) { // eat the treasure and get 1 score
                            player.setScore(player.getScore() + 1);
                            LOG.debug("player {} get 1 score", playerId);
                        }
                    }
                }
            }

            // Unknown move
            else {
                LOG.error("player {} unknown move: {}", playerId, move);
//                System.err.println("Player " + playerId + " unknown move " + move);
                return gameState;
            }
        }

//        for (PlayerVO player : gameState.getPlayerList()) {
//            if (player.getPlayerId().equalsIgnoreCase(playerId)) {
//                player.getGameRemoteObj().updateGameState(gameState);
//            }
//        }

        // If player is the primary server, it should inform the backup server update to the latest game state
        if (gameState.getPlayerList().size() > 1) {
            try {
                gameState.getPlayerList().get(1).getGameRemoteObj().updateGameState(gameState);
            } catch (RemoteException e) {
                // pServer fails to call bServer.getGameRemoteObj
                // since bServer crashed
                LOG.error(e.getMessage(), e);
                LOG.debug("detected: bServer crashed");
                // remove bServer and update to next player (new bServer)
                PlayerVO bServer = gameState.getPlayerList().get(1);
                gameState.removePlayer(bServer);
                LOG.debug("remove player {} (previous bServer)", bServer.getPlayerId());
                gameState.getPlayerList().get(1).getGameRemoteObj().updateGameState(gameState);
            }
            LOG.debug("pServer inform bServer latest game state");
        }

        // return latest game state to Game
        return gameState;
    }

    /**
     * LX
     */
    @Override
    public void ping() throws RemoteException {
//        System.out.println("successful ping -> player:" + playerId);
    }

    @Override
    public void updateGameState(GameStateVO gameState) throws RemoteException {
        this.gameState = gameState;
    }

    /**
     * compare playerList with playerId, if this is pServer
     */
    private static boolean isPrimaryServer() {
        synchronized (gameState) {
            if (gameState.getPlayerList().size() > 0
                    && gameState.getPlayerList().get(0).getPlayerId().equalsIgnoreCase(playerId)) {
                return true;
            }
            return false;
        }
    }

    /**
     * compare playerList with playerId, if this is bServer
     */
    private static boolean isBackupServer() {
        synchronized (gameState) {
            if (gameState.getPlayerList().size() > 1
                    && gameState.getPlayerList().get(1).getPlayerId().equalsIgnoreCase(playerId)) {
                return true;
            }
            return false;
        }
    }

}

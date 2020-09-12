import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game implements GameRemote {

    private static final String REMOTE_REF_TRACKER = "tracker";
    private static String host;
    private static int port;
    private static String playerId;
    private static GameStateVO gameState;
    private static TrackerRemote trackerRemoteObj;

    public static void main(String[] args) {

        readArgs(args);
        trackerRemoteObj = getTrackerRemoteObj();

        try {
            Game game = new Game();
            GameRemote gameRemoteObj = (GameRemote) UnicastRemoteObject.exportObject(game, 0);
            GameInfoReqDTO gameInfoReq = new GameInfoReqDTO(host, port, gameRemoteObj, playerId);
            GameInfoResDTO gameInfoRes = trackerRemoteObj.getGameInfo(gameInfoReq);

            if (gameInfoRes.getPlayerList().size() == 1) {  // 1st player -> pServer: init game
                initGame(gameInfoRes.getN(), gameInfoRes.getK(), gameInfoRes.getPlayerList());
            } else {    // joinGame
                // todo call joinGame one by one - LX
                PlayerVO player = new PlayerVO(host, port, gameRemoteObj, playerId, 0);
                joinGame(gameInfoRes.getPlayerList(), player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // todo player move -- LW
        // special case: bServer fail to call pServer.move

        doScheduledPing();

    }

    /**
     * set gameState
     */
    private static void joinGame(List<PlayerVO> playerList, PlayerVO player) {
        while (true) {
            try {
                gameState = playerList.get(0).getGameRemoteObj().joinGame(player);
                break;
            } catch (Exception ex) {
                playerList.remove(0);
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
                    e.printStackTrace();
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
            for (int i = 1; i < gameState.getPlayerList().size(); i++) {
                PlayerVO player = gameState.getPlayerList().get(i);
                try {
                    player.getGameRemoteObj().ping();
                } catch (Exception ex) {
                    // for both bServer and normal player
                    gameState.removePlayer(player);
                    gameState.getPlayerList().get(1).getGameRemoteObj().updateGameState(gameState);
                    trackerRemoteObj.removePlayer(player);
                }
            }
        } else if (isBackupServer()) {
            PlayerVO server = gameState.getPlayerList().get(0);
            try {
                server.getGameRemoteObj().ping();
            } catch (Exception ex) {
                gameState.removePlayer(server);
                gameState.getPlayerList().get(1).getGameRemoteObj().updateGameState(gameState);
                trackerRemoteObj.removePlayer(server);
            }
        }
    }

    private static TrackerRemote getTrackerRemoteObj() {
        TrackerRemote trackerRemoteObj = null;
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            trackerRemoteObj = (TrackerRemote) registry.lookup(REMOTE_REF_TRACKER);
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
                host = args[0];
                port = Integer.parseInt(args[1]);
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
     * <p>
     * The player moves and refreshes its local state.
     * If it is primary server, inform backup server latest game state.
     * return latest game state to this Game.
     */
    @Override
    public GameStateVO move(MoveReqDTO moveRequest) throws RemoteException {
        Integer move = moveRequest.getKeyboardInput();
        String playerId = moveRequest.getPlayerId();

        // the player exits the game on its own initiative
        // todo synchronized
        if (move == 9) {
            System.out.println("Player " + playerId + " quit the game.");
            for (PlayerVO player : gameState.getPlayerList()) {
                if (player.getPlayerId().equalsIgnoreCase(playerId)) {
                    gameState.movePlayer(player, move);
                    gameState.removePlayer(player);
                }
            }
        }
        // the player moves S/N/E/W or remain its position, then refresh its local state
        else if (move == 0 || move == 1 || move == 2 || move == 3 || move == 4) {
            for (PlayerVO player : gameState.getPlayerList()) {
                if (player.getPlayerId().equalsIgnoreCase(playerId)) {
                    boolean score = gameState.movePlayer(player, move);
                    if (score == true) {
                        // todo: add 1 score to player
                    }
                }
            }
        }
        // Unknown move
        else {
            System.err.println("Player " + playerId + " unknown move " + move);
            return gameState;
        }

        for (PlayerVO player : gameState.getPlayerList()) {
            if (player.getPlayerId().equalsIgnoreCase(playerId)) {
                player.getGameRemoteObj().updateGameState(gameState);
            }
        }

        // If player is the primary server, it should inform the backup server update to the latest game state
        if (gameState.getPlayerList().size() > 1) {
            gameState.getPlayerList().get(1).getGameRemoteObj().updateGameState(gameState);
        }

        // return latest game state to Game
        // todo inform bServer and tracker (only for 9)

        return gameState;
    }

    /**
     * LX
     */
    @Override
    public void ping() throws RemoteException {
        System.out.println("successful ping -> player:" + playerId + "@" + host + ":" + port);
    }

    @Override
    public void updateGameState(GameStateVO gameState) throws RemoteException {
        this.gameState = gameState;
    }

    /**
     * 通过比对 playerList 和自身的 playerId，判断当前玩家是否是 pServer
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
     * 通过比对 playerList 和自身的 playerId，判断当前玩家是否是 bServer
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

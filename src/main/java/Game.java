import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Game implements GameRemote {

    private static final String REMOTE_REF_TRACKER = "tracker";
    private static String host;
    private static int port;
    private static String playerId;
    private static List<PlayerVO> playerList = new ArrayList<>();
    private static GameStateVO gameState;

    public static void main(String[] args) {
        readArgs(args);

        TrackerRemote trackerStub = getTrackerStub();


    }

    private static TrackerRemote getTrackerStub() {
        TrackerRemote trackerStub = null;
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            trackerStub = (TrackerRemote) registry.lookup(REMOTE_REF_TRACKER);
        } catch (RemoteException | NotBoundException ex) {
            System.err.println("readArgs error: " + ex.getMessage());
            System.exit(0);
        }
        return trackerStub;
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

    public void initGame(int N, int K, List<PlayerVO> playerList){
        gameState=new GameStateVO(N,K,playerList);
    }

    /**
     * LW
     */
    @Override
    public GameStateVO joinGame(PlayerVO playerVO) throws RemoteException {
        playerList.add(playerVO);
        gameState.addPlayer(playerVO);
        return gameState;
        // todo: update playerList
    }

    /**
     * JH
     */
    @Override
    public GameStateVO move(MoveReqDTO moveRequest) throws RemoteException {
        return null;
        // todo: update playerList
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
        // todo: update playerList
    }

    /**
     * 通过比对 playerList 和自身的 playerId，判断当前玩家是否是 pServer
     */
    private static boolean isPrimaryServer() {
        if (playerList.size() > 0 && playerList.get(0).getPlayerId().equalsIgnoreCase(playerId)) {
            return true;
        }
        return false;
    }

    /**
     * 通过比对 playerList 和自身的 playerId，判断当前玩家是否是 bServer
     */
    private static boolean isBackupServer() {
        if (playerList.size() > 1 && playerList.get(1).getPlayerId().equalsIgnoreCase(playerId)) {
            return true;
        }
        return false;
    }

    /**
     * primary ping 其它所有player，backup ping primary，具体处理未知
     */
    private static boolean ScheduleCheck() {
        if(isPrimaryServer()){
            for(int i=1; i<playerList.size(); i++){
                ping();
            }
        }
        else if (isBackupServer()){
            ping();
        }
    }
}

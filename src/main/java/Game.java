import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Game implements GameRemote {

    private static final String REMOTE_REF_TRACKER = "tracker";
    private static String host = "127.0.0.1";
    private static int port = 1099;
    private static String playerId;

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

    /**
     * LW
     */
    @Override
    public GameStateVO joinGame(PlayerVO playerVO) throws RemoteException {
        return null;
    }

    /**
     * JH
     */
    @Override
    public GameStateVO move(MoveReqDTO moveRequest) throws RemoteException {
        return null;
    }

    /**
     * LX
     */
    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void updateGameState(GameStateVO gameState) throws RemoteException {

    }
}

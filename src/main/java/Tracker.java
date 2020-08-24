import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Tracker implements TrackerRemote {

    /**
     * The rmi-registry should be used only for registering and locating the tracker.
     */
    private static final String REMOTE_REF = "tracker";
    private static int port;
    private static int N;
    private static int K;
    private static GameRemote serverRemoteObj;

    public static void main(String[] args) {
        readArgs(args);

        try {
            Tracker tracker = new Tracker();
            TrackerRemote stub = (TrackerRemote) UnicastRemoteObject.exportObject(tracker, port);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(REMOTE_REF, stub);

            System.out.println("tracker ready... remote ref: " + REMOTE_REF + " port: " + port + ", N: " + N + ", K: " + K + ".");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public GameInfoResDTO getGameInfo(GameInfoReqDTO request) throws RemoteException {
        System.out.println("Tracker getGameInfo - request: " + request);
        GameInfoResDTO response = new GameInfoResDTO(N, K, serverRemoteObj);
        System.out.println("Tracker getGameInfo - END - response: " + response);
        return response;
    }

/*
    private synchronized AddPlayerResult addPlayer(GameInfoReqDTO request) {
        // playerId exists
        for (PlayerVO playerVO : existingPlayers) {
            if (playerVO.getPlayerId().equalsIgnoreCase(request.getPlayerId())){
                return new AddPlayerResult(false, "playerId already exists..");
            }
        }
        PlayerVO newPlayer = new PlayerVO(request.getPlayerId());
        switch (existingPlayers.size()) {
            case 0:
                newPlayer.setPrimaryServer(true);
                newPlayer.setBackupServer(true);
                break;
            case 1:
                newPlayer.setBackupServer(true);
                existingPlayers.get(0).setBackupServer(false);
                break;
            default:
                break;
        }
        existingPlayers.add(newPlayer);
        return new AddPlayerResult(true, null);
    }*/
/*

    static class AddPlayerResult {
        private boolean isSuccess;
        private String failReason;

        public AddPlayerResult(boolean isSuccess, String failReason) {
            this.isSuccess = isSuccess;
            this.failReason = failReason;
        }
    }
*/

    @Override
    public GenerateServerResDTO generateServer(GenerateServerReqDTO request) throws RemoteException {
        return null;
    }

    /**
     * java Tracker [port-number] [N] [K]
     * - port-number is the port over which the Tracker is listening
     * - The (implicit) IP address will be the local machine’s IP
     */
    private static void readArgs(String[] args) {
        if (args != null && args.length == 3) {
            try {
                port = Integer.parseInt(args[0]);
                N = Integer.parseInt(args[1]);
                K = Integer.parseInt(args[2]);
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
}

package nus.cs5223;

import nus.cs5223.dto.GenerateServerReqDTO;
import nus.cs5223.dto.GenerateServerResDTO;
import nus.cs5223.dto.JoinGameReqDTO;
import nus.cs5223.dto.JoinGameResDTO;
import nus.cs5223.remote.TrackerRemote;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Tracker implements TrackerRemote {

    /**
     * The rmi-registry should be used only for registering and locating the tracker.
     */

    private static final String REMOTE_REF = "tracker";
    private static int port = 0;
    private static int N = 10;
    private static int K = 15;


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
    public JoinGameResDTO joinGame(JoinGameReqDTO request) throws RemoteException {
        return null;
    }

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
        }
    }

    private static void exitGame(String[] args) {
        System.err.println("invalid args: " + args);
        System.exit(0);
    }
}

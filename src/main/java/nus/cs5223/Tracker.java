package nus.cs5223;

import nus.cs5223.dto.GenerateServerReqDTO;
import nus.cs5223.dto.GenerateServerResDTO;
import nus.cs5223.dto.JoinGameReqDTO;
import nus.cs5223.dto.JoinGameResDTO;
import nus.cs5223.remote.TrackerRemote;

import java.rmi.RemoteException;

public class Tracker implements TrackerRemote {

    /**
     * The rmi-registry should be used only for registering and locating the tracker.
     */

    public static void main(String[] args) {
        /**
         * java Tracker [port-number] [N] [K]
         *  - port-number is the port over which the Tracker is listening
         *  - The (implicit) IP address will be the local machine’s IP
         */


    }

    @Override
    public JoinGameResDTO joinGame(JoinGameReqDTO request) throws RemoteException {
        return null;
    }

    @Override
    public GenerateServerResDTO generateServer(GenerateServerReqDTO request) throws RemoteException {
        return null;
    }
}

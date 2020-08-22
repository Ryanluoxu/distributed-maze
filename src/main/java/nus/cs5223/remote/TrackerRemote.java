package nus.cs5223.remote;

import nus.cs5223.dto.GenerateServerReqDTO;
import nus.cs5223.dto.GenerateServerResDTO;
import nus.cs5223.dto.JoinGameReqDTO;
import nus.cs5223.dto.JoinGameResDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TrackerRemote extends Remote {
    JoinGameResDTO joinGame(JoinGameReqDTO request) throws RemoteException;

    GenerateServerResDTO generateServer(GenerateServerReqDTO request) throws RemoteException;

}

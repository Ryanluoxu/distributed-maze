import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TrackerRemote extends Remote {
    JoinGameResDTO joinGame(JoinGameReqDTO request) throws RemoteException;

    GenerateServerResDTO generateServer(GenerateServerReqDTO request) throws RemoteException;

}

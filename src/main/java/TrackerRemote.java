import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TrackerRemote extends Remote {
    GameInfoResDTO getGameInfo(GameInfoReqDTO request) throws RemoteException;

    GenerateServerResDTO generateServer(GenerateServerReqDTO request) throws RemoteException;

}

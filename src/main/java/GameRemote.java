import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameRemote extends Remote {

    GameStateVO joinGame(PlayerVO playerVO) throws RemoteException;

    GameStateVO move(MoveReqDTO moveRequest) throws RemoteException;

    void ping() throws RemoteException;

    void updateGameState(GameStateVO gameState) throws RemoteException;

}

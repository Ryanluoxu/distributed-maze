import java.util.List;

public interface GameLocal {

    GameStateVO initGame(int N, int K, List<PlayerVO> playerList);
}

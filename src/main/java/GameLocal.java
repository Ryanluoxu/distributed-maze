import java.util.List;

public interface GameLocal {

    /**
     * 首位玩家 tracker.getGameInfo 之后：initGame 得到 gameState
     */
    GameStateVO initGame(int N, int K, List<PlayerVO> playerList);
}

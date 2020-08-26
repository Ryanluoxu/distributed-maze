import java.util.List;

public class GameStateVO {
    private List<PlayerVO> playerList;
    private MazeVO maze;

    static class MazeVO {
        CellVO[][] cells;
    }

    static class CellVO {
        int x;
        int y;
        boolean hasTreasure;
        String playerId;
    }
}

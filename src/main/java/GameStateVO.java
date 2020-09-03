import java.util.List;
import java.util.Random;

public class GameStateVO {
    private List<PlayerVO> playerList;
    private MazeVO maze;
    private Integer N;
    private Integer K;
    private Random rand = new Random();

    public GameStateVO(Integer n, Integer k){
        K=k;
        N=n;
        maze = new MazeVO();
        maze.cells = new CellVO[n][n];
        
        for(int i=0;i<n;i++){
            for(int j=0; j<n;j++){
                maze.cells[i][j]=new CellVO();
            }
        }

        for(int i=0; i<K; i++){
            placeCells("*");
        }
    }

    static class MazeVO {
        CellVO[][] cells;
    }

    static class CellVO {
        int x;
        int y;
        boolean hasTreasure;
        String playerId;
        public CellVO(Integer X, Integer Y){
            hasTreasure=false;
            playerId="";
        }
    }

    public boolean isTreasure(int x, int y){
        return maze.cells[x][y].hasTreasure;
    }

    public String isPlayer(int x, int y){
        return maze.cells[x][y].playerId;
    }

    public List<PlayerVO> getPlayerList(){
        return playerList;
    }

    public void addPlayer(PlayerVO player){
        //添加player并随机安排位置
        playerList.add(player);
        placeCells(player.getPlayerId());
    }

    public void placeCells(String cell){
        //随机放置treasure或者player
        //To do 用户很多的时候优化放置效率
        while(true){
            int x = rand.nextInt(N);
            int y = rand.nextInt(N);
            if(maze.cells[x][y].hasTreasure==false 
                & maze.cells[x][y].playerId.equalsIgnoreCase("")){
                maze.cells[x][y].playerId=cell;
                if(cell.equalsIgnoreCase("*")){
                    maze.cells[x][y].hasTreasure=true;
                }
                break;
            }
        }
    }
}
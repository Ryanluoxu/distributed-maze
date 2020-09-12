import java.util.List;
import java.util.Random;

public class GameStateVO {
    private List<PlayerVO> playerList;
    private MazeVO maze;
    private Integer N;
    private Integer K;
    private Random rand = new Random();

    public GameStateVO(Integer n, Integer k, List<PlayerVO> playerList){
        K=k;
        N=n;
        this.playerList=playerList;
        maze = new MazeVO();
        maze.cells = new CellVO[n][n];

        for(int i=0;i<n;i++){
            for(int j=0; j<n;j++){
                maze.cells[i][j]=new CellVO(i,j);
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

    public Integer getSize(){
        return N;
    }

    public List<PlayerVO> getPlayerList(){
        return playerList;
    }

    public void addPlayer(PlayerVO player){
        //添加player并随机安排位置
        playerList.add(player);
        placeCells(player.getPlayerId());
    }

    /**
     * Remove the player when the player exits the game.
     */
    public void removePlayer(PlayerVO player) {
        playerList.remove(player);

        //todo: update maze

        //todo: all other players delete the player from list?
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

    /**
     *  move player
     *    4
     *   1 3
     *    2
     * @param player
     */
    public boolean movePlayer(PlayerVO player, int move) {
        String playerId = player.getPlayerId();
        int x = 0;
        int y = 0;

        // find current position
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                if (maze.cells[i][j].playerId.equalsIgnoreCase(playerId)) {
                    x = i;
                    y = j;
                }
            }
        }

        // calculate new position
        switch (move){
            case 0:
                break;
            case 1:
                if (x>0) x = x - 1;
                break;
            case 2:
                if (y<N-1) y = y + 1;
                break;
            case 3:
                if (x<N-1) x = x + 1;
                break;
            case 4:
                if (y>0) y = y - 1;
                break;
            default:
                System.out.println("Player " + player.getPlayerId() + " unknown move " + move);
                break;
        }

        // Update player's position
        if (maze.cells[x][y].playerId!="") {
            return false;
        }
        else if (maze.cells[x][y].hasTreasure==false) {
            maze.cells[x][y].playerId = playerId;
            return true;
        }
        else if (maze.cells[x][y].hasTreasure==true) {
            maze.cells[x][y].playerId = playerId;

            // Place new treasure
            placeCells("*");
            return true;
        }

        return false;

    }




}
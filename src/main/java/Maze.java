import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
@SuppressWarnings("serial")
public class Maze extends JFrame{
    private JPanel panel;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private String[][] mazeGrid;
    private Map<String, Integer> playerScores;
    private Map<String, String> server;

    public Maze() {
        init();
        this.setTitle("maze");
        this.add(panel);
        this.setPreferredSize(new Dimension(800, 800));
        this.pack(); // 不加pack就只剩标题栏了
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 用户单击窗口的关闭按钮时程序执行的操作
    }

    public void init() {
        panel = new JPanel();
        leftPanel = new JPanel();
        centerPanel = new JPanel();
        panel.setLayout(new BorderLayout());
        refreshBoard(gameState);
    }

    //刷新全局信息
    public void refreshBoard(GameStateVO gameState){
        updateGameState(gameState);
        displayBoard();
    }

    //更新全局信息，包括maze, player和server
    public void updateGameState(GameStateVO gameState){
        MazeVO maze = gameState.getMazeVO();
        List<PlayerVO> playerList = gameState.getPlayerList();
        updatePlayerStatus(playerList);
        updateMazeStatus(maze);
        updateServerStatus(playerList)
    }

    public void updatePlayerStatus(List<PlayerVO> playerList){
        Map<String, Integer> player = new HashMap<String, Integer>();
        for(int i=0; i<playerList.length; i++){
            player.put(playerList[i].getPlayerId(),playerList.getScore());
        }
        this.playerScores=player;
    }

    public void updateMazeStatus(MazeVO maze){
        K=maze.cells.length
        N=maze[0].cells.length
        String mazeGrid[][] = new String[K][N];
        for(int i=0; i<mazeGrid.length; i++){
            for(int j=0; j<mazeGrid[i].length; j++){
                if(maze.cells[i][j].hasTreasure){
                    mazeGrid[i][j]=new String("*");
                }
                else if(mazeGrid[i][j].playerId.equals("")){
                    mazeGrid[i][j] = new String(" ");
                }
                else{
                    mazeGrid[i][j] = new String(mazeGrid[i][j].playerId);
                }
            }
        }
        this.mazeGrid=mazeGrid;
    }

    public void updateServerStatus(List<PlayerVO> playerList){
        Map<String, String> server = new HashMap<String, String>();
        server.put("Primary Server:", playerList[0].getPlayerId());
        if(playerList.length>1){
            server.put("Backup Server:", playerList[1].getPlayerId());
        }
        this.server=server;
    }

    //展示全局信息
    public void displayBoard(){
        displayScoreBoard();
        displayMazeBoard();
    }

    public void displayScoreBoard(){
        leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(255, 255, 255));
        for (Map.Entry<String, Integer> entry : this.playerScores.entrySet()) {
            StringBuilder label =new StringBuilder();
            label.append(entry.getKey());
            label.append(":");
            label.append(String.valueOf(entry.getValue()));
            leftPanel.add(new JLabel(label.toString(),JLabel.CENTER));
        }
        panel.add(leftPanel, BorderLayout.WEST);
    }

    public void displayMazeBoard(){
        centerPanel.setLayout(new GridLayout(this.mazeGrid.length, this.mazeGrid[0].length, 1, 1));
        centerPanel.setBackground(new Color(255, 255, 255));
        for(int row=0; row<this.mazeGrid.length; row++){
            for(int col=0; col<this.mazeGrid[row].length; col++){
                JLabel gridCell= new JLabel(this.mazeGrid[row][col],JLabel.CENTER);
                gridCell.setOpaque(true);
                gridCell.setBackground(Color.GREEN);
                centerPanel.add(gridCell);
            }
        }
        panel.add(centerPanel, BorderLayout.CENTER);
    }

    public static void main(String args[]) {
        new Maze();
    }
}
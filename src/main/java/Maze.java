import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.management.monitor.StringMonitor;
import javax.swing.*;
import java.util.concurrent.TimeUnit;
@SuppressWarnings("serial")
public class Maze extends JFrame{
    private JPanel panel;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private String[][] mazeGrid;
    private Map<String, Integer> playerScores;
    private Map<String, String> server;
    private LocalScanner lScanner;

    public Maze() throws Exception{
        init();
        this.setTitle("maze");
        this.add(panel);
        this.setPreferredSize(new Dimension(800, 800));
        this.pack(); // 不加pack就只剩标题栏了
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 用户单击窗口的关闭按钮时程序执行的操作
    }

    public void init() throws Exception{
        panel = new JPanel();
        leftPanel = new JPanel();
        centerPanel = new JPanel();
        panel.setLayout(new BorderLayout());
        lScanner = new LocalScanner();
    }

    //刷新全局信息
    public void refreshBoard(GameStateVO gameState){
        updateGameState(gameState);
        displayBoard();
    }

    //更新全局信息，包括maze, player和server
    public void updateGameState(GameStateVO gameState){
        List<PlayerVO> playerList = gameState.getPlayerList();
        //updatePlayerStatus(playerList);
        updateMazeStatus(gameState);
        //updateServerStatus(playerList);
    }

    public void updatePlayerStatus(List<PlayerVO> playerList){
        Map<String, Integer> player = new HashMap<String, Integer>();
        for(int i=0; i<playerList.size(); i++){
            player.put(playerList.get(i).getPlayerId(),playerList.get(i).getScore());
        }
        this.playerScores=player;
    }

    public void updateMazeStatus(GameStateVO gameState){
        int N=gameState.getSize();
        String mazeGrid[][] = new String[N][N];
        for(int i=0; i<mazeGrid.length; i++){
            for(int j=0; j<mazeGrid[i].length; j++){
                String playerId=gameState.isPlayer(i, j);
                if(gameState.isTreasure(i,j)){
                    mazeGrid[i][j]=new String("*");
                }
                else if(playerId.equals("")){
                    mazeGrid[i][j] = new String(" ");
                }
                else{
                    mazeGrid[i][j] = playerId;
                }
            }
        }
        this.mazeGrid=mazeGrid;
    }

    public void updateServerStatus(List<PlayerVO> playerList){
        Map<String, String> server = new HashMap<String, String>();
        server.put("Primary Server:", playerList.get(0).getPlayerId());
        if(playerList.size()>1){
            server.put("Backup Server:", playerList.get(1).getPlayerId());
        }
        this.server=server;
    }

    //展示全局信息
    public void displayBoard(){
        //displayScoreBoard();
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

    public void clearPanel(){
        centerPanel.removeAll();
    }

    public void paintPanel(){
        centerPanel.repaint();
        centerPanel.revalidate();
    }

    public static void main(String args[]) throws Exception{
        Maze maze = new Maze();
        GameStateVO gameState = new GameStateVO(15,10);
        maze.refreshBoard(gameState);
        for(int i=0;i<10;i++){
            TimeUnit.SECONDS.sleep(1);
            maze.clearPanel();
            gameState.placeCells("*");
            maze.refreshBoard(gameState);
            maze.paintPanel();
            maze.setVisible(true);
        }
    }
}

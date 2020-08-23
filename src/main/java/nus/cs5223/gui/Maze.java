package nus.cs5223.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
@SuppressWarnings("serial")
public class Maze extends JFrame{
    private JPanel panel;
    private JPanel leftPanel;
    private JPanel centerPanel;

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

        Map<String, Integer> player=updatePlayerStatus();
        String[][] maze=updateMazeStatus();

        displayScoreBoard(player);
        displayMazeBoard(maze);
    }

    public Map<String, Integer> updatePlayerStatus(){
        Map<String, Integer> player = new HashMap<String, Integer>();
        player.put("uu",1);
        player.put("aa",3);
        return player;
    }

    public String[][] updateMazeStatus(){
        String maze[][] = new String[20][20];
        for(int i=0; i<maze.length; i++){
            for(int j=0; j<maze[i].length; j++){
                maze[i][j]= new String(" ");
            }
        }
        maze[10][10]=new String("uu");
        maze[10][15]=new String("aa");
        maze[11][12]=new String("*");
        return maze;
    }

    public Map<String, String> updateServerStatus(){
        Map<String, String> server = new HashMap<String, String>();
        server.put("Primary","uu");
        server.put("Backup","aa");
        return server;
    }

    public void displayScoreBoard(Map<String, Integer> playerScores){
        leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(255, 255, 255));
        for (Map.Entry<String, Integer> entry : playerScores.entrySet()) {
            StringBuilder label =new StringBuilder();
            label.append(entry.getKey());
            label.append(":");
            label.append(String.valueOf(entry.getValue()));
            leftPanel.add(new JLabel(label.toString(),JLabel.CENTER));
        }
        panel.add(leftPanel, BorderLayout.WEST);
    }

    public void displayMazeBoard(String[][] maze){
        centerPanel.setLayout(new GridLayout(maze.length, maze[0].length, 1, 1));
        centerPanel.setBackground(new Color(255, 255, 255));
        for(int row=0; row<maze.length; row++){
            for(int col=0; col<maze[row].length; col++){
                JLabel gridCell= new JLabel(maze[row][col],JLabel.CENTER);
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
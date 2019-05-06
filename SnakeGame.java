import java.awt.*;
import javax.swing.*;

public final class SnakeGame{
     JFrame frame = new JFrame("SnakeGame");
     GamePanel g = new GamePanel();
     public SnakeGame(){
          /*JLabel score = new JLabel("Score: " + g.getScore());
          JLabel level = new JLabel("Level " + g.getLevel());
          score.setForeground(Color.WHITE);
          level.setForeground(Color.WHITE);
          frame.add(score);
          frame.add(level);
          */
          //the content of the frame is the g object from the GamePanel class
          frame.setContentPane(g);
          //default close operation is to close when close button is pressed
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          //user can't change size of panel
          frame.setResizable(false);
          frame.pack();

          //size is set
          frame.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
          frame.setLocationRelativeTo(null);
          //set visible = true so that the user can see it
          frame.setVisible(true);
     }

     public final JFrame getSnakeGame(){
          return frame;
     }

}
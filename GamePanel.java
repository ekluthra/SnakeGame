//importing packages
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import java.lang.Math;

public class GamePanel extends JPanel implements Runnable, KeyListener{
     //height and width of the window
     public static final int WIDTH = 800;
     public static final int HEIGHT = 800;

     //objects for rendering
     private Graphics2D g2d;
     private BufferedImage image;

     private Thread thread;
     private boolean running;
     private long targetTime;
     
     //size of the entities (food and snake) in the game
     private final int SIZE = 20;
     //the the head of the snake and the food are objects of the entity class
     private Entity head, food;
     //the snake's body is an array
     private ArrayList<Entity> snake;
     //variables for score, levl, and if the game is over or not
     private int score;
     private int level;
     private boolean gameover;

     //dx and dy are 'change in the x position' and 'change in the y position', respectively
     private int dx, dy;
     //key input
     private boolean  up,right,left,down,start;

     //class constructor
     public GamePanel(){
          setPreferredSize(new Dimension(WIDTH, HEIGHT));
          setFocusable(true);
          requestFocus();
          addKeyListener(this);
     }
     public void addNotify(){
          super.addNotify();
          thread = new Thread(this);
          thread.start();
     }
     //method for setting the frame rate
     private void setFPS(int fps){
          targetTime = 1000/fps;
     }

     //method for determining what to do if a key is pressed
     public void keyPressed(KeyEvent e){
          //getting the value of the pressed key using a method from KeyEvent called getKeyCode
          int k = e.getKeyCode();
          //if statements for each scenario
          if(k == KeyEvent.VK_UP) up = true;
          if(k == KeyEvent.VK_DOWN) down = true;
          if(k == KeyEvent.VK_LEFT) left = true;
          if(k == KeyEvent.VK_RIGHT) right = true;
          if(k == KeyEvent.VK_ENTER) start = true;

     }

     //method for determining what to do once the key is released
     public void keyReleased(KeyEvent e){
          //getting the value of the key released
          int k = e.getKeyCode();

          //if statements for what to do when each key is released
          if(k == KeyEvent.VK_UP) up = false;
          if(k == KeyEvent.VK_DOWN) down = false;
          if(k == KeyEvent.VK_LEFT) left = false;
          if(k == KeyEvent.VK_RIGHT) right = false;
          if(k == KeyEvent.VK_ENTER) start = false;
     }
     //mandatory method for when using KeyListener
     public void keyTyped(KeyEvent arg0){

     }
     
     public void run(){
          if(running) return;
          init();
          long startTime;
          long elapsed;
          long wait;
          while(running){
               startTime = System.nanoTime();
               
               update();
               requestRender();
               
               elapsed = System.nanoTime() - startTime;
               wait = targetTime - elapsed/1000000;
               if(wait > 0){
                    try{
                         Thread.sleep(wait);
                    } catch(Exception e) {
                         e.printStackTrace();
                    }
               }
          }
     }
     /*The init method is the first method called in an Applet or JApplet.
      *When an foodt is loaded by the Java plugin of a browser or by an foodt viewer,
      *it will first call the Applet. ... Any initializations that are required to use the foodt should be executed here.
      *Init method is used to start any given process.
      */
     private void init(){
          image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
          g2d = image.createGraphics();
          running = true;
          setUplevel();

     }

     //method for starting the level, and initializing any variables for the game
     private void setUplevel(){
          //snake body is initialized as a new ArrayList
          snake = new ArrayList<Entity>();
          //head is an entity with size SIZE (which is 20, in this case)
          head = new Entity(SIZE);
          //setting the initial position of the snake to the middle of the screen
          head.setPosition(WIDTH / 2, HEIGHT /2);
          //adding the head of the snake to the snake arraylist entity itself
          snake.add(head);
          //for loop for making the initial length of the snake 3 blocks long
          for(int i = 1; i < 3; i++){
               //appending the blocks onto the snake one by one
               Entity e = new Entity(SIZE);
               e.setPosition((head.getX()+(i*SIZE)), head.getY());
               snake.add(e);
          }
          //the food for the snake is initialized, its the same block size as the snake
          food = new Entity(SIZE);
          //setting the food's position
          setApple();
          //setting score to 0
          score = 0;
          //game has just started, so game can't be over
          gameover = false;
          //initial level is level 1
          level = 1;
          dx = dy = 0;
          setFPS(level*10);
     }
     //method for setting the position of the food
     public void setApple(){
          //food is given a random x and y position
          int x = (int)(Math.random()*(WIDTH-SIZE));
          int y = (int)(Math.random()*(HEIGHT-SIZE));
          //food is placed so that it's not in between the grid. it fits perfectly in each square
          y = y - (y%SIZE);
          x = x - (x%SIZE);
          //set and display the food
          food.setPosition(x,y);
     }
     private void requestRender(){
          render(g2d);
          Graphics g = getGraphics();
          g.drawImage(image, 0, 0, null);
          g.dispose();
     }
     
     private void update(){
          if(gameover){
               if(start){
                    setUplevel();
               }
               return;
          }
          if(up && dy ==0){
               dy = -SIZE;
               dx = 0;
          }
          if(down && dy ==0){
               dy = SIZE;
               dx = 0;
          }
          if(left && dx == 0){
               dy = 0;
               dx = -SIZE;
          }
          if(right && dx == 0 && dy != 0){
               dy = 0;
               dx = SIZE;
          }
          if(dx != 0 || dy != 0) {
               for (int i = snake.size() - 1; i > 0; i--) {
                    snake.get(i).setPosition(
                            snake.get(i - 1).getX(),
                            snake.get(i - 1).getY()
                    );
               }
               head.move(dx, dy);
          }
          //if the head of the snake collides with any part of the body, then end the game
          for(Entity e : snake){
               if(e.isCollision(head)){
                    gameover = true;
                    break;
               }
          }
          //if the snake's head collides with the food, increase the score and set the food to a new position
          if(food.isCollision(head)){
               score++;
               setApple();
               //add one more block to the length of the snake
               Entity e = new Entity(SIZE);
               //initially set the position of it to off screen
               e.setPosition(-100,-100);
               //then append it to the snake
               snake.add(e);
               //if the score is a multiple of 5
               if(score % 5 == 0){
                    //increase the level
                    level++;
                    //max level is 10
                    if(level > 10) level = 10;
                    //make it faster
                    setFPS(level*10);
               }
          }
          //if the head of the snake goes out of bounds
          if(head.getX() < 0) gameover=true;
          if(head.getY() < 0) gameover=true;
          if(head.getX() > WIDTH) gameover=true;
          if(head.getY() > HEIGHT) gameover=true;
     }
     public void render(Graphics2D g2d){
          g2d.clearRect(0,0,WIDTH, HEIGHT);
          g2d.setColor(Color.GREEN);
          for(Entity e : snake){
               e.render(g2d);
          }

          g2d.setColor(Color.RED);
          g2d.setFont(new Font("Times New Roman", Font.BOLD, 22));
          food.render(g2d);
          if(gameover){
               g2d.drawString("Game Over", 345, 400);
          }

          g2d.setColor(Color.WHITE);
          g2d.drawString("Score: " + score,10, 20);
          g2d.drawString("Level: " + level, 120, 20);

     }

     public int getScore(){
          return score;
     }
     public int getLevel(){
          return level;
     }
}
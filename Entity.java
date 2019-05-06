import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Entity{
     //each entity in the game(such as the snake's body, head, and food) will have an xy position and a size
     private int x,y,size;

     //constructor. Initializes the size of the object declared with the size parameter
     public Entity(int size){
          this.size = size;
     }
     //getter methods for getting the x and y values of the entity
     public int getX(){return x;}
     public int getY(){return y;}
     //setter methods for setting the x and y values of the entity
     public void setX(int x){this.x=x;}
     public void setY(int y){this.y=y;}
     //setPosition method for setting both x and y at the same time
     public void setPosition(int x,int y){
          this.x=x;
          this.y=y;
     }
     //moving the entity by adding to the x and y values
     public void move(int dx, int dy){
          x+= dx;
          y+= dy;
     }
     
     public Rectangle getBound(){
          return new Rectangle(x,y,size,size);
     }

     //method that checks for collisions
     public boolean isCollision(Entity o){
          //if Entity o is the same as the object of the entity class, then return that there is no collision
          if(o == this) return false;
          //otherwise return that one rectangle object has crossed another rectangle object
          return getBound().intersects(o.getBound());
     }
     //each rectangle in the game (parts of the snake, food) is this big and at this position:
     public void render(Graphics2D g2d){
          g2d.fillRect(x+1,y+1,size-2,size-2);
     }
}
import java.util.Random;
public abstract class MovingObject extends RoomObject{

    Random rng = new Random();
    Board room;
    int battery;
    String lastAction;
    String nextAction;
    int nextTile;
    int newX;
    int newY;

    public MovingObject(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, direction);
    }

    public abstract void scan();

    public abstract void move();
    
    public abstract void decideAction();

    public abstract void executeAction();

    public void turn(int newDirection){
        // Used by baby and dumb vacuum to randomly decide which new direction to turn to
        newDirection = rng.nextInt(4) + 1;
        while(newDirection == direction){
            newDirection = rng.nextInt(4) + 1;
        }
        direction = newDirection;
    }


    public void setNewCoord(){
        //new coordinates describe the coordinates of the tile the baby/vacuum plan to go to
        newX = xCoord;
        newY = yCoord;

        switch (direction) {
            case 1 -> newY--;
            case 2 -> newX++;
            case 3 -> newY++;
            case 4 -> newX--;
        }
    }
    
    public void setRoom(Board room){
        this.room = room;
        battery = room.batteryCapacity;
    }


    
}

public class RoomObject {
    protected int xCoord;
    protected int yCoord;
    protected int direction; //1-north, 2-east, 3-south, 4-west

    
    public RoomObject(int xCoord, int yCoord, int direction) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.direction = direction;
        
    }
}

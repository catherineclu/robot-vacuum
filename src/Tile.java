public class Tile extends RoomObject{


    public Tile(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, direction);
    }

    @Override
    public String toString(){
        return "T";
    }
    
}

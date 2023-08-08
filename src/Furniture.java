public class Furniture extends RoomObject{

    public Furniture(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, direction);
    }

    @Override
    public String toString(){
        return "F";
    }

    
}

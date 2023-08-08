public class Vacuum extends MovingObject implements Battery {
    int numOfMoves;

    public Vacuum(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, direction);
        numOfMoves = 0;
    }

    public void move(){

        RoomObject temp;
        setNewCoord();

        if(newX < 0 || newY < 0 || newX == room.components[1].length || newY == room.components.length){
            System.out.println("Skipped turn b/c vacuum tried to move out of bounds:");
        }
        else{
            temp = room.components[newY][newX];
            
            if(temp instanceof Tile){
                
                room.components[newY][newX] = room.components[yCoord][xCoord];

                replacePreviousTile();

                if(temp instanceof ChargingStation){
                    charge();
                    numOfMoves = -1;
                }
                
                vacuumTile(newX, newY);
                yCoord = newY;
                xCoord = newX;
                numOfMoves ++;
            }
            else{
                System.out.println("Skipped turn b/c vacuum tried to move into " + temp);
            }
        }   
    }

    private void replacePreviousTile(){
        //checks if the battery is fully charged to see if the previous square was a charging station
        if(battery == room.batteryCapacity || numOfMoves == 0){
            room.components[yCoord][xCoord] = new ChargingStation(xCoord, yCoord, 0);
        }
        else{
            room.components[yCoord][xCoord] = new Tile(xCoord, yCoord, 0);
        }
    }

    public void vacuumTile(int newX, int newY){
        room.vacuumed[yCoord][xCoord] = true;
        room.vacuumed[newY][newX] = true;
    }

    public void drain(){
        battery--;
    }

    public void charge(){
        battery = room.batteryCapacity;
    }

    public int getCharge(){
        return battery;
    }

    public void setCharge(int charge) { 
        battery = charge; 
    }

    @Override
    public void scan() {
    }

    @Override
    public void decideAction() {        
    }

    @Override
    public void executeAction() {
    }
}

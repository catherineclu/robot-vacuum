import java.util.ArrayList;
public class Baby extends MovingObject{
    boolean vacuumInSight;
    int vacuumDistance;
    int lastObjectCovered; // 0 = empty tile, 1 = dumbvac, 2 = smartvac
    

    public Baby(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, direction);
        vacuumInSight = false;
        lastObjectCovered = 0;
    }

    ArrayList<RoomObject> scannedArea;

    public void decideAction(){
        if(vacuumInSight && vacuumDistance > 0){
            nextAction = "move";
        }
        else{
            if(lastAction == null){
                nextAction = "scan";
            }
            else if(lastAction.equals("move") || lastAction.equals("scan")){
                nextAction = "turn";
            }
            else{
                nextAction = "scan";
            }
        }
    }


    public void executeAction(){
        switch (nextAction) {
            case "move" -> {
                move();
                vacuumDistance--;
            }
            case "turn" -> turn(direction);
            case "scan" -> {
                scan();
                checkForVacuumInSight();
            }
        }
        lastAction = nextAction;
    }


    public void kill(Vacuum target){
        target.setCharge(0);
    }

    @Override
    public void scan() {

        scannedArea = new ArrayList<>();

        if(direction == 1){
            for(int i = yCoord; i >= 0; i--){
                scannedArea.add(this.room.components[i][xCoord]);
            }
        }
        else if(direction == 2){
            for(int i = xCoord; i < room.components[yCoord].length; i++){
                scannedArea.add(this.room.components[yCoord][i]);
            }
        }
        else if(direction == 3){
            for(int i = yCoord; i < room.components.length; i++){
                scannedArea.add(this.room.components[i][xCoord]);
            }
        }
        else if(direction == 4){
            for(int i = xCoord; i >= 0; i--){
                scannedArea.add(this.room.components[yCoord][i]);
            }   
        }
    }

    public void checkForVacuumInSight(){
        vacuumDistance = 0;
        for(RoomObject roomObj : scannedArea){
            if(roomObj instanceof Furniture){
                vacuumInSight = false;
                break;
            }
            else if(roomObj instanceof Tile){
                vacuumDistance ++;
            }
            else if(roomObj instanceof Vacuum){
                vacuumInSight = true;
                vacuumDistance ++;
                break;
            }
        }
    }

    @Override
    public String toString(){
        return "B";
    }

    @Override
    public void move() {
        RoomObject temp;
        setNewCoord();


        if(newX < 0 || newY < 0 || newX == room.components[1].length || newY == room.components.length){
            System.out.println("Skipped turn b/c baby tried to move out of bounds:");
        }
        else {
            temp = room.components[newY][newX];
            if( !(temp instanceof Furniture || temp instanceof ChargingStation) ){

                room.components[newY][newX] = room.components[yCoord][xCoord];
                replaceLastTile();
                lastObjectCovered = storeLastObject(temp);
                
                yCoord = newY;
                xCoord = newX;
            }
            else{
                System.out.println("Skipped turn b/c baby tried to move into furniture:");
            }
        }
    }

    private void replaceLastTile() {
        if (lastObjectCovered == 0) {
            room.components[yCoord][xCoord] = new Tile(xCoord, yCoord, 0);
        }
        else if (lastObjectCovered == 1) {
            room.components[yCoord][xCoord] = new DumbVacuum(xCoord, yCoord, 0);
            ((Vacuum)room.components[yCoord][xCoord]).setCharge(0);
        }
        else if (lastObjectCovered == 2) {
            room.components[yCoord][xCoord] = new SmartVacuum(xCoord, yCoord, 0);
            ((Vacuum)room.components[yCoord][xCoord]).setCharge(0);
        }
    }

    // checks intended spot for vacuum and kills, returning the previous contents of that spot
    private int storeLastObject(RoomObject lastObject) {
        if(lastObject instanceof DumbVacuum){
            kill((Vacuum)lastObject);
            return 1;
        }
        else if(lastObject instanceof SmartVacuum){
            kill((Vacuum)lastObject);
            return 2;
        }
        else
            return 0;
    }
}

import java.util.ArrayList;
import java.util.Random;

public class SmartVacuum extends Vacuum{
    ArrayList<RoomObject> scannedArea = new ArrayList<>();
    ArrayList<Integer> weightedArea = new ArrayList<>();
    int directionToTurnTo;


    public SmartVacuum(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, direction);
    }


    @Override
    public void decideAction(){
        if(lastAction == null){
            nextAction = "scan";
        }
        else if(lastAction.equals("scan")){
            moveOrTurn();
        }
        else if(lastAction.equals("turn")){
            nextAction = "move"; 
        }
        else{
            nextAction = "scan";
        }
    }



    @Override
    public void executeAction() {
        if(battery>0){
            if (nextAction.equals("move")) {
                move();
            } else if (nextAction.equals("turn")) {
                turn(directionToTurnTo);
            } else if (nextAction.equals("scan")) {
                scan();
                nextTile = 0;
                nextTile = chooseTile();
            } else {
                System.out.println("error");
            }
            lastAction = nextAction;
            drain();
        }
    }



    public void moveOrTurn() {
        boolean correctDirection = (direction == (double) nextTile/2);
        if(correctDirection){
            nextAction = "move";
        }
        else{
            directionToTurnTo = nextTile == 1? nextTile = 2: nextTile/2;
            nextAction = "turn";
        }
    }



    public void changeWeightedValues(int index, int tileChange, int surroundingChange){
        weightedArea.set(index, weightedArea.get(index) + tileChange);
        weightedArea.set(index+1, weightedArea.get(index+1) + surroundingChange);
        weightedArea.set(index-1, weightedArea.get(index-1) + surroundingChange);
    }



    @Override
    public void turn(int newDirection) {
        direction = newDirection;
    }



    public void outOfBoundsScanCheck(int[] xDiff, int[] yDiff){
        if(xCoord == 0){
            for(int i = 0; i < 8; i++){
                if(xDiff[i] == -1){
                    xDiff[i] = 20;
                }
            }
        }
        else if(xCoord >= room.components[1].length-1){
            for(int i = 0; i < 8; i++){
                if(xDiff[i] == 1){
                    xDiff[i] = 20;
                }
            }
        }
        if(yCoord == 0){
            for(int i = 0; i < 8; i++){
                if(yDiff[i] == -1){
                    yDiff[i] = 20;
                }
            }
        }
        else if(yCoord >= room.components.length-1){
            for(int i = 0; i < 8; i++){
                if(yDiff[i] == 1){
                    yDiff[i] = 20;
                }
            }
        }
    }



    @Override
    public void scan() {
       this.scannedArea = new ArrayList<RoomObject>();
        int[] xDiff = {-1, 0, 1, 1, 1, 0, -1, -1};
        int[] yDiff = {-1, -1, -1, 0, 1, 1, 1, 0};
        scannedArea.add(null);
        
        outOfBoundsScanCheck(xDiff, yDiff);
        
        for(int i = 0; i < 8; i++){
            if(xDiff[i] == 20 || yDiff[i] == 20){
                scannedArea.add(null);
            }
            else{
                scannedArea.add(this.room.components[yCoord+yDiff[i]][xCoord+xDiff[i]]);
            }
        }
        scannedArea.add(null);
    }


    @Override
    public String toString(){
        return "S";
    }



    public int chooseTile(){
        weighOption();
        int tileWeight = 0;
        ArrayList<Integer>  possibleMoves = new ArrayList<Integer>();
        for(int i = 1; i <=8; i++){
            tileWeight = weightedArea.get(i);
            for(int j = 0; j < tileWeight; j++){
                possibleMoves.add(i);
            }
        }
        Random rand = new Random();
        int chosenTile = rand.nextInt(possibleMoves.size());
        return possibleMoves.get(chosenTile);
    }



    public void weighOption(){
        weightedArea = new ArrayList<>();
        //sets all values to 0 and adds a one index long buffer on the beginning and end
        for(int i = 0; i <= 9; i++){
            weightedArea.add(10);
        }
        for(int i = 1; i <= 8; i++){
            if(scannedArea.get(i) == null){
                continue;
            }
            if(scannedArea.get(i) instanceof Baby){
                changeWeightedValues(i, 0, -6);
            }
            if(scannedArea.get(i) instanceof Tile){
                changeWeightedValues(i, 2, 0);
            }
            if(scannedArea.get(i) instanceof ChargingStation){
                if(battery <= room.batteryCapacity/2){
                    changeWeightedValues(i, 5, 3);
                }
                else{
                    changeWeightedValues(i, 2, 1);
                }
            } 
            //check if the square is vacuumed or not
            if(room.vacuumed[scannedArea.get(i).yCoord][scannedArea.get(i).xCoord]){
                changeWeightedValues(i, -1, 0);
            }
            else{
                changeWeightedValues(i, 3, 1);
            }
        }
        //removes all moves that are not eligible free tiles
        for(int i = 1; i <= 8; i++){
            if(scannedArea.get(i) == null || !(scannedArea.get(i) instanceof Tile)){
                weightedArea.set(i, 0);
            }
        }
        //sets the buffers to null
        weightedArea.set(0, 0);
        weightedArea.set(9, 0);
    }
}

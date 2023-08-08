public class DumbVacuum extends Vacuum{
    RoomObject scannedObj;


    public DumbVacuum(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, direction);
    }


    @Override
    public void decideAction() {
        if (lastAction == null) {
            nextAction = "scan";
        } else {
            switch (lastAction) {
                case "scan":
                    if (scannedObj instanceof Tile || scannedObj instanceof ChargingStation) {
                        nextAction = "move";
                    } else {
                        nextAction = "turn";
                    }
                    break;
                case "move":
                    nextAction = "scan";
                    break;
                case "turn":
                    nextAction = "move";
                    break;
                default:
                    nextAction = "scan";
                    break;
            }
        }
    }

    @Override
    public void executeAction() {
        if (nextAction.equals("move")) {
            move();
        } else if (nextAction.equals("scan")) {
            scan();
        } else {
            turn(direction);
        }
        drain();
        lastAction = nextAction;
    }

    @Override
    public void scan() {

        setNewCoord();

        if(newX < 0 || newY < 0 || newX == room.components[1].length || newY == room.components.length){
            scannedObj = null;
        }
        else{
            scannedObj = this.room.components[newY][newX];
        }
    }

    @Override
    public String toString(){
        return "D";
    }
    
}

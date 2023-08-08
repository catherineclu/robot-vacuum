import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        File text = new File("src/world1.txt");
        Scanner fileReader = new Scanner(text);
        ArrayList<String> worldCharacteristics = new ArrayList<>();

        while (fileReader.hasNext()) {
            String nextCharacteristic = fileReader.nextLine();
            worldCharacteristics.add(nextCharacteristic);
        }

        Board room = new Board(worldCharacteristics);
        room.createWorld();


        for(int i = 0; i < room.components.length; i++){
            for(int j = 0; j < room.components[1].length; j++){
                room.vacuumed[i][j] = false;
                if(room.components[i][j] instanceof Furniture){
                    room.vacuumed[i][j] = true;
                }
                if(room.components[i][j] instanceof MovingObject){
                    MovingObject temp = (MovingObject) room.components[i][j];
                    temp.setRoom(room);
                    room.components[i][j] = temp;
                }
            }
        }
        GUI gui = new GUI(room.getBoard(), room.vacuumed);

        //puts all the babies and vacuums into an array list
        ArrayList<Baby> babies = new ArrayList<>();
        ArrayList<Vacuum> vacuums = new ArrayList<>();
        for(int i = 0; i < room.components.length; i++){
            for(int j = 0; j < room.components[0].length; j++){
                if(room.components[i][j] instanceof Baby){
                    babies.add((Baby)room.components[i][j]);
                }
                if(room.components[i][j] instanceof Vacuum){
                    vacuums.add((Vacuum)room.components[i][j]);
                }
            }
        }

        while(true) {
            //causes delay between loops according to world file
            Thread.sleep(room.moveSpeed);

            for (Vacuum movingVac : vacuums) {
                if (movingVac.battery > 0) {
                    movingVac.decideAction();
                    movingVac.executeAction();
                }
            }
            gui.refresh(room.getBoard(), room.vacuumed);

            for (Baby movingBaby : babies) {
                movingBaby.decideAction();
                movingBaby.executeAction();
            }

            gui.refresh(room.getBoard(), room.vacuumed);

            boolean allVacuumed = true;
            boolean vacsAlive = false;
            for (Vacuum movingVac : vacuums) {
                if (movingVac.getCharge() != 0) {
                    vacsAlive = true;
                    break;
                }
            }
            //checks for end conditions
            for (int i = 0; i < room.vacuumed.length; i++) {
                for (int j = 0; j < room.vacuumed[0].length; j++) {
                    if (!room.vacuumed[i][j]) {
                        allVacuumed = false;
                        break;
                    }
                }
            }

            if (allVacuumed || !vacsAlive) {
                System.out.println("Game over!");
                System.exit(0);
            }
        }
    }
}

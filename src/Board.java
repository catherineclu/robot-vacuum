import java.util.ArrayList;

public class Board {

    ArrayList<String> characteristics;
    RoomObject[][] components;
    boolean[][] vacuumed;
    
    int numObjects;
    int batteryCapacity;
    int moveSpeed;
    int xCor = 0;
    int yCor = 0;
    int dir = 0;
    int objectIndex = 0;

    public RoomObject[][] getBoard() {
        return components;
    }

    public Board(ArrayList<String> characteristic){
        this.characteristics = characteristic;
        String[] boardSize = characteristics.get(0).split(",");
        int xMax = Integer.parseInt(boardSize[0]);
        int yMax = Integer.parseInt(boardSize[1]);
        this.components = new RoomObject[yMax][xMax];
        this.vacuumed = new boolean[yMax][xMax];
    }

    public void createWorld(){
        initializeEmptyTiles();
        objectIndex++;

        initializeSmartVacs();
        objectIndex++;

        initializeDumbVacs();
        objectIndex++;

        moveSpeed = Integer.parseInt(characteristics.get(objectIndex));
        moveSpeed = 1000/moveSpeed;
        objectIndex++;

        batteryCapacity = Integer.parseInt(characteristics.get(objectIndex));
        objectIndex++;

        initializeBabies();
        objectIndex++;

        initializeFurniture();
    }

    private void initializeEmptyTiles(){
        for(int row = 0; row < components.length; row++){
            for(int col = 0; col < components[1].length; col++){
                components[row][col] = new Tile(col, row, 0);
            }
        }
    }

    private void initializeSmartVacs(){
        numObjects = Integer.parseInt(characteristics.get(objectIndex));
        for(int i = 0; i < numObjects; i ++){
            getInfo(numObjects);
            components[yCor][xCor] = new SmartVacuum(xCor, yCor, dir);
        }
    }

    private void initializeDumbVacs(){
        numObjects = Integer.parseInt(characteristics.get(objectIndex));
        for(int i = 0; i < numObjects; i ++){
            getInfo(numObjects);
            components[yCor][xCor] = new DumbVacuum(xCor, yCor, dir);
        }
    }

    private void initializeBabies(){
        numObjects = Integer.parseInt(characteristics.get(objectIndex));
        for(int i = 0; i < numObjects; i ++){
            getInfo(numObjects);
            components[yCor][xCor] = new Baby(xCor, yCor, dir);
        }
    }

    private void initializeFurniture(){
        numObjects = Integer.parseInt(characteristics.get(objectIndex));
        for(int i = 0; i < numObjects; i ++){
            getInfo(numObjects);
            components[yCor][xCor] = new Furniture(xCor, yCor, dir);
        }
    }

    private void getInfo(int numObjects){
        objectIndex++;
        String[] vacPos = characteristics.get(objectIndex).split(",");
        xCor = Integer.parseInt(vacPos[0]);
        yCor = Integer.parseInt(vacPos[1]);
        if(vacPos.length > 2){
            dir = switch(vacPos[2]){
                case "N" -> 1;
                case "E" -> 2;
                case "S" -> 3;
                case "W" -> 4;
                default -> 0; 
            };
        }
    }
}

import javax.swing.*;
import java.awt.*;

public class GUI {
    JFrame frame;
    int width;
    int length;
    JPanel panel;

    ImageIcon smartVac;
    ImageIcon deadSmartVac;
    ImageIcon dumbVac;
    ImageIcon deadDumbVac;
    ImageIcon baby;
    ImageIcon charger;
    ImageIcon obstacle;
    ImageIcon dirtyTile;
    ImageIcon cleanTile;

    public GUI(RoomObject[][] board, boolean[][] vacuumed) {
        width = board.length;
        length = board[0].length;

        //assigns all icons and visual components
        smartVac = new ImageIcon(new ImageIcon("src/smartestVacuum.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        deadSmartVac = new ImageIcon(new ImageIcon("src/deadSmartVac.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        dumbVac = new ImageIcon(new ImageIcon("src/DumbVacuum.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        deadDumbVac = new ImageIcon(new ImageIcon("src/deadDumbVac.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        baby = new ImageIcon(new ImageIcon("src/baby.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        obstacle = new ImageIcon(new ImageIcon("src/furniture.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        charger = new ImageIcon(new ImageIcon("src/chargingStation.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        dirtyTile = new ImageIcon(new ImageIcon("src/Tile.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        cleanTile = new ImageIcon(new ImageIcon("src/cleanTile.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("RoboVac");
        
        paint(board, vacuumed);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public void refresh(RoomObject[][] newBoard, boolean[][] vacuumed) {
        frame.remove(panel);
        paint(newBoard, vacuumed);
        frame.add(panel);
        frame.pack();
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    public void paint(RoomObject[][] board, boolean[][] vacuumed) {
        panel = new JPanel(new GridLayout(width, length, width, length));

        //constantly updates board
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                if (board[i][j] instanceof SmartVacuum) {
                    Vacuum v = (Vacuum)board[i][j];
                    if (v.getCharge() == 0)
                        panel.add(new JLabel(deadSmartVac));
                    else
                        panel.add(new JLabel(smartVac));
                }
                else if (board[i][j] instanceof DumbVacuum) {
                    Vacuum v = (Vacuum)board[i][j];
                    if (v.getCharge() == 0)
                        panel.add(new JLabel(deadDumbVac));
                    else
                        panel.add(new JLabel(dumbVac));
                }
                else if (board[i][j] instanceof Baby) {
                    panel.add(new JLabel(baby));
                }
                else if (board[i][j] instanceof Furniture) {
                    panel.add(new JLabel(obstacle));
                }
                else if (board[i][j] instanceof ChargingStation) {
                    panel.add(new JLabel(charger));
                }
                else{
                    if(vacuumed[i][j]){
                        panel.add(new JLabel(cleanTile));
                    }
                    else{
                        panel.add(new JLabel(dirtyTile));
                    }
                }
            }
        }
    }
}
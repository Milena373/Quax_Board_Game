package comp20050.qssboard;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.util.List;
import java.util.Random;

public class BotPlayer {

    private Random rand = new Random();

    public Polygon chooseMove(List<Polygon> cells) {

        while (true) {
            Polygon cell = cells.get(rand.nextInt(cells.size()));

            if (!cell.getFill().equals(Color.BLACK) &&
                    !cell.getFill().equals(Color.WHITE)) {

                return cell;
            }
        }
    }
}
package comp20050.qssboard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test for BotPlayer + QuaxController + real board wiring.
 *
 * This uses the actual FXML controller and actual Dijkstra implementation.
 */
public class IntTestBotStrategy extends ApplicationTest {

    private QuaxController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(QuaxApplication.class.getResource("quax-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        controller = fxmlLoader.getController();
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void botChoosesMissingCell_inNearCompleteBlackTopToBottomChain() {
        // Build a nearly complete black vertical chain in column 0.
        // Missing cell is OctCell56 (row 5, col 0), which should be the best winning move.
        int[] blackChainExceptGap = {1, 12, 23, 34, 45, 67, 78, 89, 100, 111};
        for (int id : blackChainExceptGap) {
            lookup("#OctCell" + id).queryAs(Polygon.class).setFill(Color.BLACK);
        }

        List<Polygon> allCells = new ArrayList<>();
        for (int i = 1; i <= 121; i++) {
            allCells.add(lookup("#OctCell" + i).queryAs(Polygon.class));
        }
        for (int i = 1; i <= 100; i++) {
            allCells.add(lookup("#RhoCell" + i).queryAs(Polygon.class));
        }

        BotPlayer bot = new BotPlayer();
        Polygon chosen = bot.chooseMove(allCells, Color.BLACK, controller);

        assertEquals("OctCell56", chosen.getId(),
                "With a single missing stone in a top-to-bottom black chain, the bot should choose that winning cell");
    }

    @Test
    void botChoosesMissingCell_inNearCompleteWhiteLeftToRightChain() {
        // Row 0 across the top: ids 1..11
        // Leave a single gap at OctCell6.
        int[] whiteChainExceptGap = {1, 2, 3, 4, 5, 7, 8, 9, 10, 11};
        for (int id : whiteChainExceptGap) {
            lookup("#OctCell" + id).queryAs(Polygon.class).setFill(Color.WHITE);
        }

        List<Polygon> allCells = new ArrayList<>();
        for (int i = 1; i <= 121; i++) {
            allCells.add(lookup("#OctCell" + i).queryAs(Polygon.class));
        }
        for (int i = 1; i <= 100; i++) {
            allCells.add(lookup("#RhoCell" + i).queryAs(Polygon.class));
        }

        BotPlayer bot = new BotPlayer();
        Polygon chosen = bot.chooseMove(allCells, Color.WHITE, controller);

        assertEquals("OctCell6", chosen.getId(),
                "With a single missing stone in a left-to-right white chain, the bot should choose that winning cell");
    }

    @Test
    void botBlocksOpponentsImmediateWin_whenBlackCanStopWhiteChain() {
        // WHITE is one move away from connecting left-to-right on row 0.
        // Gap is OctCell6, so BLACK should block there.
        int[] whiteThreat = {1, 2, 3, 4, 5, 7, 8, 9, 10, 11};
        for (int id : whiteThreat) {
            lookup("#OctCell" + id).queryAs(Polygon.class).setFill(Color.WHITE);
        }

        List<Polygon> allCells = new ArrayList<>();
        for (int i = 1; i <= 121; i++) {
            allCells.add(lookup("#OctCell" + i).queryAs(Polygon.class));
        }
        for (int i = 1; i <= 100; i++) {
            allCells.add(lookup("#RhoCell" + i).queryAs(Polygon.class));
        }

        BotPlayer bot = new BotPlayer();
        Polygon chosen = bot.chooseMove(allCells, Color.BLACK, controller);

        assertEquals("OctCell6", chosen.getId(),
                "When White has an immediate winning move, Black should block that cell");
    }

    @Test
    void botNeverChoosesOccupiedCell() {
        lookup("#OctCell56").queryAs(Polygon.class).setFill(Color.BLACK);
        lookup("#OctCell57").queryAs(Polygon.class).setFill(Color.WHITE);
        lookup("#RhoCell10").queryAs(Polygon.class).setFill(Color.BLACK);

        List<Polygon> allCells = new ArrayList<>();
        for (int i = 1; i <= 121; i++) {
            allCells.add(lookup("#OctCell" + i).queryAs(Polygon.class));
        }
        for (int i = 1; i <= 100; i++) {
            allCells.add(lookup("#RhoCell" + i).queryAs(Polygon.class));
        }

        BotPlayer bot = new BotPlayer();
        Polygon chosen = bot.chooseMove(allCells, Color.BLACK, controller);

        Color fill = (Color) chosen.getFill();
        boolean occupied = Color.BLACK.equals(fill) || Color.WHITE.equals(fill);

        assertEquals(false, occupied,
                "Bot should never choose a cell that is already occupied");
    }

    @Test
    void botPrefersOwnWinningMove_overBlockingOpponent() {
        // BLACK can win immediately by filling OctCell56
        int[] blackChainExceptGap = {1, 12, 23, 34, 45, 67, 78, 89, 100, 111};
        for (int id : blackChainExceptGap) {
            lookup("#OctCell" + id).queryAs(Polygon.class).setFill(Color.BLACK);
        }

        // WHITE also has a near-complete row with a gap at OctCell6
        int[] whiteThreat = {1, 2, 3, 4, 5, 7, 8, 9, 10, 11};
        for (int id : whiteThreat) {
            // avoid overwriting black pieces if any overlap
            Polygon p = lookup("#OctCell" + id).queryAs(Polygon.class);
            if (!Color.BLACK.equals(p.getFill())) {
                p.setFill(Color.WHITE);
            }
        }

        List<Polygon> allCells = new ArrayList<>();
        for (int i = 1; i <= 121; i++) {
            allCells.add(lookup("#OctCell" + i).queryAs(Polygon.class));
        }
        for (int i = 1; i <= 100; i++) {
            allCells.add(lookup("#RhoCell" + i).queryAs(Polygon.class));
        }

        BotPlayer bot = new BotPlayer();
        Polygon chosen = bot.chooseMove(allCells, Color.BLACK, controller);

        assertEquals("OctCell56", chosen.getId(),
                "If the bot can win immediately, it should take the winning move instead of blocking");
    }
}

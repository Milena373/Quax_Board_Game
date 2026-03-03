package comp20050.qssboard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

public class UnitIntTestTurnIndicator extends ApplicationTest {

    @Test
    void testTurnToggle() {
        boolean isBlackTurn = true;
        isBlackTurn = !isBlackTurn;
        assertFalse(isBlackTurn, "turn should be white after toggle");
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(QuaxApplication.class.getResource("quax-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testCellClickChangesColour() {
        // find polygon by its fx:id
        Polygon cell = lookup("#OctCell1").queryAs(Polygon.class);

        // simulate a real mouse click
        clickOn(cell);

        // verify state
        assertEquals(Color.BLACK, cell.getFill());
    }

    @Test
    void testTurnIndicatorUpdates() {
        // click cell
        clickOn("#RhoCell1");

        // verify label text matches logic
        verifyThat("#turnLabel", (Label l) -> l.getText().contains("WHITE to play"));
    }

    @Test
    void testCellColourCannotBeOverwritten() {
        Polygon cell = lookup("#RhoCell1").queryAs(Polygon.class);

        // first click (black's turn)
        clickOn(cell);
        assertEquals(Color.BLACK, cell.getFill());

        // second click on the same cell (should be ignored)
        clickOn(cell);

        // verify it didn't change to white
        assertEquals(Color.BLACK, cell.getFill());

        // verify turn didn't advance (should still be white's turn)
        verifyThat("#turnLabel", (Label l) -> l.getText().contains("WHITE to play"));
    }
}
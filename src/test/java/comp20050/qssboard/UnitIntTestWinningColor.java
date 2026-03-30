package comp20050.qssboard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for win detection and winner display.
 */
public class UnitIntTestWinningColor extends ApplicationTest{

    private QuaxController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(QuaxApplication.class.getResource("quax-view.fxml"));
        Parent board = fxmlLoader.load();
        controller = fxmlLoader.getController();

        double scale = 0.6;
        board.setScaleX(scale);
        board.setScaleY(scale);

        javafx.scene.Group fixedWrapper = new javafx.scene.Group(board);

        javafx.scene.layout.StackPane root = new javafx.scene.layout.StackPane(fixedWrapper);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    void test_blackVerticalStraightWin(){

        clickOn("#OctCell5"); // BLACK
        clickOn("#OctCell34"); // WHITE
        clickOn("#OctCell16"); // BLACK
        clickOn("#OctCell35"); // WHITE
        clickOn("#OctCell27"); // BLACK
        clickOn("#OctCell36"); // WHITE
        clickOn("#OctCell38"); // BLACK
        clickOn("#OctCell25"); // WHITE
        clickOn("#OctCell49"); // BLACK
        clickOn("#OctCell14"); // WHITE
        clickOn("#OctCell60"); // BLACK
        clickOn("#OctCell15"); // WHITE
        clickOn("#OctCell71"); // BLACK
        clickOn("#OctCell17"); // WHITE
        clickOn("#OctCell82"); // BLACK
        clickOn("#OctCell18"); // WHITE
        clickOn("#OctCell93"); // BLACK
        clickOn("#OctCell19"); // WHITE
        clickOn("#OctCell104"); // BLACK
        clickOn("#OctCell20"); // WHITE
        clickOn("#OctCell115"); // BLACK
        var winner = lookup("#winnerLabel").queryAs(javafx.scene.control.Label.class);

        assertTrue(controller.isBlackConnectedTopToBottom());
        assertFalse(controller.isWhiteConnectedLeftToRight());
        assertEquals("BLACK wins!", winner.getText());

    }

    @Test
    void test_blackDiagonalStraightWin(){

        clickOn("#OctCell111"); // BLACK
        clickOn("#OctCell1"); // WHITE
        clickOn("#RhoCell91"); // BLACK
        clickOn("#RhoCell1"); // WHITE
        clickOn("#OctCell101"); // BLACK
        clickOn("#OctCell13"); // WHITE
        clickOn("#RhoCell82"); // BLACK
        clickOn("#OctCell14"); // WHITE
        clickOn("#OctCell91"); // BLACK
        clickOn("#OctCell15"); // WHITE
        clickOn("#RhoCell73"); // BLACK
        clickOn("#OctCell16"); // WHITE
        clickOn("#OctCell81"); // BLACK
        clickOn("#RhoCell5"); // WHITE
        clickOn("#RhoCell64"); // BLACK
        clickOn("#OctCell17"); // WHITE
        clickOn("#OctCell71"); // BLACK
        clickOn("#OctCell18"); // WHITE
        clickOn("#RhoCell55"); // BLACK
        clickOn("#RhoCell17"); // WHITE
        clickOn("#OctCell61"); // BLACK...
        clickOn("#OctCell29"); // WHITE
        clickOn("#RhoCell46"); // BLACK
        clickOn("#OctCell40"); // WHITE
        clickOn("#OctCell51"); // BLACK
        clickOn("#OctCell20"); // WHITE
        clickOn("#RhoCell37"); // BLACK
        clickOn("#OctCell42"); // WHITE
        clickOn("#OctCell41"); // BLACK
        clickOn("#OctCell43"); // WHITE
        clickOn("#RhoCell28"); // BLACK
        clickOn("#OctCell32"); // WHITE
        clickOn("#OctCell31"); // BLACK
        clickOn("#OctCell54"); // WHITE
        clickOn("#RhoCell19"); // BLACK
        clickOn("#OctCell64"); // WHITE
        clickOn("#OctCell21"); // BLACK
        clickOn("#OctCell100"); // WHITE
        clickOn("#RhoCell10"); // BLACK
        clickOn("#OctCell112"); // WHITE
        clickOn("#OctCell11"); // BLACK
        var winner = lookup("#winnerLabel").queryAs(javafx.scene.control.Label.class);

        assertTrue(controller.isBlackConnectedTopToBottom());
        assertFalse(controller.isWhiteConnectedLeftToRight());
        assertEquals("BLACK wins!", winner.getText());

    }

    // In this test case , we look at getting winning chain that takes complex path , vertical,horizontal,and diagonal connection.
    @Test
    void test_blackMixWin(){

        clickOn("#OctCell1");    // BLACK
        clickOn("#OctCell12");   // WHITE

        clickOn("#RhoCell1");    // BLACK
        clickOn("#OctCell23");   // WHITE

        clickOn("#OctCell13");   // BLACK
        clickOn("#RhoCell11");   // WHITE

        clickOn("#OctCell14");   // BLACK
        clickOn("#OctCell24");   // WHITE

        clickOn("#OctCell25");   // BLACK
        clickOn("#RhoCell22");   // WHITE

        clickOn("#OctCell26");   // BLACK
        clickOn("#OctCell36");   // WHITE

        clickOn("#OctCell37");   // BLACK
        clickOn("#RhoCell33");   // WHITE

        clickOn("#OctCell48");   // BLACK
        clickOn("#OctCell47");   // WHITE

        clickOn("#OctCell59");   // BLACK
        clickOn("#RhoCell43");   // WHITE

        clickOn("#OctCell70");   // BLACK
        clickOn("#OctCell58");   // WHITE

        clickOn("#OctCell71");   // BLACK
        clickOn("#OctCell69");   // WHITE

        clickOn("#RhoCell65");   // BLACK
        clickOn("#RhoCell63");   // WHITE

        clickOn("#OctCell83");   // BLACK
        clickOn("#OctCell81");   // WHITE

        clickOn("#OctCell94");   // BLACK
        clickOn("#OctCell82");   // WHITE

        clickOn("#OctCell105");  // BLACK
        clickOn("#RhoCell75");   // WHITE

        clickOn("#OctCell106");  // BLACK
        clickOn("#OctCell93");   // WHITE

        clickOn("#OctCell117");  // BLACK

        var winner = lookup("#winnerLabel").queryAs(javafx.scene.control.Label.class);

        assertTrue(controller.isBlackConnectedTopToBottom());
        assertFalse(controller.isWhiteConnectedLeftToRight());
        assertEquals("BLACK wins!", winner.getText());

    }
// In this test case , we see that many connected chains can be developed but only one will produce a win
    @Test
    void test_black_2ConnectedChainOneWin(){
        clickOn("#OctCell117");  // BLACK
        clickOn("#OctCell45");   // WHITE

        clickOn("#OctCell106");  // BLACK
        clickOn("#OctCell46");   // WHITE

        clickOn("#OctCell95");   // BLACK
        clickOn("#OctCell47");   // WHITE

        clickOn("#OctCell84");   // BLACK
        clickOn("#OctCell48");   // WHITE

        clickOn("#OctCell73");   // BLACK
        clickOn("#OctCell49");   // WHITE

        clickOn("#OctCell4");    // BLACK
        clickOn("#RhoCell44");   // WHITE

        clickOn("#OctCell15");   // BLACK
        clickOn("#OctCell59");   // WHITE

        clickOn("#RhoCell14");   // BLACK
        clickOn("#RhoCell54");   // WHITE

        clickOn("#OctCell27");   // BLACK
        clickOn("#OctCell70");   // WHITE

        clickOn("#RhoCell25");   // BLACK
        clickOn("#RhoCell63");   // WHITE

        clickOn("#OctCell39");   // BLACK
        clickOn("#OctCell80");   // WHITE

        clickOn("#OctCell50");   // BLACK
        clickOn("#OctCell91");   // WHITE

        clickOn("#RhoCell45");   // BLACK
        clickOn("#OctCell102");  // WHITE

        clickOn("#OctCell60");   // BLACK
        clickOn("#OctCell72");   // WHITE

        clickOn("#OctCell71");   // BLACK
        clickOn("#RhoCell56");   // WHITE

        clickOn("#RhoCell64");   // BLACK
        clickOn("#OctCell62");   // WHITE

        clickOn("#OctCell81");   // BLACK
        clickOn("#OctCell63");   // WHITE

        clickOn("#OctCell92");   // BLACK
        clickOn("#OctCell64");   // WHITE

        clickOn("#OctCell103");  // BLACK
        clickOn("#OctCell65");   // WHITE

        clickOn("#OctCell114");  // BLACK

        var winner = lookup("#winnerLabel").queryAs(javafx.scene.control.Label.class);

        assertTrue(controller.isBlackConnectedTopToBottom());
        assertFalse(controller.isWhiteConnectedLeftToRight());
        assertEquals("BLACK wins!", winner.getText());
    }

    @Test

    void test_whiteHorizontalStraightWin(){
        clickOn("#OctCell3");    // BLACK
        clickOn("#OctCell23");   // WHITE

        clickOn("#OctCell4");    // BLACK
        clickOn("#OctCell24");   // WHITE

        clickOn("#OctCell15");   // BLACK
        clickOn("#OctCell25");   // WHITE

        clickOn("#OctCell16");   // BLACK
        clickOn("#OctCell26");   // WHITE

        clickOn("#OctCell17");   // BLACK
        clickOn("#OctCell27");   // WHITE

        clickOn("#OctCell18");   // BLACK
        clickOn("#OctCell28");   // WHITE

        clickOn("#OctCell19");   // BLACK
        clickOn("#OctCell29");   // WHITE

        clickOn("#OctCell20");   // BLACK
        clickOn("#OctCell30");   // WHITE

        clickOn("#OctCell21");   // BLACK
        clickOn("#OctCell31");   // WHITE

        clickOn("#OctCell22");   // BLACK
        clickOn("#OctCell32");   // WHITE

        clickOn("#OctCell43");   // BLACK
        clickOn("#OctCell33");   // WHITE

        var winner = lookup("#winnerLabel").queryAs(javafx.scene.control.Label.class);

        assertFalse(controller.isBlackConnectedTopToBottom());
        assertTrue(controller.isWhiteConnectedLeftToRight());
        assertEquals("WHITE wins!", winner.getText());

    }

    @Test
    void test_whiteDiagonalStraightWin(){
        clickOn("#OctCell1");   // BLACK
        clickOn("#OctCell111"); // WHITE

        clickOn("#RhoCell1");   // BLACK
        clickOn("#RhoCell91");  // WHITE

        clickOn("#OctCell13");  // BLACK
        clickOn("#OctCell101"); // WHITE

        clickOn("#OctCell14");  // BLACK
        clickOn("#RhoCell82");  // WHITE

        clickOn("#OctCell15");  // BLACK
        clickOn("#OctCell91");  // WHITE

        clickOn("#OctCell16");  // BLACK
        clickOn("#RhoCell73");  // WHITE

        clickOn("#RhoCell5");   // BLACK
        clickOn("#OctCell81");  // WHITE

        clickOn("#OctCell17");  // BLACK
        clickOn("#RhoCell64");  // WHITE

        clickOn("#OctCell18");  // BLACK
        clickOn("#OctCell71");  // WHITE

        clickOn("#RhoCell17");  // BLACK
        clickOn("#RhoCell55");  // WHITE

        clickOn("#OctCell29");  // BLACK
        clickOn("#OctCell61");  // WHITE

        clickOn("#OctCell40");  // BLACK
        clickOn("#RhoCell46");  // WHITE

        clickOn("#OctCell20");  // BLACK
        clickOn("#OctCell51");  // WHITE

        clickOn("#OctCell42");  // BLACK
        clickOn("#RhoCell37");  // WHITE

        clickOn("#OctCell43");  // BLACK
        clickOn("#OctCell41");  // WHITE

        clickOn("#OctCell32");  // BLACK
        clickOn("#RhoCell28");  // WHITE

        clickOn("#OctCell54");  // BLACK
        clickOn("#OctCell31");  // WHITE

        clickOn("#OctCell64");  // BLACK
        clickOn("#RhoCell19");  // WHITE

        clickOn("#OctCell100"); // BLACK
        clickOn("#OctCell21");  // WHITE

        clickOn("#OctCell112"); // BLACK
        clickOn("#RhoCell10");  // WHITE

        clickOn("#OctCell113"); // BLACK

        clickOn("#OctCell11");  // WHITE
     var winner = lookup("#winnerLabel").queryAs(javafx.scene.control.Label.class);

        assertFalse(controller.isBlackConnectedTopToBottom());
        assertTrue(controller.isWhiteConnectedLeftToRight());
        assertEquals("WHITE wins!", winner.getText());

    }

    // In this test case , we look at getting winning chain that takes complex path , vertical,horizontal,and diagonal connection.
    @Test
    void test_whiteMixWin(){

        clickOn("#OctCell112"); // BLACK
        clickOn("#OctCell111"); // WHITE

        clickOn("#RhoCell92");  // BLACK
        clickOn("#RhoCell91");  // WHITE

        clickOn("#OctCell102"); // BLACK
        clickOn("#OctCell101"); // WHITE

        clickOn("#RhoCell83");  // BLACK
        clickOn("#RhoCell82");  // WHITE

        clickOn("#OctCell103"); // BLACK
        clickOn("#OctCell91");  // WHITE

        clickOn("#RhoCell84");  // BLACK
        clickOn("#OctCell92");  // WHITE

        clickOn("#OctCell104"); // BLACK
        clickOn("#OctCell93");  // WHITE

        clickOn("#RhoCell85");  // BLACK
        clickOn("#OctCell94");  // WHITE

        clickOn("#OctCell105"); // BLACK
        clickOn("#OctCell83");  // WHITE

        clickOn("#RhoCell86");  // BLACK
        clickOn("#OctCell84");  // WHITE

        clickOn("#OctCell95");  // BLACK
        clickOn("#OctCell85");  // WHITE

        clickOn("#OctCell96");  // BLACK
        clickOn("#OctCell86");  // WHITE

        clickOn("#OctCell97");  // BLACK
        clickOn("#OctCell75");  // WHITE

        clickOn("#OctCell98");  // BLACK
        clickOn("#OctCell76");  // WHITE

        clickOn("#OctCell99");  // BLACK
        clickOn("#OctCell77");  // WHITE
        var winner = lookup("#winnerLabel").queryAs(javafx.scene.control.Label.class);

        assertFalse(controller.isBlackConnectedTopToBottom());
        assertTrue(controller.isWhiteConnectedLeftToRight());
        assertEquals("WHITE wins!", winner.getText());

    }

    // In this test case , we see that many connected chains can be developed but only one will produce a win
    @Test
    void test_white_2ConnectedChainOneWin(){

        clickOn("#OctCell5");    // BLACK
        clickOn("#OctCell92");   // WHITE

        clickOn("#OctCell16");   // BLACK
        clickOn("#OctCell93");   // WHITE

        clickOn("#OctCell38");   // BLACK
        clickOn("#OctCell94");   // WHITE

        clickOn("#OctCell49");   // BLACK
        clickOn("#OctCell23");   // WHITE

        clickOn("#OctCell60");   // BLACK
        clickOn("#OctCell24");   // WHITE

        clickOn("#OctCell72");   // BLACK
        clickOn("#OctCell25");   // WHITE

        clickOn("#OctCell84");   // BLACK
        clickOn("#OctCell26");   // WHITE

        clickOn("#OctCell95");   // BLACK
        clickOn("#OctCell27");   // WHITE

        clickOn("#OctCell106");  // BLACK
        clickOn("#OctCell28");   // WHITE

        clickOn("#OctCell107");  // BLACK
        clickOn("#OctCell29");   // WHITE

        clickOn("#OctCell108");  // BLACK
        clickOn("#OctCell30");   // WHITE

        clickOn("#OctCell109");  // BLACK
        clickOn("#OctCell31");   // WHITE

        clickOn("#OctCell110");  // BLACK
        clickOn("#OctCell32");   // WHITE

        clickOn("#OctCell117");  // BLACK
        clickOn("#OctCell33");   // WHITE

        var winner = lookup("#winnerLabel").queryAs(javafx.scene.control.Label.class);

        assertFalse(controller.isBlackConnectedTopToBottom());
        assertTrue(controller.isWhiteConnectedLeftToRight());
        assertEquals("WHITE wins!", winner.getText());

    }


}

package comp20050.qssboard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

/*
* Integration test for the show strategy button and hide strategy button used to display and remove illustration
*  of the strategy used by the bot at a given move respectively.
* When the show strategy button is clicked the strategy used by the bot should be illustrated on the board and the
* hide strategy button should appear
*
* When the hide strategy button is clicked , the illustrations should disappear and the show strategy button should appear again.
* */

public class IntTestStrategyButton extends ApplicationTest {

    private QuaxController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(QuaxApplication.class.getResource("quax-view.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        root.setScaleX(0.6);
        root.setScaleY(0.6);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //test case: when show strategy button  is clicked, the button after clcicked should disappear and the hide strategy button should appear

    @Test
    public void showStrategyButtonClickedTest(){

        Button showStrategyButton = lookup("#showStrategyButton").queryAs(Button.class);
        Button hideStrategyButton = lookup("#hideStrategyButton").queryAs(Button.class);
        clickOn("#showStrategyButton");

        assertTrue(hideStrategyButton.isVisible());
        assertFalse(showStrategyButton.isVisible());
    }

    //test case: when hide strategy button is clicked , after show strategy button is clicked
    // hide button show disappear and show button should appear

    @Test
    public void hideStrategyButtonClickedTest(){

        Button showStrategyButton = lookup("#showStrategyButton").queryAs(Button.class);
        Button hideStrategyButton = lookup("#hideStrategyButton").queryAs(Button.class);
        clickOn("#showStrategyButton");
        clickOn("#hideStrategyButton");

        assertTrue(showStrategyButton.isVisible());
        assertFalse(hideStrategyButton.isVisible());

    }

    //test case : when the game is loaded up does the show strategy button appear and is the hide button hidden

    @Test
    public void showStrategyButtonStartGameTest(){

        Button showStrategyButton = lookup("#showStrategyButton").queryAs(Button.class);
        Button hideStrategyButton = lookup("#hideStrategyButton").queryAs(Button.class);

        assertTrue(showStrategyButton.isVisible());
        assertFalse(hideStrategyButton.isVisible());

    }

}

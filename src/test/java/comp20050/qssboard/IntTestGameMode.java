package comp20050.qssboard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
public class IntTestGameMode extends ApplicationTest {
    //private HelloController cont;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach

    //We need to ensure that their isnt a default game mode set , so reset is needed betweem test
    void resetGameMode() {
        GameControl.setGameMode(null);
    }

    @Test
    void testOnHumanVsHuman() {
        clickOn("#HVsHButton"); // creating a click for the test

        var modeLabel = lookup("#ModeLabel").queryAs(javafx.scene.control.Label.class);
        var hvsh = lookup("#HVsHButton").queryAs(javafx.scene.control.Button.class);
        var hvsb = lookup("#HVsBButton").queryAs(javafx.scene.control.Button.class);
        var selectionLabel = lookup("#gameModeSelect").queryAs(javafx.scene.control.Label.class);

        assertEquals("Human Vs Human", modeLabel.getText());
        assertFalse(hvsh.isDisabled());
        assertFalse(hvsb.isDisabled());
        assertEquals(0.4, selectionLabel.getOpacity(), 0.0001);
        assertEquals(GameControl.getGameMode(), GameControl.GameMode.HUMAN_VS_HUMAN); // testing the internal logic as well

    }

    @Test
    void testOnHumanVsBot() {
        clickOn("#HVsBButton"); // creating a click for the test

        var modeLabel = lookup("#ModeLabel").queryAs(javafx.scene.control.Label.class);
        var hvsh = lookup("#HVsHButton").queryAs(javafx.scene.control.Button.class);
        var hvsb = lookup("#HVsBButton").queryAs(javafx.scene.control.Button.class);
        var selectionLabel = lookup("#gameModeSelect").queryAs(javafx.scene.control.Label.class);

        assertEquals("Human Vs Bot", modeLabel.getText());
        assertFalse(hvsb.isDisabled());
        assertFalse(hvsh.isDisabled());
        assertEquals(0.4, selectionLabel.getOpacity(), 0.0001);
        assertEquals(GameControl.getGameMode(), GameControl.GameMode.HUMAN_VS_BOT); // testing the internal logic as well

    }

    @Test
    void testDisableUnclcikedMode() {
        clickOn("#HVsHButton");
        var hvsh = lookup("#HVsHButton").queryAs(javafx.scene.control.Button.class);
        var hvsb = lookup("#HVsBButton").queryAs(javafx.scene.control.Button.class);
        var modeLabel = lookup("#ModeLabel").queryAs(javafx.scene.control.Label.class);
        assertTrue(hvsb.isDisable());

        //lets see if it changed back
        clickOn("#HVsBButton");
        assertEquals("Human Vs Human",modeLabel.getText());
        assertEquals(GameControl.GameMode.HUMAN_VS_HUMAN, GameControl.getGameMode());
    }


}

package comp20050.qssboard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

public class UnitIntTestPieRule extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                QuaxApplication.class.getResource("quax-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setX(0);
        stage.setY(0);
        stage.setScene(scene);
        stage.show();
    }

    // UNIT TEST: test that pie rule button appears after first move

    @Test
    void testPieButtonAppears() {

        // first move (black)
        clickOn("#OctCell1");

        Button pieButton = lookup("#pieRuleButton").queryAs(Button.class);

        assertTrue(pieButton.isVisible());
    }

    // INTEGRATION TEST: test that clicking the pie rule button swaps the turn

    @Test
    void testPieRuleSwitchesTurn() {

        clickOn("#OctCell1");       // black first move
        clickOn("#pieRuleButton");  // pie rule activated

        // verify that turn label updated
        verifyThat("#turnLabel", (Label l) -> l.getText().contains("WHITE to play"));
    }

    // test that pie rule can't be used more than once

    @Test
    void testPieButtonAvailableOnce() {

        clickOn("#OctCell1");        // black first move
        clickOn("#pieRuleButton");   // pie rule activated

        Button pieButton = lookup("#pieRuleButton").queryAs(Button.class);

        System.out.println("Visible: " + pieButton.isVisible());
        System.out.println("Disabled: " + pieButton.isDisabled());

        // button should disappear and be disabled
        assertFalse(pieButton.isVisible());
        assertTrue(pieButton.isDisabled());
    }
}
package comp20050.qssboard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

public class UnitIntTestPieRule extends ApplicationTest {


   private QuaxController controller;


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(QuaxApplication.class.getResource("quax-view.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        root.setScaleX(0.4);
        root.setScaleY(0.4);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // UNIT TEST: test that pie rule button appears after first move

    @Test
    void testPieButtonAppears() {

        // first move (black)
//        clickOn("#OctCell1");
//        WaitForAsyncUtils.waitForFxEvents();
        Button pieButton = lookup("#pieRuleButton").queryAs(Button.class);

        System.out.println("Visible: " + pieButton.isVisible());
        System.out.println("Disabled: " + pieButton.isDisabled());
        System.out.println("Managed: " + pieButton.isManaged());
        assertTrue(pieButton.isVisible(), "Pie rule button should be visible after first move");
    }

    // INTEGRATION TEST: test that clicking the pie rule button swaps the turn

    @Test
    void testPieRuleSwitchesTurn() {

        //clickOn("#OctCell1");       // black first move
        WaitForAsyncUtils.waitForFxEvents();

        Button pieButton = lookup("#pieRuleButton").queryAs(Button.class);
        assertTrue(pieButton.isVisible(), "Pie rule button should be visible before clicking it");
        clickOn("#pieRuleButton");  // pie rule activated
        WaitForAsyncUtils.waitForFxEvents(); //creating a delay

        // verify that turn label updated
        verifyThat("#turnLabel", (Label l) -> l.getText().contains("BLACK to play"));
    }

    // test that pie rule can't be used more than once

    @Test
    void testPieButtonAvailableOnce() {

        //clickOn("#OctCell1");        // black first move
        WaitForAsyncUtils.waitForFxEvents();
        Button pieButton = lookup("#pieRuleButton").queryAs(Button.class);
        assertTrue(pieButton.isVisible(), "Pie rule button should be visible before clicking it");
        clickOn("#pieRuleButton");   // pie rule activated
        WaitForAsyncUtils.waitForFxEvents();

        System.out.println("Visible: " + pieButton.isVisible());
        System.out.println("Disabled: " + pieButton.isDisabled());

        // button should disappear and be disabled
        assertFalse(pieButton.isVisible());
        assertTrue(pieButton.isDisabled());
    }
}
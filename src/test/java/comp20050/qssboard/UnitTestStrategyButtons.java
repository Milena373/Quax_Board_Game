package comp20050.qssboard;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/*
* Unit test for the method onShowStrategyButton and onHideStrategyButton
* when onShowStrategyButton - show strategy button disappears and strategy button appears
* when onHideStrategyButton - hide strategy button disappears and show strategy button appears */

class UnitTestStrategyButtons {

    private QuaxController controller;
    private Button showStrategyButton;
    private Button hideStrategyButton;

    @BeforeAll
    static void initJavaFx() {
        new JFXPanel(); // initializes JavaFX toolkit
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new QuaxController();

        showStrategyButton = new Button("Show Strategy");
        hideStrategyButton = new Button("Hide Strategy");

        showStrategyButton.setVisible(true);
        hideStrategyButton.setVisible(false);

        injectField("showStrategyButton", showStrategyButton);
        injectField("hideStrategyButton", hideStrategyButton);
    }

    @Test
    void onShowStrategyButton() throws Exception {
        Method method = QuaxController.class.getDeclaredMethod("onShowStrategyButton");
        method.setAccessible(true);
        method.invoke(controller);

        assertFalse(showStrategyButton.isVisible());
        assertTrue(hideStrategyButton.isVisible());
    }

    @Test
    void onHideStrategyButton() throws Exception {
        showStrategyButton.setVisible(false);
        hideStrategyButton.setVisible(true);

        Method method = QuaxController.class.getDeclaredMethod("onHideStrategyButton");
        method.setAccessible(true);
        method.invoke(controller);

        assertTrue(showStrategyButton.isVisible());
        assertFalse(hideStrategyButton.isVisible());
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = QuaxController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }
}

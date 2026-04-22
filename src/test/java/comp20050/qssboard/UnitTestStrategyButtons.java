package comp20050.qssboard;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Polygon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Unit test for the methods onShowStrategyButton and onHideStrategyButton.
 * - onShowStrategyButton: show strategy button disappears and hide strategy button appears
 * - onHideStrategyButton: hide strategy button disappears and show strategy button appears
 */
class UnitTestStrategyButtons {

    private QuaxController controller;
    private Button showStrategyButton;
    private Button hideStrategyButton;
    private ShowStrategy showStrategy;

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
        showStrategyButton.setDisable(false);

        hideStrategyButton.setVisible(false);
        hideStrategyButton.setDisable(true);

        Group boardContainer = new Group();
        Map<String, Polygon> cellMap = new HashMap<>();

        showStrategy = new ShowStrategy(
                boardContainer,
                cellMap,
                showStrategyButton,
                hideStrategyButton
        );

        injectField("showStrategyButton", showStrategyButton);
        injectField("hideStrategyButton", hideStrategyButton);
        injectField("showStrategy", showStrategy);
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
        Method showMethod = QuaxController.class.getDeclaredMethod("onShowStrategyButton");
        showMethod.setAccessible(true);
        showMethod.invoke(controller);

        Method hideMethod = QuaxController.class.getDeclaredMethod("onHideStrategyButton");
        hideMethod.setAccessible(true);
        hideMethod.invoke(controller);

        assertTrue(showStrategyButton.isVisible());
        assertFalse(hideStrategyButton.isVisible());
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = QuaxController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }
}
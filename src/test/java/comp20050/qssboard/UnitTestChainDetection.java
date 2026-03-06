package comp20050.qssboard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTestChainDetection extends ApplicationTest {

    private QuaxController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(QuaxApplication.class.getResource("quax-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        controller = fxmlLoader.getController();
        stage.setScene(scene);
        stage.show();
    }

    // unit test 1 - single cell chain
    @Test
    void test_feature4_1_singleCellChain() {
        clickOn("#OctCell1");
        Polygon cell = lookup("#OctCell1").queryAs(Polygon.class);
        List<String> chain = controller.getConnectedChain("OctCell1", Color.BLACK);
        assertEquals(1, chain.size());
        assertTrue(chain.contains("OctCell1"));
    }

    // unit test 2 - horizontal chain
    @Test
    void test_feature4_2_horizontalChain() {
        clickOn("#OctCell1"); // BLACK
        clickOn("#OctCell55"); // WHITE - skip turn
        clickOn("#OctCell2"); // BLACK
        clickOn("#OctCell56"); // WHITE - skip turn
        clickOn("#OctCell3"); // BLACK

        List<String> chain = controller.getConnectedChain("OctCell1", Color.BLACK);
        assertEquals(3, chain.size());
        assertTrue(chain.contains("OctCell1"));
        assertTrue(chain.contains("OctCell2"));
        assertTrue(chain.contains("OctCell3"));
    }

    // unit test 3 - vertical chain
    @Test
    void test_feature4_3_verticalChain() {
        clickOn("#OctCell1");  // BLACK
        clickOn("#OctCell55"); // WHITE - skip turn
        clickOn("#OctCell12"); // BLACK
        clickOn("#OctCell56"); // WHITE - skip turn
        clickOn("#OctCell23"); // BLACK

        List<String> chain = controller.getConnectedChain("OctCell1", Color.BLACK);
        assertEquals(3, chain.size());
        assertTrue(chain.contains("OctCell1"));
        assertTrue(chain.contains("OctCell12"));
        assertTrue(chain.contains("OctCell23"));
    }

    // unit test 4 - diagonal chain via rhombus
    @Test
    void test_feature4_4_diagonalChain() {
        clickOn("#OctCell1");  // BLACK
        clickOn("#OctCell55"); // WHITE - skip turn
        clickOn("#RhoCell1");  // BLACK
        clickOn("#OctCell56"); // WHITE - skip turn
        clickOn("#OctCell13"); // BLACK

        List<String> chain = controller.getConnectedChain("OctCell1", Color.BLACK);
        assertEquals(2, chain.size());
        assertTrue(chain.contains("OctCell1"));
        assertTrue(chain.contains("OctCell13"));
    }

    // unit test 5 - diagonal without rhombus bridge (should NOT connect)
    @Test
    void test_feature4_5_diagonalWithoutBridge() {
        clickOn("#OctCell1");  // BLACK
        clickOn("#OctCell55"); // WHITE - skip turn
        clickOn("#OctCell13"); // BLACK

        List<String> chain = controller.getConnectedChain("OctCell1", Color.BLACK);
        assertEquals(1, chain.size()); // should NOT contain OctCell13
        assertFalse(chain.contains("OctCell13"));
    }

    // unit test 6 - two separate chains should not merge
    @Test
    void test_feature4_6_separateChains() {
        clickOn("#OctCell1");  // BLACK
        clickOn("#OctCell55"); // WHITE - skip turn
        clickOn("#OctCell3");  // BLACK - not adjacent to OctCell1

        List<String> chain = controller.getConnectedChain("OctCell1", Color.BLACK);
        assertEquals(1, chain.size());
        assertFalse(chain.contains("OctCell3"));
    }

    // unit test 7 - white chain detection
    @Test
    void test_feature4_8_whiteChainDetection() {
        clickOn("#OctCell55"); // BLACK - skip turn
        clickOn("#OctCell1");  // WHITE
        clickOn("#OctCell56"); // BLACK - skip turn
        clickOn("#OctCell2");  // WHITE

        List<String> chain = controller.getConnectedChain("OctCell1", Color.WHITE);
        assertEquals(2, chain.size());
        assertTrue(chain.contains("OctCell1"));
        assertTrue(chain.contains("OctCell2"));
    }

}
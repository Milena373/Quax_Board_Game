/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package comp20050.qssboard;

import javafx.scene.control.Label; //added
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;

import static javafx.scene.paint.Color.BLACK;

public class HelloController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="OctCell1"
    private Polygon OctCell1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell10"
    private Polygon OctCell10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell11"
    private Polygon OctCell11; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell12"
    private Polygon OctCell12; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell13"
    private Polygon OctCell13; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell14"
    private Polygon OctCell14; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell15"
    private Polygon OctCell15; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell16"
    private Polygon OctCell16; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell2"
    private Polygon OctCell2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell3"
    private Polygon OctCell3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell4"
    private Polygon OctCell4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell5"
    private Polygon OctCell5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell6"
    private Polygon OctCell6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell7"
    private Polygon OctCell7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell8"
    private Polygon OctCell8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell9"
    private Polygon OctCell9; // Value injected by FXMLLoader

    @FXML // fx:id="RhoCell1"
    private Polygon RhoCell1; // Value injected by FXMLLoader

    @FXML // fx:id="RhoCell2"
    private Polygon RhoCell2; // Value injected by FXMLLoader

    @FXML // fx:id="RhoCell3"
    private Polygon RhoCell3; // Value injected by FXMLLoader

    @FXML // fx:id="RhoCell4"
    private Polygon RhoCell4; // Value injected by FXMLLoader

    @FXML // fx:id="RhoCell5"
    private Polygon RhoCell5; // Value injected by FXMLLoader

    @FXML // fx:id="RhoCell6"
    private Polygon RhoCell6; // Value injected by FXMLLoader

    @FXML // fx:id="RhoCell7"
    private Polygon RhoCell7; // Value injected by FXMLLoader

    @FXML // fx:id="RhoCell8"
    private Polygon RhoCell8; // Value injected by FXMLLoader

    // added code
    @FXML
    private Label turnLabel;

    @FXML
    private Polygon turnOctagon;

    @FXML
    private Polygon turnRhombus;

    private boolean isBlackTurn = true;

    @FXML
    void getCellID(MouseEvent event) {
        Polygon cell = (Polygon) event.getSource();

        // if cell has been clicked already
        if (cell.getFill().equals(javafx.scene.paint.Color.BLACK) ||
                cell.getFill().equals(javafx.scene.paint.Color.WHITE)) {
            return;
        }

        // set color of clicked cell
        if (isBlackTurn) {
            cell.setFill(javafx.scene.paint.Color.BLACK);
        } else {
            cell.setFill(javafx.scene.paint.Color.WHITE);
            cell.setStroke(javafx.scene.paint.Color.BLACK);
        }

        // switch turn
        isBlackTurn = !isBlackTurn;

        // update display for next player
        if (isBlackTurn) {
            turnLabel.setText("BLACK to play :)");
            turnOctagon.setFill(javafx.scene.paint.Color.BLACK);
            turnRhombus.setFill(javafx.scene.paint.Color.BLACK);
        } else {
            turnLabel.setText("WHITE to play :)");
            turnOctagon.setFill(javafx.scene.paint.Color.WHITE);
            turnOctagon.setStroke(javafx.scene.paint.Color.BLACK);
            turnRhombus.setFill(javafx.scene.paint.Color.WHITE);
            turnRhombus.setStroke(javafx.scene.paint.Color.BLACK);
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert OctCell1 != null : "fx:id=\"OctCell1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell10 != null : "fx:id=\"OctCell10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell11 != null : "fx:id=\"OctCell11\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell12 != null : "fx:id=\"OctCell12\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell13 != null : "fx:id=\"OctCell13\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell14 != null : "fx:id=\"OctCell14\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell15 != null : "fx:id=\"OctCell15\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell16 != null : "fx:id=\"OctCell16\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell2 != null : "fx:id=\"OctCell2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell3 != null : "fx:id=\"OctCell3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell4 != null : "fx:id=\"OctCell4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell5 != null : "fx:id=\"OctCell5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell6 != null : "fx:id=\"OctCell6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell7 != null : "fx:id=\"OctCell7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell8 != null : "fx:id=\"OctCell8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell9 != null : "fx:id=\"OctCell9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert RhoCell1 != null : "fx:id=\"RhoCell1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert RhoCell2 != null : "fx:id=\"RhoCell2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert RhoCell3 != null : "fx:id=\"RhoCell3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert RhoCell4 != null : "fx:id=\"RhoCell4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert RhoCell5 != null : "fx:id=\"RhoCell5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert RhoCell6 != null : "fx:id=\"RhoCell6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert RhoCell7 != null : "fx:id=\"RhoCell7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert RhoCell8 != null : "fx:id=\"RhoCell8\" was not injected: check your FXML file 'hello-view.fxml'.";

    }
}
package comp20050.qssboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX application setup and UI initialisation
 */
public class QuaxApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Load UI layout from FXML
        var fxmlUrl = QuaxApplication.class.getResource("/comp20050/qssboard/quax-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        Pane board = fxmlLoader.load();

        // Root container used to manage scaling
        StackPane root = new StackPane(board);

        // Adjust board scale when window size changes
        root.widthProperty().addListener((obs, oldVal, newVal) -> calculateScale(board, root));
        root.heightProperty().addListener((obs, oldVal, newVal) -> calculateScale(board, root));

        Scene scene = new Scene(root);
        stage.setTitle("QSS Board Game");
        stage.setScene(scene);

        stage.setMaximized(true);
        stage.show();
    }

    /**
     * Scales the board to fit within the window
     */
    private void calculateScale(Pane board, StackPane root) {
        double windowWidth = root.getWidth();
        double windowHeight = root.getHeight();
        double boardWidth = board.getPrefWidth();
        double boardHeight = board.getPrefHeight();

        double scaleX = windowWidth / boardWidth;
        double scaleY = windowHeight / boardHeight;
        double finalScale = Math.min(scaleX, scaleY);

        if (finalScale < 1.0) {
            board.setScaleX(finalScale);
            board.setScaleY(finalScale);
        }
    }
}

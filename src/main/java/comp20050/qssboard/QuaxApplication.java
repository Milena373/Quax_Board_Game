package comp20050.qssboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class QuaxApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        var url = QuaxApplication.class.getResource("/comp20050/qssboard/quax-view.fxml");
        //System.err.println("FXML url = " + url);
        FXMLLoader fxmlLoader = new FXMLLoader(url);


        // 1. Load your board as a Pane
        Pane board = fxmlLoader.load();

        // 2. Wrap the board in a StackPane (this acts as a "frame")
        StackPane root = new StackPane(board);

        // 3. This listener scales the board down if the window is too small
        root.widthProperty().addListener((obs, oldVal, newVal) -> calculateScale(board, root));
        root.heightProperty().addListener((obs, oldVal, newVal) -> calculateScale(board, root));

        Scene scene = new Scene(root);
        stage.setTitle("QSS Board Game");
        stage.setScene(scene);

        // 4. Open in maximized window
        stage.setMaximized(true);
        stage.show();
    }
    private void calculateScale(Pane board, StackPane root) {
        double windowWidth = root.getWidth();
        double windowHeight = root.getHeight();
        double boardWidth = board.getPrefWidth();
        double boardHeight = board.getPrefHeight();

        // Find the scale factor that fits the board in the window
        double scaleX = windowWidth / boardWidth;
        double scaleY = windowHeight / boardHeight;
        double finalScale = Math.min(scaleX, scaleY);

        // Don't scale up (keep quality), only scale down if needed
        if (finalScale < 1.0) {
            board.setScaleX(finalScale);
            board.setScaleY(finalScale);
        }
    }
}

package comp20050.qssboard;

import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.Group;

public class QuaxController {

    @FXML private Label turnLabel;
    @FXML private Polygon turnOctagon;
    @FXML private Polygon turnRhombus;
    @FXML private Label winnerLabel;
    @FXML private Button pieRuleButton;
    @FXML private Group boardContainer;
    @FXML private Button showStrategyButton;
    @FXML private Button hideStrategyButton;

    private final Map<String, Polygon> cellMap = new HashMap<>();
    private final List<Polygon> allCells = new ArrayList<>();

    private BotPlayer bot = new BotPlayer();
    private Player player1 ;
    private Player player2 ;

    private boolean pieRuleAvailable = true;
    private boolean isBlackTurn = true;
    private boolean gameOver = false;
    private boolean botIsBlack = true;

    private static final int BOARD_SIZE = 11;
    private static final int LAST_INDEX = BOARD_SIZE - 1;
    private static final int RHO_SIZE = BOARD_SIZE - 1;



    @FXML
    private void botMove() {
        Polygon move = bot.chooseMove(allCells);

        if (botIsBlack) {
            move.setFill(Color.BLACK);
        } else {
            move.setFill(Color.WHITE);
        }

        move.setStroke(Color.BLACK);

        updatePieRuleState(move.getFill().equals(Color.BLACK) ? Color.BLACK : Color.WHITE);
        checkForWinner();
        isBlackTurn = !isBlackTurn;
        updateTurnDisplay();
    }

    @FXML
    void handleCellClick(MouseEvent event) {
        if (gameOver) {
            return;
        }

        Polygon clickedCell = (Polygon) event.getSource();
        if (cellIsOccupied(clickedCell)) {
            return;
        }

        javafx.scene.paint.Color currentColor = placePiece(clickedCell);
        updatePieRuleState(currentColor);
        checkForWinner();
        switchTurn();
        updateTurnDisplay();

        if (!gameOver && isBlackTurn == botIsBlack) {
            botMove();
            checkForWinner();
        }
    }

    /**
     * Returns true if the selected cell already contains a piece.
     */
    private boolean cellIsOccupied(Polygon cell) {
        return cell.getFill().equals(javafx.scene.paint.Color.BLACK)
                || cell.getFill().equals(javafx.scene.paint.Color.WHITE);
    }

    /**
     * Places the current player's piece in the selected cell
     * and returns the colour that was placed.
     */
    private javafx.scene.paint.Color placePiece(Polygon cell) {
        if (isBlackTurn) {
            cell.setFill(javafx.scene.paint.Color.BLACK);
            return javafx.scene.paint.Color.BLACK;
        }

        cell.setFill(javafx.scene.paint.Color.WHITE);
        cell.setStroke(javafx.scene.paint.Color.BLACK);
        return javafx.scene.paint.Color.WHITE;
    }

    /**
     * Updates pie rule availability after a move is made.
     */
    private void updatePieRuleState(javafx.scene.paint.Color currentColor) {
        if (!pieRuleAvailable) {
            return;
        }

        if (currentColor.equals(javafx.scene.paint.Color.BLACK)) {
            showPieButton();
            return;
        }

        hidePieButton();
        pieRuleAvailable = false;
    }

    /**
     * Checks whether either player has won and updates the winner label.
     */
    private void checkForWinner() {
        if (isBlackConnectedTopToBottom()) {
            gameOver = true;
            winnerLabel.setText("BLACK wins!");
            return;
        }

        if (isWhiteConnectedLeftToRight()) {
            gameOver = true;
            winnerLabel.setText("WHITE wins!");
        }
    }

    /**
     * Switches to the other player's turn if the game is still active.
     */
    private void switchTurn() {
        if (!gameOver) {
            isBlackTurn = !isBlackTurn;
        }
    }

    // converts OctCell ID to row/col (0-indexed)
    private int[] octToRowCol(int id) {
        return new int[]{(id - 1) / BOARD_SIZE, (id - 1) % BOARD_SIZE};
    }

    // converts row/col to OctCell ID
    private int rowColToOct(int row, int col) {
        return row * BOARD_SIZE + col + 1;
    }

    // gets the RhoCell ID between two diagonally adjacent octagons
    private int getRhoCellID(int row1, int col1, int row2, int col2) {
        int rhoRow = Math.min(row1, row2);
        int rhoCol = Math.min(col1, col2);
        return rhoRow * RHO_SIZE + rhoCol + 1;
    }

    private boolean isCellOwnedBy(String cellId, javafx.scene.paint.Color color) {
        Polygon cell = cellMap.get(cellId);
        return cell != null && cell.getFill().equals(color);
    }

    public List<String> getConnectedChain(String startCellID, javafx.scene.paint.Color playerColor) {
        List<String> visited = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();

        // only works for OctCells as start
        if (!startCellID.startsWith("OctCell")) return visited;
        if (!isCellOwnedBy(startCellID, playerColor)) return visited;

        queue.add(startCellID);
        visited.add(startCellID);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int id = Integer.parseInt(current.replace("OctCell", ""));
            int[] rc = octToRowCol(id);
            int row = rc[0], col = rc[1];

            // horizontal/vertical neighbours
            int[][] directOffsets = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            for (int[] off : directOffsets) {
                int newRow = row + off[0];
                int newCol = col + off[1];
                if (newRow < 0 || newRow > LAST_INDEX || newCol < 0 || newCol > LAST_INDEX) continue;
                String nID = "OctCell" + rowColToOct(newRow, newCol);
                if (!visited.contains(nID) && isCellOwnedBy(nID, playerColor)) {
                    visited.add(nID);
                    queue.add(nID);
                }
            }

            // diagonal neighbours - only if same colour RhoCell bridge exists
            int[][] diagOffsets = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
            for (int[] off : diagOffsets) {
                int newRow = row + off[0];
                int newCol = col + off[1];
                if (newRow < 0 || newRow > 10 || newCol < 0 || newCol > 10) continue;
                int rhoID = getRhoCellID(row, col, newRow, newCol);
                String rID = "RhoCell" + rhoID;
                String nID = "OctCell" + rowColToOct(newRow, newCol);
                if (!visited.contains(nID) && isCellOwnedBy(rID, playerColor) && isCellOwnedBy(nID, playerColor)) {
                    visited.add(nID);
                    queue.add(nID);
                }
            }
        }
        return visited;
    }

    public boolean isBlackConnectedTopToBottom() {
        // check every cell in the top row
        for (int col = 0; col <= LAST_INDEX; col++) {
            String startID = "OctCell" + rowColToOct(0, col);
            // only start BFS from BLACK cells
            if (!isCellOwnedBy(startID, javafx.scene.paint.Color.BLACK)) continue;

            // get the full connected chain from this starting cell
            List<String> chain = getConnectedChain(startID, javafx.scene.paint.Color.BLACK);

            // check if any cell in the chain is in the bottom row
            for (String cellID : chain) {
                int id = Integer.parseInt(cellID.replace("OctCell", ""));
                int[] rc = octToRowCol(id);
                if (rc[0] == LAST_INDEX) return true; // reached bottom row
            }
        }
        return false;
    }

    public boolean isWhiteConnectedLeftToRight() {
        // check every cell in the left column
        for (int row = 0; row <= LAST_INDEX; row++) {
            String startID = "OctCell" + rowColToOct(row, 0);
            // only start BFS from WHITE cells
            if (!isCellOwnedBy(startID, javafx.scene.paint.Color.WHITE)) continue;

            // get the full connected chain from this starting cell
            List<String> chain = getConnectedChain(startID, javafx.scene.paint.Color.WHITE);

            // check if any cell in the chain is in the right column
            for (String cellID : chain) {
                int id = Integer.parseInt(cellID.replace("OctCell", ""));
                int[] rc = octToRowCol(id);
                if (rc[1] == LAST_INDEX) return true; // reached right column
            }
        }
        return false;
    }

    private void updateTurnDisplay(){
        if (isBlackTurn) {
            turnLabel.setText("BLACK to play");
            turnOctagon.setFill(javafx.scene.paint.Color.BLACK);
            turnRhombus.setFill(javafx.scene.paint.Color.BLACK);

        } else {
            turnLabel.setText("WHITE to play");
            turnOctagon.setFill(javafx.scene.paint.Color.WHITE);
            turnOctagon.setStroke(javafx.scene.paint.Color.BLACK);
            turnRhombus.setFill(javafx.scene.paint.Color.WHITE);
            turnRhombus.setStroke(javafx.scene.paint.Color.BLACK);
        }
    }

    private void showPieButton(){
        pieRuleButton.setVisible(true);
        pieRuleButton.setDisable(false);
    }

    private void hidePieButton(){
        pieRuleButton.setVisible(false);
        pieRuleButton.setDisable(true);
    }

    @FXML
    private void onShowStrategyButton(){
        showStrategyButton.setVisible(false);

        //call to a function that shows trategy : STILL DEVELOPEMENT

        hideStrategyButton.setVisible(true);
    }

    @FXML
    private void onHideStrategyButton(){

        hideStrategyButton.setVisible(false);

        //call to a function that will remove strategy illustration : STILL DEVELOPMENT

        showStrategyButton.setVisible(true);

    }
    @FXML
    private void onPieRule(){
        if(!pieRuleAvailable){
            return;
        }

        // swap the colours of players
        GameControl.PlayerTurn temp = player1.getPlayerColor();
        player1.setPlayerColor(player2.getPlayerColor());
        player2.setPlayerColor(temp);

        botIsBlack = !botIsBlack;

        hidePieButton();
        pieRuleAvailable = false;
        updateTurnDisplay();

        if (!gameOver && isBlackTurn == botIsBlack) {
            botMove();
        }
    }

    private void populateAllCells() {
        // look through every object inside the boardContainer
        for (javafx.scene.Node node : boardContainer.getChildren()) {
            if (node instanceof Polygon polygon) {
                String id = polygon.getId();
                if (id != null && (id.startsWith("OctCell") || id.startsWith("RhoCell"))) {
                    allCells.add(polygon);
                    cellMap.put(id, polygon);
                }
            }
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert pieRuleButton != null : "fx:id=\"pieRuleButton\" was not injected: check your FXML file 'quax-view.fxml'.";
        assert showStrategyButton != null : "fx:id=\"showStrategyButton\" was not injected: check your FXML file 'quax-view.fxml'.";
        assert hideStrategyButton != null : "fx:id=\"hideStrategyButton\" was not injected: check your FXML file 'quax-view.fxml'.";

        player1 = new Player(GameControl.PlayerTurn.BLACK);
        player2 = new Player(GameControl.PlayerTurn.WHITE);

        botIsBlack = true;
        isBlackTurn = true;

        populateAllCells();

        if (allCells.isEmpty()) {
            System.err.println("ERROR: no cells were injected");
        }

        javafx.application.Platform.runLater(() -> {
            if (botIsBlack && isBlackTurn && !allCells.isEmpty()) {
                botMove();
            }
        });

        updateTurnDisplay();
    }
}
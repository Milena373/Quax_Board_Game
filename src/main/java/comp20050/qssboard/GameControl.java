package comp20050.qssboard;

/**
 * Manages the current player's turn
 */
public class GameControl {

    public enum PlayerTurn { BLACK, WHITE }

    private static PlayerTurn playerTurn;

    public GameControl() {}

    public static PlayerTurn getPlayerTurn() {
        return playerTurn;
    }

    public static void setPlayerTurn(PlayerTurn turn) {
        playerTurn = turn;
    }
}
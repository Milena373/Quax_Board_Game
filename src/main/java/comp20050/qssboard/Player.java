package comp20050.qssboard;

/**
 * Represents a player in the game
 */
public class Player {

    private GameControl.PlayerTurn playerColor;

    public Player(GameControl.PlayerTurn playerColor) {
        this.playerColor = playerColor;
    }

    public GameControl.PlayerTurn getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(GameControl.PlayerTurn playerColor) {
        this.playerColor = playerColor;
    }
}

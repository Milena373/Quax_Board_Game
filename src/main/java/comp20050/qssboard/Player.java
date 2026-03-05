package comp20050.qssboard;

public class Player {
    private GameControl.PlayerTurn playerColor;
    public Player(GameControl.PlayerTurn playerColor) {
        this.playerColor = playerColor;
    }

    GameControl.PlayerTurn getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(GameControl.PlayerTurn playerColor) {
     this.playerColor = playerColor;
    }
}

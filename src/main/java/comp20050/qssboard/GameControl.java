package comp20050.qssboard;

public class GameControl {

    public enum GameMode { HUMAN_VS_HUMAN,HUMAN_VS_BOT}
    public enum PlayerTurn { BLACK, WHITE }

    private static GameMode gameMode;// Human Vs Human or Human Vs Bot
    private static PlayerTurn playerTurn;

    public GameControl() {}

    public static GameMode getGameMode() {
        return gameMode;
    }
    public static void setGameMode(GameMode mode) {
        gameMode = mode;
    }

    public static PlayerTurn getPlayerTurn() { return playerTurn; }
    public static void setPlayerTurn(PlayerTurn playerTurn) { playerTurn = playerTurn; }
}
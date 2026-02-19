package comp20050.qssboard;

public class GameControl {

        public enum GameMode { HUMAN_VS_HUMAN,HUMAN_VS_BOT}

        private static GameMode gameMode;// Human Vs Human or Human Vs Bot

        public GameControl() {}

        public static GameMode getGameMode() {
            return gameMode;
        }
        public static void setGameMode(GameMode mode) {
            gameMode = mode;
        }


}

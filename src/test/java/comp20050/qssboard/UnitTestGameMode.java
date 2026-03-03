package comp20050.qssboard;


//This Test conducts unit test on the feature of selecting what mode you want to play with Human vs Human or
//Human Vs Bot .
//Authored By : Ruha Renu


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UnitTestGameMode {
    @Test
    void setGameMode_onHumanVsHuman(){
        GameControl controller = new GameControl();
        GameControl.setGameMode(GameControl.GameMode.HUMAN_VS_HUMAN);

        assertEquals(GameControl.GameMode.HUMAN_VS_HUMAN, GameControl.getGameMode());
    }
    @Test
    void setGameMode_onHumanVsBot(){
        GameControl controller = new GameControl();
        GameControl.setGameMode(GameControl.GameMode.HUMAN_VS_BOT);

        assertNotEquals(GameControl.GameMode.HUMAN_VS_HUMAN, GameControl.getGameMode());
    }

}

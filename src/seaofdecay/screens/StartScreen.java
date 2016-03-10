package seaofdecay.screens;

import seaofdecay.screens.PlayScreen.WorldType;
import seaofdecay.util.asciipanel.AsciiPanel;

import java.awt.event.KeyEvent;

/** The start screen. Displays a logo and waits for the user to press ENTER. */
public class StartScreen implements Screen {

    public void displayOutput(AsciiPanel terminal) {
        RES_MGR.drawRes(terminal, "SoD_Intro.xp");
    }

    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen(WorldType.VALLEY) : this;
    }
}

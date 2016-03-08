package seaofdecay.screens;

import seaofdecay.util.asciipanel.AsciiPanel;

import java.awt.event.KeyEvent;

/**
 * The lose screen. Is displayed when the character dies.
 * */
public class LoseScreen implements Screen {

    public void displayOutput(AsciiPanel terminal) {
        RES_MGR.drawRes(terminal, "SoD_Lose.xp");
    }

    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new StartScreen() : this;
    }
}

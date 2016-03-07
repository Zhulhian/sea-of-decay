package seaofdecay.screens;

import seaofdecay.util.asciipanel.AsciiPanel;

import java.awt.event.KeyEvent;


public class LoseScreen implements Screen {

    private int width;
    private int height;

    public void displayOutput(AsciiPanel terminal) {
        resMgr.drawRes(terminal, "SoD_Lose.xp");
    }

    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new StartScreen() : this;
    }
}

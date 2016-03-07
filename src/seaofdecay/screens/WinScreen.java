package seaofdecay.screens;

import seaofdecay.util.asciipanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;


public class WinScreen implements Screen {

    public void displayOutput(AsciiPanel terminal) {
        terminal.write("Wow. You won. Congratulations.", 1, 1, Color.orange);
        terminal.writeCenter(" - - Press [ENTER] to restart - - ",
                                20, Color.orange);
    }

    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen(PlayScreen.WorldType.Valley) : this;
    }
}

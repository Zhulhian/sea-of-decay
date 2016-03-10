package seaofdecay.screens;

import seaofdecay.ApplicationMain;
import seaofdecay.screens.PlayScreen.WorldType;
import seaofdecay.util.asciipanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

/** The win screen. Will probably not be reached. Low priority. Just displays a message for now.*/
public class WinScreen implements Screen {

    public void displayOutput(AsciiPanel terminal) {

	terminal.writeCenter("Wow. You won. Congratulations.", 1, Color.orange);
	terminal.writeCenter(" - - Press [ENTER] to restart - - ", ApplicationMain.HEIGHT - 10, Color.orange);

    }

    public Screen respondToUserInput(KeyEvent key) {
	return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen(WorldType.VALLEY) : this;
    }
}

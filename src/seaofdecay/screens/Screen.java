package seaofdecay.screens;

import seaofdecay.util.asciipanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public interface Screen {
    Color bg = new Color(22,22,22);
    ResourceManager resMgr = new ResourceManager();
    void displayOutput(AsciiPanel terminal);

    Screen respondToUserInput(KeyEvent key);
}

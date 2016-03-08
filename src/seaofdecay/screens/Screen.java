package seaofdecay.screens;

import seaofdecay.util.asciipanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Interface for the different screens. Every screen should handle
 * user input, display output, and have access to the resource manager.
 * */
public interface Screen {

	/** Default background color. */
	final Color BGC = new Color(22,22,22);

	/**
	 * Handles resource files located in the res folder.
	 * Can draw directly to a terminal or return the resource.
	 * */
	ResourceManager RES_MGR = new ResourceManager();

	void displayOutput(AsciiPanel terminal);

	Screen respondToUserInput(KeyEvent key);
}

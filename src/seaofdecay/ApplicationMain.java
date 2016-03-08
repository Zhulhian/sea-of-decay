package seaofdecay;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import seaofdecay.screens.Screen;
import seaofdecay.screens.StartScreen;
import seaofdecay.util.asciipanel.AsciiPanel;

/**
 *
 * The main class.
 * Creaters a new asciiPanel terminal and adds the StartScreen to it.
 * Handles the input and graphics through screens.
 *
 * */
public class ApplicationMain extends JFrame implements KeyListener {

	/**
	 * Width in characters. Will be multiplied by character width of font file
	 * to get the end resolution.
	 * */
	public static final int WIDTH = 150;
	/**
	 * Height in characters. Will be multiplied by character width of font file
	 * to get the end resolution.
	 * */
	public static final int HEIGHT = 90;

	private AsciiPanel terminal;
	private Screen screen;

	/**
	 * @param width 	width in characters.
	 * @param height	height in characters.
	 * */
	public ApplicationMain(int width, int height) {
		// super(); IntelliJ says it's uneccesary. I'm going to keep it as a comment for now.
		terminal = new AsciiPanel(width, height);
		add(terminal);
		pack();
		screen = new StartScreen();
		addKeyListener(this);
		repaint();
	}

	public void repaint() {
		terminal.setDefaultBackgroundColor(Screen.BGC);
		// terminal.clear() flushes the buffer and fills the terminal with
		// the default background color.
		terminal.clear();
		screen.displayOutput(terminal);
		super.repaint();
	}

	public void keyPressed(KeyEvent e) {
		screen = screen.respondToUserInput(e);
		repaint();
	}

	// Not used for now, but might be useful in the future.
	public void keyReleased(KeyEvent e) {}

	// Ditto.
	public void keyTyped(KeyEvent e) {}

	public static void main(String[] args){

		// The final resolution will be CharacterWidth * WIDTH x CharacterHeight * HEIGHT.
		// Try to keep it in 16:9 aspect ratio, as all the art for the start and end screens are made in 150 x 90 characters.
		ApplicationMain app = new ApplicationMain(WIDTH, HEIGHT);
		app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		app.setVisible(true);

	}
}

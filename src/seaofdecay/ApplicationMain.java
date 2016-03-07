package seaofdecay;

import javax.swing.*;

import seaofdecay.util.asciipanel.AsciiPanel;

import java.awt.event.KeyListener;

public class ApplicationMain extends JFrame {

	private AsciiPanel terminal;

	public ApplicationMain() {
		super();
		terminal = new AsciiPanel();
		terminal.write("TEST", 1, 1);
		add(terminal);
		pack();
	}

	public static void main(String[] args) {
		ApplicationMain game = new ApplicationMain();
		game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		game.setVisible(true);
	}
}

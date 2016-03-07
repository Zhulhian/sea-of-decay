package seaofdecay;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import seaofdecay.screens.Screen;
import seaofdecay.screens.StartScreen;
import seaofdecay.util.asciipanel.AsciiPanel;


public class ApplicationMain extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;

    public ApplicationMain(int width, int height) {
        super();
        terminal = new AsciiPanel(width, height);
        add(terminal);
        pack();
        screen = new StartScreen();
        addKeyListener(this);
        repaint();
    }

    public void repaint() {
        terminal.setDefaultBackgroundColor(Screen.bg);
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args){

        ApplicationMain app = new ApplicationMain(150, 90);
        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        app.setVisible(true);
    }
}

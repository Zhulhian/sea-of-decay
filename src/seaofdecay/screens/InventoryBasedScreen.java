package seaofdecay.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;

import seaofdecay.Creature;
import seaofdecay.Item;
import seaofdecay.util.asciipanel.AsciiPanel;


/**
 * A general screen for Inventory based screens, such as drink, drop, etc.
 */
public abstract class InventoryBasedScreen implements Screen {

	/** Inventory Y position */
	public static final int INV_Y = 15;
	/** Inventory width */
	public static final int INV_WIDTH = 20;
	/** Query width, asks what item you want to do something with. */
	public static final int QUERY_WIDTH = 30;
	protected Creature player;
	private String letters;

	protected abstract String getVerb();
	/** Isn't used right now, except for DropScreen where it returns true, but it will be used for future
	 * extensions. */
	protected abstract boolean isAcceptable(Item item);
	protected abstract Screen use(Item item);

	protected InventoryBasedScreen(Creature player) {
		this.player = player;
		this.letters = "abcdefghijklmnopqrstuvwxyz";
	}

	public void displayOutput(AsciiPanel terminal) {
		Iterable<String> lines = getList();

		int y = INV_Y;
		int x = 2;

		terminal.clear(' ', x, y, INV_WIDTH, Creature.INV_SIZE);

		for (String line : lines) {
			terminal.write(line, x, y);
			y++;

		}


		terminal.clear(' ', x - 1, INV_Y - 2, QUERY_WIDTH, 1, AsciiPanel.brightBlack, AsciiPanel.brightBlack);
		terminal.write("What would you like to " + getVerb() + "?", x, INV_Y - 2, AsciiPanel.brightYellow, AsciiPanel.brightBlack);

		terminal.repaint();
	}

	private Iterable<String> getList() {
		Collection<String> lines = new ArrayList<>();
		Item[] inventory = player.getInventory().getItems();

		for (int i = 0; i < inventory.length; i++) {
			Item item = inventory[i];

			if (item == null || !isAcceptable(item))
				continue;

			String line = letters.charAt(i) + " - " + item.getGlyph() + " " + item.getName();

			lines.add(line);
		}

		return lines;
	}

	public Screen respondToUserInput(KeyEvent key) {
		char c = key.getKeyChar();

		Item[] items = player.getInventory().getItems();

		if (letters.indexOf(c) > -1
				&& items.length > letters.indexOf(c)
				&& items[letters.indexOf(c)] != null
				&& isAcceptable(items[letters.indexOf(c)]))
			return use(items[letters.indexOf(c)]);
		else if (key.getKeyCode() == KeyEvent.VK_ESCAPE)
			return null;
		else
			return this;
	}

}

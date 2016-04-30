package seaofdecay.screens;

import seaofdecay.*;
import seaofdecay.Point;
import seaofdecay.util.asciipanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Main screen. Handles the actual gameplay and displays the world.
 * */
public class PlayScreen implements Screen {

	private Screen subscreen;

	/** Width of the Sea of Decay map. */
	public static final int SOD_WIDTH = 170;
	/** Height of the Sea of Decay map. */
	public static final int SOD_HEIGHT = 160;
	/** Width of the Valley map. */
	public static final int VALLEY_WIDTH = 200;
	/** Height of the Valley map. */
	public static final int VALLEY_HEIGHT = 130;
	/** Width of the Abyss map. */
	public static final int ABYSS_WIDTH = 200;
	/** Height of the Abyss map. */
	public static final int ABYSS_HEIGHT = 200;

	private static final int GUI_HEIGHT = 5;
	private static final int GUI_WIDTH = 20;

	/** List to hold all the status messages*/
	private List<String> messages;

	/** Field of View */
	private FieldOfView fov;

	private World world;
	private Creature player;

	private WorldType currentWorld;

	private int screenWidth;
	private int screenHeight;

	public PlayScreen(WorldType worldType) {
		this.screenWidth = ApplicationMain.WIDTH;
		this.screenHeight = ApplicationMain.HEIGHT;
		this.messages = new ArrayList<>();
		this.currentWorld = worldType;
		createWorld(worldType);

		this.fov = new FieldOfView(world);

		EntityFactory entityFactory = new EntityFactory(world);

		createCreatures(entityFactory);
		createItems(entityFactory);
	}

	private void createCreatures(EntityFactory entityFactory) {
		player = entityFactory.newPlayer(messages, fov);

		if (currentWorld == WorldType.SEA_OF_DECAY) {
			for (int i = 0; i < 20; i++) {
				entityFactory.newFungus();
			}
			for (int i = 0; i < 40; i++) {
				entityFactory.newMoth();
			}
		}
	}

	private void createItems(EntityFactory entityFactory) {

		if (currentWorld == WorldType.SEA_OF_DECAY) {
			for (int i = 0; i < world.width() * world.height() / 40; i++) {
				if (Math.random() < 0.5)
					entityFactory.newMushroom();
				else
					entityFactory.newSpore();
			}
			entityFactory.newVictoryItem();
		}
	}

	private void createWorld(WorldType worldType) {

		switch (worldType) {
			case SEA_OF_DECAY:
				world = new WorldBuilder(SOD_WIDTH, SOD_HEIGHT)
						.makeSOD()
						.build();
				break;
			case VALLEY:
				world = new WorldBuilder(VALLEY_WIDTH, VALLEY_HEIGHT)
						.makeValley()
						.build();
				break;
			case ABYSS:
				world = new WorldBuilder(ABYSS_WIDTH, ABYSS_HEIGHT)
					.makeAbyss()
					.build();
				break;
			default:
				// If the worldType is not found, create a 25 x 25 empty world.
				world = new WorldBuilder(ABYSS_WIDTH, ABYSS_HEIGHT)
						.build();
				break;
		}
	}

	// Camera view functions (for scrolling map)
	public int getScrollX() {
		return Math.max(0, Math.min(player.x - screenWidth / 2, world.width() - screenWidth));
	}
	public int getScrollY() {
		return Math.max(0, Math.min(player.y - screenHeight / 2, world.height() - screenHeight));
	}

//    private void player.moveBy(int mx, int my) {
//        centerX = Math.max(0, Math.min(centerX + mx, world.width() - 1));
//        centerY = Math.max(0, Math.min(centerY + my, world.height() - 1));
//    }

	private void displayTiles(AsciiPanel terminal, int left, int top) {
		fov.update(player.x, player.y, player.getVisionRadius());

		terminal.setDefaultBackgroundColor(Screen.BGC);
		for (int x = 0; x < screenWidth; x++) {
			for (int y = 0; y < screenHeight; y++) {

				int wx = x + left;
				int wy = y + top;

				if (player.canSee(wx, wy))
						terminal.write(world.glyph(wx, wy), x, y, world.fgColor(wx, wy), world.bgColor(wx, wy));
				else
					terminal.write(fov.getTile(wx, wy).glyph(), x, y, Color.darkGray);
			}
		}
	}

	private void displayGUI(AsciiPanel terminal) {
		terminal.setDefaultBackgroundColor(new Color(14, 31, 49));
		for (int w = 2; w < GUI_WIDTH; w++) {
			for (int h = 2; h < GUI_HEIGHT; h++) {
				terminal.write(" ", w, h);
			}
		}
	}

	private void displayMessages(AsciiPanel terminal, List<String> messages) {
		int top = 3;

		for (int x = 0; x < 60; x++) {
			for (int y = 0; y < 7; y++) {
				terminal.write(' ', 24 + x, 2 + y);
			}
		}

		for (int i = 0; i < messages.size(); i++) {
			terminal.write(messages.get(i), 25, top + i);
		}
		if (messages.size() > 4) {
			messages.clear();
		}
	}

	public void displayOutput(AsciiPanel terminal) {

		int left = getScrollX();
		int top = getScrollY();

		displayTiles(terminal, left, top);

		displayGUI(terminal);
		displayMessages(terminal, messages);
		String stats = String.format("%3d/%3d hp", player.getHp(), player.getMaxHp());
		terminal.write(stats, 3, 3, new Color(222, 136, 135));

		if (subscreen != null)
			subscreen.displayOutput(terminal);
	}

	public Screen respondToUserInput(KeyEvent key) {
		if (subscreen != null) {
			subscreen = subscreen.respondToUserInput(key);
		} else {
			switch (key.getKeyCode()) {
				// Cheats
				case KeyEvent.VK_ESCAPE:
					return new LoseScreen();
				case KeyEvent.VK_ENTER:
					return new WinScreen();

				case KeyEvent.VK_T:
					return new PlayScreen(WorldType.SEA_OF_DECAY);

				case KeyEvent.VK_V:
					return new PlayScreen(WorldType.VALLEY);

				case KeyEvent.VK_A:
					return new PlayScreen(WorldType.ABYSS);

				//      -  -  -    Movement    -  -  -       //

				// Left
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_H:
					player.moveBy(-1, 0);
					break;

				// Right
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_L:
					player.moveBy(1, 0);
					break;

				// Up
				case KeyEvent.VK_UP:
				case KeyEvent.VK_K:
					player.moveBy(0, -1);
					break;

				// Down
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_J:
					player.moveBy(0, 1);
					break;

				// Diagonals
				case KeyEvent.VK_Y:
					player.moveBy(-1, -1);
					break;
				case KeyEvent.VK_U:
					player.moveBy(1, -1);
					break;
				case KeyEvent.VK_B:
					player.moveBy(-1, 1);
					break;
				case KeyEvent.VK_N:
					player.moveBy(1, 1);
					break;

				//    -   -   -   ACTION   -   -   -    //
				case KeyEvent.VK_C:
					Point playerPos = new Point(player.x, player.y);
					for (Point p : playerPos.neighbors8()) {
						if (world.getTile(p.x, p.y) == Tile.VALLEY_DOOR_OPEN) {
							world.dig(p.x, p.y);
							world.setTile(p.x, p.y, Tile.VALLEY_DOOR_CLOSED);
							player.doAction("close the door");
							break;
						}
					}
					break;

				case KeyEvent.VK_D: subscreen = new DropScreen(player); break;

				case KeyEvent.VK_G:
				case KeyEvent.VK_COMMA:
					player.pickup();
					break;


			}
		}

		if (world.getTile(player.x, player.y) == Tile.VALLEY_PORTAL) {
			return new PlayScreen(WorldType.SEA_OF_DECAY);
		}

		if (world.getTile(player.x, player.y) == Tile.SOD_PORTAL) {
			for (Item item : player.getInventory().getItems()) {
				if (item != null && item.getName().equals("Lantern of The Ohm"))
					return new WinScreen();
			}
			return new PlayScreen(WorldType.ABYSS);
		}

		if (subscreen == null) {
			world.update();
		}

		if (player.getHp() < 1) {
			return new LoseScreen();
		}

		return this;
	}
}

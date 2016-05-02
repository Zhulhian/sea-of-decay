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

	/** Number of fungi */
	public static final int NO_OF_FUNGI = 20;
	/** Number of moths */
	public static final int NO_OF_MOTHS = 40;
	/** How many mushrooms there are for every spore */
	public static final double ITEM_RATIO = 0.5;
	/** Number of items in the SoD world.  */
	public static final int ITEM_AMOUNT = 600;
	/** Width of message log */
	public static final int MSG_LOG_WIDTH = 60;
	/** Height of message log */
	public static final int MSG_LOG_HEIGHT = 7;
	/** X pos of message log */
	public static final int MSG_LOG_X = 24;
	/** Y pos of message log */
	public static final int MSG_LOG_Y = 2;
	private Screen subscreen = null;

	/** Width of the Sea of Decay map. */
	public static final int SOD_WIDTH = 170;
	/** Height of the Sea of Decay map. */
	public static final int SOD_HEIGHT = 160;
	/** Width of the Valley map. */
	public static final int VALLEY_WIDTH = 200;
	/** Height of the Valley map. */
	public static final int VALLEY_HEIGHT = 130;
	/** Width of the Abyss map. */
	public static final int ABYSS_WIDTH = 300;
	/** Height of the Abyss map. */
	public static final int ABYSS_HEIGHT = 300;

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
			for (int i = 0; i < NO_OF_FUNGI; i++) {
				entityFactory.newFungus();
			}
			for (int i = 0; i < NO_OF_MOTHS; i++) {
				entityFactory.newMoth();
			}
		}
	}

	private void createItems(EntityFactory entityFactory) {

		if (currentWorld == WorldType.SEA_OF_DECAY) {
			for (int i = 0; i < ITEM_AMOUNT; i++) {
				if (Math.random() < ITEM_RATIO)
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
		String stats = String.format("%3d/%3d hp", player.getHp(), player.getMaxHp());
		terminal.write(stats, 3, 3, new Color(222, 136, 135));
	}

	private void displayMessages(AsciiPanel terminal, List<String> messages) {
		for (int x = 0; x < MSG_LOG_WIDTH; x++) {
			for (int y = 0; y < MSG_LOG_HEIGHT; y++) {
				terminal.write(' ', MSG_LOG_X + x, MSG_LOG_Y + y);
			}
		}

		for (int i = 0; i < messages.size(); i++) {
			terminal.write(messages.get(i), MSG_LOG_X + 1, MSG_LOG_Y + 1 + i);
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

		if (subscreen != null)
			subscreen.displayOutput(terminal);
	}

	private Screen enterPortal() {
		Tile playerTile = world.getTile(player.x, player.y);
		if (playerTile == Tile.VALLEY_PORTAL) {
			return new PlayScreen(WorldType.SEA_OF_DECAY);
		}
		else if (playerTile == Tile.SOD_PORTAL) {
			for (Item item : player.getInventory().getItems()) {
				if (item != null && item.getName().equals("Lantern of The Ohm"))
					return new WinScreen();
			}
			return new PlayScreen(WorldType.ABYSS);
		}
		else {
			return this;
		}
	}

	/** Yes it is long... but in roguelike games there are a lot of keys to be used, many of them have lots more than this.
	 * Not sure how to circumvent having a lot of keys. I don't think this is that unreasonably long. */
	public Screen respondToUserInput(KeyEvent key) {
		if (subscreen != null) {
			subscreen = subscreen.respondToUserInput(key);
		} else {
			switch (key.getKeyCode()) {

				/** I've left the cheats as comments in case you want to use them for easy debugging. */
				//				// Cheats
				//				case KeyEvent.VK_ESCAPE:
				//					return new LoseScreen();
				//				case KeyEvent.VK_ENTER:
				//					return new WinScreen();
				//
				//				case KeyEvent.VK_T:
				//					return new PlayScreen(WorldType.SEA_OF_DECAY);
				//
				//				case KeyEvent.VK_V:
				//					return new PlayScreen(WorldType.VALLEY);
				//
				//				case KeyEvent.VK_A:
				//					return new PlayScreen(WorldType.ABYSS);


				//      -  -  -    Movement    -  -  -       //

				// Left

				case KeyEvent.VK_H:
					player.moveBy(-1, 0);
					break;

				// Right

				case KeyEvent.VK_L:
					player.moveBy(1, 0);
					break;

				// Up

				case KeyEvent.VK_K:
					player.moveBy(0, -1);
					break;

				// Down

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
							world.closeDoor(p.x, p.y);
							player.doAction("close the door");
							break;
						}
					}
					break;

				/** Drop item. */
				case KeyEvent.VK_D: subscreen = new DropScreen(player); break;

				/** Pick up item. */
				case KeyEvent.VK_G:
					player.pickup();
					break;


			}
		}

		if (world.getTile(player.x, player.y) == Tile.VALLEY_PORTAL ||
				world.getTile(player.x, player.y) == Tile.SOD_PORTAL) {
			return enterPortal();
		}

		/** When we have a subscreen, we don't update the world. */
		if (subscreen == null) {
			world.update();
		}

		if (player.getHp() < 1) {
			return new LoseScreen();
		}

		return this;
	}
}


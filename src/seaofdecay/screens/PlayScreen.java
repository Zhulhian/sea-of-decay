package seaofdecay.screens;

import seaofdecay.*;
import seaofdecay.util.asciipanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Main screen. Handles the actual gameplay and displays the world.
 * */
public class PlayScreen implements Screen {

	enum WorldType {
		SEA_OF_DECAY, VALLEY
	}

	private World world;
	private Creature player;

	private WorldType currentWorld;

	private int screenWidth;
	private int screenHeight;

	public PlayScreen(WorldType worldType) {
		this.screenWidth = ApplicationMain.WIDTH;
		this.screenHeight = ApplicationMain.HEIGHT;
		this.currentWorld = worldType;
		createWorld(worldType);

		CreatureFactory creatureFactory = new CreatureFactory(world);
		createCreatures(creatureFactory);
	}

	private void createCreatures(CreatureFactory creatureFactory) {
		player = creatureFactory.newPlayer();

		if (currentWorld == WorldType.SEA_OF_DECAY) {
			for (int i = 0; i < 100; i++) {
				creatureFactory.newFungus();
			}
		}
	}

	private void createWorld(WorldType worldType) {

		switch (worldType) {
			case SEA_OF_DECAY:
				world = new WorldBuilder(200, 200)
						.makeCaves()
						.build();
				break;
			case VALLEY:
				world = new WorldBuilder(200, 130)
						.makeValley()
						.build();
				break;
			default:
				// If the worldType is not found, create a 25 x 25 empty world.
				world = new WorldBuilder(25, 25)
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
		terminal.setDefaultBackgroundColor(Screen.BGC);
		for (int x = 0; x < screenWidth; x++) {
			for (int y = 0; y < screenHeight; y++) {

				int wx = x + left;
				int wy = y + top;

				terminal.write(world.glyph(wx, wy), x, y, world.fgColor(wx, wy), world.bgColor(wx, wy));

			}
		}

		for (Creature c : world.getCreatures()) {
			if ((c.x >= left && c.x < left + screenWidth) &&
					(c.y >= top && c.y < top + screenHeight)) {
				terminal.write(c.getGlyph(), c.x - left, c.y - top, c.getColor(), world.bgColor(c.x , c.y));
			}
		}
	}

	public void displayOutput(AsciiPanel terminal) {

		int left = getScrollX();
		int top = getScrollY();

		displayTiles(terminal, left, top);
		terminal.writeCenter(" - - press [ESC] to lose or [ENTER] to win - -", 80,
				new Color(59, 164, 164), new Color(222, 11, 11));
	}


	public Screen respondToUserInput(KeyEvent key) {
		switch (key.getKeyCode()) {
			// Cheats
			case KeyEvent.VK_ESCAPE: return new LoseScreen();
			case KeyEvent.VK_ENTER: return new WinScreen();
			case KeyEvent.VK_P:
				if (world.tile(player.x, player.y) == Tile.VALLEY_PORTAL)
					return new PlayScreen(WorldType.SEA_OF_DECAY);
				break;

			case KeyEvent.VK_T:
				return new PlayScreen(WorldType.SEA_OF_DECAY);

			//      -  -  -    Movement    -  -  -       //

			// Left
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_H: player.moveBy(-1,  0); break;

			// Right
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_L: player.moveBy(1 ,  0); break;

			// Up
			case KeyEvent.VK_UP:
			case KeyEvent.VK_K: player.moveBy(0 , -1); break;

			// Down
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_J: player.moveBy(0 ,  1); break;

			// Diagonals
			case KeyEvent.VK_Y: player.moveBy(-1, -1);   break;
			case KeyEvent.VK_U: player.moveBy(1 , -1);   break;
			case KeyEvent.VK_B: player.moveBy(-1,  1);   break;
			case KeyEvent.VK_N: player.moveBy(1 ,  1);   break;

		}

		world.update();

		return this;
	}
}

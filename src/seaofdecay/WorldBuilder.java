package seaofdecay;

import seaofdecay.screens.Screen;
import seaofdecay.util.xpreader.XPChar;
import seaofdecay.util.xpreader.XPFile;

/** A class for creating worlds. Holds functions for generating
 * the different world types. */
public class WorldBuilder {
	/** The occurance of wall tiles during the */
	public static final double SOD_WALL_GROUND_RATIO = 0.5;
	public static final double ABYSS_WALL_GROUND_RATIO = 0.35;
	private int width;
	private int height;
	private Tile[][] tiles;

	public WorldBuilder(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];
	}

	public World build() {
		return new World(tiles);
	}

	private WorldBuilder randomizeTiles() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = Math.random() < SOD_WALL_GROUND_RATIO ? Tile.SOD_GROUND : Tile.SOD_WALL;
			}
		}
		return this;
	}

	private WorldBuilder abyssChaos() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = Math.random() < ABYSS_WALL_GROUND_RATIO ? Tile.ABYSS_WALL : Tile.ABYSS_GROUND;
			}
		}
		return this;
	}

	public WorldBuilder makeAbyss() {
		return abyssChaos();
	}

	private WorldBuilder smooth(int times) {

		Tile[][] smoothedTiles = new Tile[width][height];
		for (int time = 0; time < times; time++) {

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int floorTiles = 0;
					int wallTiles = 0;

					for (int ox = -1; ox < 2; ox++) {
						for (int oy = -1; oy < 2; oy++) {

							if (x + ox < 0 || x + ox >= width
									|| y + oy < 0 || y + oy >= height) {
								continue;
							}

							if (tiles[x + ox][y + oy] == Tile.SOD_GROUND)
								floorTiles++;
							else
								wallTiles++;
						}
					}
					smoothedTiles[x][y] = floorTiles >= wallTiles ? Tile.SOD_GROUND : Tile.SOD_WALL;
				}
			}
			tiles = smoothedTiles;
		}
		return this;
	}

	private WorldBuilder makeVictoryPortal(int n) {
		int x;
		int y;

		for (int i = 0; i < n; i++) {
			do {
				x = (int) (Math.random() * width);
				y = (int) (Math.random() * height);
			} while (tiles[x][y] != Tile.SOD_GROUND);

			tiles[x][y] = Tile.SOD_PORTAL;
		}
		return this;
	}

	public WorldBuilder makeValley() {
		XPFile valley = Screen.RES_MGR.getRes("SoD_Valley.xp");
		for (int x = 0; x < valley.layer(0).width; x++) {
			for (int y = 0; y < valley.layer(0).height; y++) {
				XPChar tile = valley.layer(0).data[x][y];
				tiles[x][y] = Tile.getTile(tile.code, WorldType.VALLEY);
			}
		}
		return this;
	}

	public WorldBuilder makeSOD() {
		return randomizeTiles().smooth(5).makeVictoryPortal(10);
	}
}

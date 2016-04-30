package seaofdecay;

import seaofdecay.screens.Screen;
import seaofdecay.util.xpreader.XPChar;
import seaofdecay.util.xpreader.XPFile;

/** A class for creating worlds. Holds functions for generating
 * the different world types. */
public class WorldBuilder {
	public static final double SOD_WALL_OCCURANCE = 0.5;
	public static final double ABYSS_WALL_OCCURRANCE = 0.35;
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
				// I don't think this constant is very magical. It is clear what
				// the purpose of this is.
				tiles[x][y] = Math.random() < SOD_WALL_OCCURANCE ? Tile.SOD_GROUND : Tile.SOD_WALL;
			}
		}
		return this;
	}

	private WorldBuilder abyssChaos() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = Math.random() < ABYSS_WALL_OCCURRANCE ? Tile.ABYSS_WALL : Tile.ABYSS_GROUND;
				System.out.println(tiles[x][y].isGround());
			}
		}
		return this;
	}

	public WorldBuilder makeAbyss() {
		return abyssChaos();
	}

	private WorldBuilder smooth(int times, WorldType worldType) {

		// So many nested for-loops... Good thing it is only run once.

		Tile wallTile;
		Tile groundTile;

		switch (worldType) {
			case SEA_OF_DECAY:
				wallTile = Tile.SOD_WALL;
				groundTile = Tile.SOD_GROUND;
				break;
			case ABYSS:
				wallTile = Tile.ABYSS_WALL;
				groundTile = Tile.ABYSS_GROUND;
				break;
			default:
				wallTile = Tile.BOUNDS;
				groundTile = Tile.VALLEY_SHADEGRASS;
				break;
		}


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

							if (tiles[x + ox][y + oy] == groundTile)
								floorTiles++;
							else
								wallTiles++;
						}
					}
					smoothedTiles[x][y] = floorTiles >= wallTiles ? groundTile : wallTile;
				}
			}
			tiles = smoothedTiles;
		}
		return this;
	}

	private WorldBuilder makeVictoryPortal() {
		int x = -1;
		int y = -1;

		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		} while (tiles[x][y] != Tile.SOD_GROUND);

		tiles[x][y] = Tile.SOD_PORTAL;
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
		return randomizeTiles().smooth(8, WorldType.SEA_OF_DECAY).makeVictoryPortal();
	}
}

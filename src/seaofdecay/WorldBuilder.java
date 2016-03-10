package seaofdecay;

import seaofdecay.screens.Screen;
import seaofdecay.util.xpreader.XPChar;
import seaofdecay.util.xpreader.XPFile;

/** A class for creating worlds. Holds functions for generating
 * the different world types. */
public class WorldBuilder {
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
				tiles[x][y] = Math.random() < 0.5 ? Tile.SOD_GROUND : Tile.SOD_WALL;
			}
		}
		return this;
	}

	private WorldBuilder smooth(int times) {

		// So many nested for-loops... *cries a little bit*

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

	public WorldBuilder makeValley() {
		XPFile valley = Screen.RES_MGR.getRes("SoD_Valley.xp");
		for (int x = 0; x < valley.layer(0).width; x++) {
			for (int y = 0; y < valley.layer(0).height; y++) {
				XPChar tile = valley.layer(0).data[x][y];
				switch (tile.code) {
					case '#':
						tiles[x][y] = Tile.VALLEY_WALL;
						break;
					case (char)247:
						tiles[x][y] = Tile.VALLEY_GRASS;
						break;
					case (char)234:
						tiles[x][y] = Tile.VALLEY_PORTAL;
						break;
					case (char)240:
						tiles[x][y] = Tile.VALLEY_FLOORBOARD;
						break;
					default: tiles[x][y] = Tile.BOUNDS; break;
				}
			}
		}
		return this;
	}

	public WorldBuilder makeCaves() {
		return randomizeTiles().smooth(8);
	}
}

package seaofdecay;

import seaofdecay.screens.Screen;
import seaofdecay.util.xpreader.XPFile;

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
				tiles[x][y] = Math.random() < 0.5 ? Tile.FUNGI_FLOOR : Tile.FUNGI_WALL;
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

							if (tiles[x + ox][y + oy] == Tile.FUNGI_FLOOR)
								floorTiles++;
							else
								wallTiles++;
						}
					}
					smoothedTiles[x][y] = floorTiles >= wallTiles ? Tile.FUNGI_FLOOR : Tile.FUNGI_WALL;
				}
			}
			tiles = smoothedTiles;
		}
		return this;
	}

	public WorldBuilder makeValley() {
		XPFile valley = Screen.resMgr.getRes("SoD_Valley.xp");
		for (int x = 0; x < valley.layer(0).width; x++) {
			for (int y = 0; y < valley.layer(0).height; y++) {
				switch (valley.layer(0).data[x][y].code) {
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

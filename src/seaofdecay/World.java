package seaofdecay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/** World class. Holds functions and variables for a world.
 * Has a list of the creatures, the tiles, and functions that
 * modify the world - such as dig. */
public class World {

	private Tile[][] tiles;

	private int width;
	public int width() { return width; }

	private int height;
	public int height() { return height; }

	private List<Creature> creatures;
	public Iterable<Creature> getCreatures() {
		return creatures;
	}

	public World(Tile[][] tiles) {
		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;

		creatures = new ArrayList<>();
	}

	public void update() {
		Iterable<Creature> toUpdate = new ArrayList<>(creatures);
		for (Creature creature : toUpdate) {
			creature.update();
		}
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return Tile.BOUNDS;
		else
			return tiles[x][y];
	}

	public void setTile(int x, int y, Tile tile) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new IllegalArgumentException("Can't set a tile outside world limits.");
		else
			tiles[x][y] = tile;
	}

	public void dig(int x, int y) {
		if (getTile(x, y).isDiggable())
			tiles[x][y] = Tile.SOD_GROUND;
	}

	public Creature creatureAt(int x, int y) {
		for (Creature c : creatures) {
			if (c.x == x && c.y == y)
				return c;
		}
		return null;
	}

	public void remove(Creature target) {
		creatures.remove(target);
	}

	public void addAtEmptyLocation(Creature creature) {
		int x;
		int y;

		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		} while (!getTile(x,y).isGround() || creatureAt(x, y) != null);

		creature.x = x;
		creature.y = y;
		creatures.add(creature);
	}

	public char glyph(int x, int y) {
		return getTile(x,y).glyph();
	}

	public Color fgColor(int x, int y) {
		return getTile(x, y).fgColor();
	}

	public Color bgColor(int x, int y) {
		return getTile(x, y).bgColor();
	}

}
package seaofdecay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** World class. Holds functions and variables for a world.
 * Has a list of the creatures, the tiles, and functions that
 * modify the world - such as dig. */
public class World {

	private Tile[][] tiles;

	private Item[][] items;

	private int width;
	public int width() { return width; }

	private int height;
	public int height() { return height; }

	private List<Creature> creatures;
	/** Not used now, but might be useful for future. */
	public Iterable<Creature> getCreatures() {
		return creatures;
	}

	public World(Tile[][] tiles) {
		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;

		this.items = new Item[width][height];

		creatures = new ArrayList<>();
	}

	public void update() {
		Iterable<Creature> toUpdate = new ArrayList<>(creatures);
		for (Creature creature : toUpdate) {
			creature.update();
		}

	}

	public Item itemAt(int x, int y) {
		return items[x][y];
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

	public void remove(int x, int y) {
		items[x][y] = null;
	}

	public void addAtEmptyLocation(Creature creature) {
		int x;
		int y;

		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		} while (creatureAt(x, y) != null ||
				!getTile(x,y).isGround());

		creature.x = x;
		creature.y = y;
		creatures.add(creature);

	}

	public void addAtEmptyLocation(Item item) {
		int x;
		int y;

		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		} while (!getTile(x, y).isGround() || itemAt(x, y) != null);

		items[x][y] = item;

	}

	public boolean addAtEmptyLocation(Item item, int x, int y) {

		List<Point> points = new ArrayList<>();
		Collection<Point> checked = new ArrayList<>();

		points.add(new Point(x,y));

		while (!points.isEmpty()) {

			Point p = points.remove(0);
			checked.add(p);

			if (!getTile(p.x, p.y).isGround())
				continue;

			if (items[p.x][p.y] == null) {
				items[p.x][p.y] = item;
				Creature c = this.creatureAt(p.x, p.y);
				if (c != null)
					c.notify("A %s lands at your feet.", item.getName());
				return true;
			} else {
				List<Point> neighbors = p.neighbors8();
				neighbors.removeAll(checked);
				points.addAll(neighbors);
			}
		}
		return false;
	}

	public char glyph(int x, int y) {
		Creature creature = creatureAt(x, y);
		Item item = itemAt(x, y);

		if (creature != null)
			return creature.getGlyph();

		if (item != null)
			return item.getGlyph();

		return getTile(x, y).glyph();
	}

	public Color fgColor(int x, int y) {
		Creature creature = creatureAt(x, y);
		Item item = itemAt(x, y);

		if (creature != null)
			return creature.getColor();

		if (item != null)
			return item.getColor();

		return getTile(x,y).fgColor();
	}

	public Color bgColor(int x, int y) {
		return getTile(x, y).bgColor();
	}

}
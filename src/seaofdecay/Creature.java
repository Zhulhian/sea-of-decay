package seaofdecay;

import java.awt.Color;

/**
 * General class for a creature. Holds X and Y coordinates, has functions
 * that every creature uses/can use such as move, dig, attack and canEnter.
 * */
public class Creature {
	private World world;

	/** The X coordinate of the creature. */
	public int x;
	/** The Y coordinate of the creature. */
	public int y;

	/** The symbol/glyph of the creature. */
	private char glyph;
	public char getGlyph() { return glyph; }

	/** The color of the creatures glyph.*/
	private Color color;
	public Color getColor() { return color; }

	private CreatureAi ai = null;
	// IntelliJ reports it as suspicious, but it obviously isn't.
	public void setCreatureAi(CreatureAi ai) { this.ai = ai; }

	public Creature(World world, char glyph, Color color) {
		this.world = world;
		this.glyph = glyph;
		this.color = color;
	}

	/** Digs out the tile at the given x and y coordinates.
	 * @param wx World X coordinate
	 * @param wy World Y coordinate */
	public void dig(int wx, int wy) {
		world.dig(wx, wy);
	}

	/** Relative move function. If there is a creature at current x + mx and current y + my,
	 * attack it. Else, check what happens on entering the given tile.
	 * @param mx relative x coordinate
	 * @param my relative y coordinate */
	public void moveBy(int mx, int my) {
		Creature other = world.creatureAt(x+mx, y+my);

		if (other == null)
			ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
		else
			attack(other);
	}

	public void update() {
		ai.onUpdate();
	}

	/** Currently just removes the other creature. That is to say instakill!*/
	public void attack(Creature other) {
		world.remove(other);
	}

	/** You can enter a tile if there is not creature there and the tile is a ground tile.
	 * @param wx World X coordinate
	 * @param wy World Y coordinate */
	public boolean canEnter(int wx, int wy) {
		return world.tile(wx, wy).isGround() &&
				world.creatureAt(wx, wy) == null;
	}

}

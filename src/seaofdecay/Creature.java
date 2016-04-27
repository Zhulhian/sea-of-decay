package seaofdecay;

import java.awt.Color;

/**
 * General class for a creature. Holds X and Y coordinates, has functions
 * that every creature uses/can use such as move, dig, attack and canEnter.
 * */
public class Creature {
	private World world;

	/** The maximum amount of health. */
	private int maxHp;
	public int getMaxHp() {return maxHp;}

	/** Current hp. */
	private int hp;
	public int getHp() {return hp;}

	/** The attack value, used when attacking other creatures.*/
	private int attackValue;
	public int getAttackValue() {return attackValue;}

	/** The defense value. */
	private int defenseValue;
	public int getDefenseValue() {return defenseValue;}

	private int visionRadius;
	public int getVisionRadius() { return visionRadius;}

	/** The X coordinate of the creature. */
	public int x;
	/** The Y coordinate of the creature. */
	public int y;

	/** Name of the creature. */
	private String name;
	public String getName() { return name; }

	/** The symbol/glyph of the creature. */
	private char glyph;
	public char getGlyph() { return glyph; }

	/** The color of the creatures glyph.*/
	private Color color;
	public Color getColor() { return color; }

	private CreatureAi ai = null;
	// IntelliJ reports it as suspicious, but it obviously isn't.
	public void setCreatureAi(CreatureAi ai) { this.ai = ai; }

	public Creature(World world, String name, char glyph, Color color, int maxHp, int attackValue, int defenseValue) {
		this.world = world;
		this.name = name;
		this.glyph = glyph;
		this.color = color;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.attackValue = attackValue;
		this.defenseValue = defenseValue;
		this.visionRadius = 30;
	}

	public boolean canSee(int wx, int wy) {
		return ai.canSee(wx, wy);
	}

	public Tile getTile(int wx, int wy) {
		return world.getTile(wx, wy);
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
			ai.onEnter(x + mx, y + my, world.getTile(x + mx, y + my));
		else
			attack(other);
	}

	public void interact(int x, int y, Tile tile) {

		/** Will expand with more interactable tiles. */
		switch (tile) {
			/** When interacting with a closed door, remove the door and replace it with
			 * an open door tile - which you can walk through. */
			case VALLEY_DOOR_CLOSED:
				world.dig(x, y);
				world.setTile(x, y, Tile.VALLEY_DOOR_OPEN);
				doAction("open the door");
				break;
			case VALLEY_DOOR_OPEN:
				world.dig(x, y);
				world.setTile(x, y, Tile.VALLEY_DOOR_CLOSED);
				doAction("close the door");
				break;
		}

	}

	/** Add ability to format strings! Good for displaying variable values,
	 * like damage done, HP recovered, etc. etc. */
	public void notify(String message, Object ... params) {
		ai.onNotify(String.format(message, params));
	}
	/** A function for notifying nearby creatures when a creature does something.
	 * As in, if a creature breaks something, it will show up in the message log of
	 * other nearby creatures. */
	public void doAction(String message, Object ... params) {
		/** The radius of which nearby creatures will notice the action
		 * happening. */
		int radius = 15;
		for (int rx = -radius; rx < radius + 1; rx++) {
			for (int ry = -radius; ry < radius + 1; ry++) {
				if (rx * rx + ry * ry > radius * radius)
					continue;

				Creature other = world.creatureAt(x + rx, y + ry);

				if (other == null)
					continue;

				if (other == this)
					other.notify("You " + message + ".", params);
				else if (other.canSee(x, y))
					other.notify(String.format("The %s %s.", name, makeSecondPerson(message)), params);
			}
		}
	}

	/** Used for proper grammar in the status messages. */
	private String makeSecondPerson(String message) {
		int space = message.indexOf(" ");

		if (space == -1) {
			return message+"s";
		} else {
			return message.substring(0, space) + "s" + message.substring(space);
		}
	}

	public void update() {
		ai.onUpdate();
	}

	/** Attacks another creature. Has an element of randomness. Uses modifyHp
	 * function to decrease HP. Might seem like a very simple attack system, but
	 * it's simple and it works well enough. I might extend it as a stretch goal.*/
	public void attack(Creature other) {
		int amount = Math.max(0, attackValue - other.getDefenseValue());

		amount = (int)(Math.random() * amount) + 1;
		//notify("You attack the %s for %d damage.", other.name, amount);
		//other.notify("The %s attacks you for %d damage.", name, amount);
		doAction("attack the %s for %d damage", other.name, amount);

		other.modifyHp(-amount);
	}

	/** Convenient function as it can both be used for healing and dealing damage.*/
	public void modifyHp(int amount) {
		hp += amount;

		if (hp < 1) {
			doAction("die");
			world.remove(this);
		}
	}

	/** You can enter a tile if there is not creature there and the tile is a ground tile.
	 * @param wx World X coordinate
	 * @param wy World Y coordinate */
	public boolean canEnter(int wx, int wy) {
		return world.getTile(wx, wy).isGround() &&
				world.creatureAt(wx, wy) == null;
	}

}

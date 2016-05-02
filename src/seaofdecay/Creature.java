package seaofdecay;

import java.awt.Color;
import java.util.Objects;

/**
 * General class for a creatureAt. Holds X and Y coordinates, has functions
 * that every creatureAt uses/can use such as move, dig, attack and canEnter.
 * */
public class Creature {
	/** How far the creatures can see. */
	public static final int VISION_RANGE = 30;
	/** Inventory size */
	public static final int INV_SIZE = 15;
	/** The radius of which other creatures will be notified of something this creature does. */
	public static final int MSG_NOTIFICATION_RADIUS = 15;
	private World world;

	/** The maximum amount of health. */
	private int maxHp;
	public int getMaxHp() {return maxHp;}

	/** Current hp. */
	private int hp;
	public int getHp() {return hp;}

	/** The attack value, used when attacking other creatures.*/
	private int attackValue;
	/** Not used now, but might be useful for future. */
	public int getAttackValue() {return attackValue;}

	/** The defense value. */
	private int defenseValue;
	public int getDefenseValue() {return defenseValue;}

	/** The creatures inventory. */
	private Inventory inventory;
	public Inventory getInventory() { return inventory; }

	private int visionRadius;
	public int getVisionRadius() { return visionRadius;}

	/** The X coordinate of the creatureAt. */
	public int x;
	/** The Y coordinate of the creatureAt. */
	public int y;

	/** Name of the creatureAt. */
	private String name;
	/** Not used now, but might be useful for future. */
	public String getName() { return name; }

	/** The symbol/glyph of the creatureAt. */
	private char glyph;
	public char getGlyph() { return glyph; }

	/** The color of the creatures glyph.*/
	private Color color;
	public Color getColor() { return color; }

	private CreatureAi ai = null;
	/** Is it really that suspicious? */
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
		this.inventory = new Inventory(INV_SIZE);
		this.visionRadius = VISION_RANGE;
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

	public void pickup() {
		Item item = world.itemAt(x, y);

		if (inventory.isFull() || item == null) {
			doAction("try to pick up the ground.");
		} else {
			doAction("pickup a %s", item.getName());
			world.remove(x, y);
			inventory.add(item);
		}
	}

	public void drop(Item item) {
		if (item == null) {
			notify("You cannot drop that.");
			return;
		}
		if (world.addAtEmptyLocation(item, x, y)) {
			doAction("drop a " + item.getName());
			inventory.remove(item);
		} else {
			notify("There's nowhere to drop the %s", item.getName());
		}
	}

	/** Relative move function. If there is a creatureAt at current x + mx and current y + my,
	 * attack it. Else, check what happens on entering the given tile.
	 * @param mx relative x coordinate
	 * @param my relative y coordinate */
	public void moveBy(int mx, int my) {

		/** Prevents the creatures from killing themselves if they stand still. Useful. */
		if (mx == 0 && my == 0) {
			return;
		}

		Creature other = world.creatureAt(x+mx, y+my);

		if (other == null)
			ai.onEnter(x + mx, y + my, world.getTile(x + mx, y + my));
		else
			attack(other);
	}

	public void interact(int x, int y, Tile tile) {

		/** Will expand with more interactable tiles. I realise I miss tiles, but that is entirely
		 * conciously. With only one tile an if-statement would be better, but I will expand with more later. */
		switch (tile) {
			/** When interacting with a closed door, remove the door and replace it with
			 * an open door tile - which you can walk through. */
			case VALLEY_DOOR_CLOSED:
				world.dig(x, y);
				world.setTile(x, y, Tile.VALLEY_DOOR_OPEN);
				doAction("open the door");
				break;
		}

	}

	/** Useful for when creatures need to see what other creatures are in the world. */
	public Creature creatureAt(int wx, int wy) {
		return world.creatureAt(wx, wy);
	}

	/** Add ability to format strings! Good for displaying variable values,
	 * like damage done, HP recovered, etc. etc. */
	/** Not sure what this means. I couldn't find any conventions for naming overloaded methods.
	 * Perhaps it wants me to use void * instead of  . . .*/
	public void notify(String message, Object ... params) {
		ai.onNotify(String.format(message, params));
	}
	/** A function for notifying nearby creatures when a creatureAt does something.
	 * As in, if a creatureAt breaks something, it will show up in the message log of
	 * other nearby creatures. */
	public void doAction(String message, Object ... params) {
		/** The radius of which nearby creatures will notice the action
		 * happening. */
		int radius = MSG_NOTIFICATION_RADIUS;
		for (int rx = -radius; rx < radius + 1; rx++) {
			for (int ry = -radius; ry < radius + 1; ry++) {
				if (rx * rx + ry * ry > radius * radius)
					continue;

				Creature other = world.creatureAt(x + rx, y + ry);

				if (other == null)
					continue;

				if (Objects.equals(other, this))
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

	/** Attacks another creatureAt. Has an element of randomness. Uses modifyHp
	 * function to decrease HP. Might seem like a very simple attack system, but
	 * it's simple and it works well enough. I might extend it as a stretch goal.*/
	public void attack(Creature other) {

		int amount = Math.max(0, attackValue - other.defenseValue);

		amount = (int) (Math.random() * amount) + 1;
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

	/** You can enter a tile if there is not creatureAt there and the tile is a ground tile.
	 * @param wx World X coordinate
	 * @param wy World Y coordinate */
	public boolean canEnter(int wx, int wy) {
		return world.getTile(wx, wy).isGround() &&
				world.creatureAt(wx, wy) == null;
	}

}

package seaofdecay;

import java.awt.*;
import java.util.List;

/** EntityFactory for creating the different creatures and assigning their
 * AI to them as well as creating the items in the world.  */
public class EntityFactory {
	private final static char FUNGUS_CHAR = 145;
	private final static char PLAYER_CHAR = '@';
	private final static char MOTH_CHAR = 224;
	private static final char MUSHROOM_CHAR = 140;
	private static final char SPORE_CHAR = 15;
	private static final char VICTORY_ITEM_CHAR = 232;

	private int fungusSpawned = 0;
	public int getFungusSpawned() { return fungusSpawned; }

	private World world;

	public EntityFactory(World world) {
		this.world = world;
	}


	/**  -  -  -   Creatures   -  -  -  **/

	public Creature newPlayer(List<String> messages, FieldOfView fov) {
		/** The player hp */
		final int playerHP = 125;
		final int playerATK = 15;
		final int playerDEF = 7;

		/** Is reported as a magical constant, but it is a RGB value, don't think adding a constant for every R, G, and B
		 *  value for every color is necessary. */
		Creature player = new Creature(world, "player", PLAYER_CHAR, new Color(255, 38, 3), playerHP, playerATK, playerDEF);

		world.addAtEmptyLocation(player);
		player.setCreatureAi(new PlayerAi(player, messages, fov));

		return player;
	}

	public void newMoth() {
		final int mothHP = 20;
		final int mothATK = 8;
		final int mothDEF = 3;

		Creature moth = new Creature(world, "moth", MOTH_CHAR, new Color(255, 176, 1), mothHP, mothATK, mothDEF);

		world.addAtEmptyLocation(moth);
		moth.setCreatureAi(new MothAi(moth));

	}

	/** This one is not VOID type because it uses the return type for it's spawn (in FungusAi). */
	public Creature newFungus() {
		final int fungusHP = 8;
		final int fungusATK = 0;
		final int fungusDEF = 1;
		fungusSpawned++;

		Creature fungus = new Creature(world, "fungus", FUNGUS_CHAR, new Color(63, 249, 63), fungusHP, fungusATK, fungusDEF);

		world.addAtEmptyLocation(fungus);
		/** I pass in the creaturefactory to the FungusAi constructor since it
		 * uses the creaturefactory to create spawns. */
		fungus.setCreatureAi(new FungusAi(this, fungus));

		return fungus;

	}

	/**   -  -  -   Items   -  -  -   **/

	public void newMushroom() {
		Item mushroom = new Item(MUSHROOM_CHAR, new Color(252, 244, 249), "mushroom");
		world.addAtEmptyLocation(mushroom);

	}

	public void newSpore() {
		Item spore = new Item(SPORE_CHAR, new Color(255,255,255), "spore");
		world.addAtEmptyLocation(spore);

	}

	public void newVictoryItem() {
		Item item = new Item(VICTORY_ITEM_CHAR, new Color(255,0,255), "Lantern of The Ohm");
		world.addAtEmptyLocation(item);

	}

}

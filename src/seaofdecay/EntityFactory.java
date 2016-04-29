package seaofdecay;

import java.awt.*;
import java.util.List;

/** EntityFactory for creating the different creatures and assigning their
 * AI to them. */
public class EntityFactory {
	private final static char FUNGUS_CHAR = 145;
	private final static char PLAYER_CHAR = '@';
	private final static char MOTH_CHAR = 224;
	private int fungusSpawned = 0;
	public int getFungusSpawned() { return fungusSpawned; }
	private World world;

	public EntityFactory(World world) {
		this.world = world;
	}


	/**  -  -  -   Creatures   -  -  -  **/

	public Creature newPlayer(List<String> messages, FieldOfView fov) {

		int playerHP = 125;
		int playerATK = 15;
		int playerDEF = 7;

		Creature player = new Creature(world, "player", PLAYER_CHAR, new Color(255, 38, 3), playerHP, playerATK, playerDEF);

		world.addAtEmptyLocation(player);
		player.setCreatureAi(new PlayerAi(player, messages, fov));

		return player;
	}

	public Creature newMoth() {

		int mothHP = 20;
		int mothATK = 8;
		int mothDEF = 3;

		Creature moth = new Creature(world, "moth", MOTH_CHAR, new Color(255, 176, 1), mothHP, mothATK, mothDEF);

		world.addAtEmptyLocation(moth);
		moth.setCreatureAi(new MothAi(moth));

		return moth;
	}

	public Creature newFungus() {
		int fungusHP = 8;
		int fungusATK = 0;
		int fungusDEF = 1;
		fungusSpawned++;

		Creature fungus = new Creature(world, "fungus", FUNGUS_CHAR, new Color(63, 249, 63), fungusHP, fungusATK, fungusDEF);

		world.addAtEmptyLocation(fungus);
		/** I pass in the creaturefactory to the FungusAi constructor since it
		 * uses the creaturefactory to create spawns. */
		fungus.setCreatureAi(new FungusAi(this, fungus));

		return fungus;

	}

	/**   -  -  -   Items   -  -  -   **/

	public Item newMushroom() {
		Item mushroom = new Item((char)140, new Color(252, 244, 249), "mushroom");
		world.addAtEmptyLocation(mushroom);
		return mushroom;
	}

	public Item newSpore() {
		Item spore = new Item((char)15, new Color(255,255,255), "spore");
		world.addAtEmptyLocation(spore);
		return spore;
	}



}

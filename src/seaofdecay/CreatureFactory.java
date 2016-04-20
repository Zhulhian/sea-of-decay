package seaofdecay;

import java.awt.*;

/** CreatureFactory for creating the different creatures and assigning their
 * AI to them. */
public class CreatureFactory {
	private final static char FUNGUS_CHAR = 145;
	private final static char PLAYER_CHAR = '@';
	private World world;

	public CreatureFactory(World world) {
		this.world = world;
	}

	public Creature newPlayer() {
		Creature player = new Creature(world, PLAYER_CHAR, new Color(255, 73, 13), 125, 15, 7 );
		world.addAtEmptyLocation(player);
		player.setCreatureAi(new PlayerAi(player));
		return player;
	}

	public Creature newFungus() {
		Creature fungus = new Creature(world, FUNGUS_CHAR, new Color(63, 249, 63), 8, 0, 1);
		world.addAtEmptyLocation(fungus);
		/** I pass in the creaturefactory to the FungusAi constructor since it
		 * uses the creaturefactory to create spawns. */
		fungus.setCreatureAi(new FungusAi(this, fungus));
		return fungus;
	}
}

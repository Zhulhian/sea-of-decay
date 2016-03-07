package seaofdecay;

import seaofdecay.util.asciipanel.AsciiPanel;

import java.awt.*;

public class CreatureFactory {
    private World world;

    public CreatureFactory(World world) {
	this.world = world;
    }

    public Creature newPlayer() {
	Creature player = new Creature(world, '@', AsciiPanel.brightRed);
	world.addAtEmptyLocation(player);
	new PlayerAi(player);
	return player;
    }

    public Creature newFungus() {
	Creature fungus = new Creature(world, (char)145, new Color(63, 249, 63));
	world.addAtEmptyLocation(fungus);
	new FungusAi(fungus, this);
	return fungus;
    }
}

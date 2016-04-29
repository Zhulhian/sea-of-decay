package seaofdecay.screens;

import seaofdecay.Creature;
import seaofdecay.Item;

/**
 * Screen for dropping items
 */
public class DropScreen extends InventoryBasedScreen {

	public DropScreen(Creature player) {
		super(player);
	}

	protected String getVerb() {
		return "drop";
	}

	/** Anything can be dropped. */
	protected boolean isAcceptable(Item item) {
		return true;
	}

	protected Screen use(Item item) {
		player.drop(item);
		return null;
	}
}

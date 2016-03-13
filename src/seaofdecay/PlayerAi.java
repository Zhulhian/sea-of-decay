package seaofdecay;

/** The player AI. Handles what happens when walking into certain tiles, attacking, etc. */
public class PlayerAi extends CreatureAi {

	public PlayerAi(Creature creature) {
		super(creature);
	}

	// override the onEnter function
	public void onEnter(int x, int y, Tile tile) {
		if (tile.isGround()) {
			creature.x = x;
			creature.y = y;
		} else if (tile.isDiggable()) {
			creature.dig(x, y);
		} else if (tile.isInteractable()){
			creature.interact(x, y, tile);
		}
	}
}

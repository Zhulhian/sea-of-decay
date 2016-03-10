package seaofdecay;

/** The player AI. Handles what happens when walking into certain tiles, attacking, etc. */
public class PlayerAi extends CreatureAi {
//
//	public PlayerAi(Creature creature) {
//		super(creature);
//	}

	// override the onEnter function
	public void onEnter(Creature owner, int x, int y, Tile tile) {
		if (tile.isGround()) {
			owner.x = x;
			owner.y = y;
		} else if (tile.isDiggable()) {
			owner.dig(x, y);
		}
	}
}

package seaofdecay;

import java.util.List;

/** The player AI. Handles what happens when walking into certain tiles, attacking, etc. */
public class PlayerAi extends CreatureAi {

	private List<String> messages;
	private FieldOfView fov;

	public PlayerAi(Creature creature, List<String> messages, FieldOfView fov) {
		super(creature);
		this.messages = messages;
		this.fov = fov;
	}

	// override the onEnter function
	public void onEnter(int x, int y, Tile tile) {
		if (tile.isGround()) {
			creature.x = x;
			creature.y = y;
		}
		else if (tile.isDiggable()) {
			creature.dig(x, y);
		} else if (tile.isInteractable()){
			creature.interact(x, y, tile);
		}
	}

	public boolean canSee(int wx, int wy) {
		return fov.isVisible(wx, wy);
	}

	// Instead of creating a getter for the message list I'll rely on
	// constructor injection.
	public void onNotify(String message) {
		messages.add(message);
	}

}

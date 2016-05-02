package seaofdecay;

/** General Creature Ai class. Has functions that every AI should have
 * such as OnEnter and onUpdate. Will be inherited by the different Monster Ai:s
 * and the Player Ai. */
public class CreatureAi {
    protected Creature creature;

    public CreatureAi(Creature creature) {
        this.creature = creature;
    }

    public void onEnter(int x, int y, Tile tile) {
	    if (tile.isGround()) {
		    creature.x = x;
		    creature.y = y;
	    } else {
		    creature.doAction("bump into a wall");
	    }
    }

	public void wander() {
		int mx = (int)(Math.random() * 3) - 1;
		int my = (int)(Math.random() * 3) - 1;

		Creature other = creature.creatureAt(creature.x + mx, creature.y + my);

		if (other != null && other.getGlyph() == creature.getGlyph()) {
			/** It is not unecessary if I want to end execution early. */
			return;
		}
		else {
			creature.moveBy(mx, my);
		}
	}

    public void onUpdate() { }

	public boolean canSee(int wx, int wy) {
		if (Math.sqrt((creature.x - wx))+ Math.sqrt((creature.y - wy)) >
				creature.getVisionRadius() * creature.getVisionRadius()) {
			return false;
		}

		for (Point p : new Line(creature.x, creature.y, wx, wy)) {
			if (creature.getTile(p.x, p.y).isGround() || p.x == wx && p.y == wy)
				continue;

			return false;
		}

		return true;
	}

    public void onNotify(String message) {}
}

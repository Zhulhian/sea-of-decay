package seaofdecay;

/** General Creature Ai class. Has functions that every AI should have
 * such as OnEnter and onUpdate. Will be inherited by the different Monster Ai:s
 * and the Player Ai. */
public class CreatureAi {
    protected Creature creature;

    public CreatureAi(Creature creature) {
        this.creature = creature;
        //this.creature.setCreatureAi(this);
    }

    public void onEnter(int x, int y, Tile tile) { }

    public void onUpdate() { }

	/** Yes it is a complex expression, but calculating field of view is a complex operation. */
	public boolean canSee(int wx, int wy) {
		if ((creature.x - wx) * (creature.x - wx) + (creature.y - wy) * (creature.y - wy) >
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

package seaofdecay;

/** General Creature Ai class. Has functions that every AI should have
 * such as OnEnter and onUpdate. Will be inherited by the different Monster Ai:s
 * and the Player Ai. */
public class CreatureAi {
    //protected Creature creature;

    //public CreatureAi(Creature creature) {
        //this.creature = creature;
        //this.creature.setCreatureAi(this);
    //}

    public void onEnter(Creature owner, int x, int y, Tile tile) { }

    public void onUpdate() {

    }
}

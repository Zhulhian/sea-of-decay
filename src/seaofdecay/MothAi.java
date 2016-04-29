package seaofdecay;

/**
 * Created by dan on 28-Apr-16.
 */
public class MothAi extends CreatureAi {

	public MothAi(Creature creature) {
		super(creature);
	}

	/** Bats move two times for every one of the players turns. */
	public void onUpdate() {
		wander();
		wander();
	}
}

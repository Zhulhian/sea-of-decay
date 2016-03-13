package seaofdecay;
/**
 * AI for the fungus enemy. Doesn't attack but floods the
 * forest quickly through duplication. Very weak. */
public class FungusAi extends CreatureAi {
	/**
	 * Used for spreading of fungus. If math.random() is less than this,
	 * and spreadCount is less than five, the fungus spreads (duplicates).
	 * */
	public static final double SPREAD_CHANCE = 0.01;
	/** The diameter of the spread. New fungi will spawn within this / 2 of the mother fungus. */
	public static final int SPREAD_DIAMETER = 10;
	private int spreadCount;

	private CreatureFactory fungusFactory;

	public FungusAi(CreatureFactory factory, Creature creature) {
		super(creature);
		this.fungusFactory = factory;
	}

	public void onUpdate() {
		if (spreadCount < 5 && Math.random() < SPREAD_CHANCE)
			spread();
	}

//  Commented out for now. Might not use at all.
//	public void setSpreadCount(int newSpread) {
//		spreadCount = newSpread;
//	}


	private void spread() {
		int x = creature.x + (int)(Math.random() * SPREAD_DIAMETER + 1) - SPREAD_DIAMETER / 2;
		int y = creature.y + (int)(Math.random() * SPREAD_DIAMETER + 1) - SPREAD_DIAMETER / 2;

		if (!creature.canEnter(x, y))
			return;

		Creature offspring = fungusFactory.newFungus();

		offspring.x = x;
		offspring.y = y;

		spreadCount++;
	}
}

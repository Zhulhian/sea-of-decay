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
	private CreatureFactory fungusFactory;
	private int spreadCount;

	public FungusAi(Creature creature, CreatureFactory factory) {
		super(creature);
		this.fungusFactory = factory;
	}

	public void onUpdate() {
		if (spreadCount < 5 && Math.random() < SPREAD_CHANCE)
			spread();
	}

	public void setSpreadCount(int newSpread) {
		spreadCount = newSpread;
	}

	private void spread() {
		int x = creature.x + (int)(Math.random() * 11) - 5;
		int y = creature.y + (int)(Math.random() * 11) - 5;

		if (!creature.canEnter(x, y))
			return;

		Creature offspring = fungusFactory.newFungus();

		offspring.x = x;
		offspring.y = y;

		spreadCount++;
	}
}

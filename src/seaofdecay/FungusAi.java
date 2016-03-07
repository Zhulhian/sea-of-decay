package seaofdecay;

public class FungusAi extends CreatureAi {
    private CreatureFactory fungusFactory;
    private int spreadCount;

    public FungusAi(Creature creature, CreatureFactory factory) {
        super(creature);
        this.fungusFactory = factory;
    }

    public void onUpdate() {
        if (spreadCount < 5 && Math.random() < 0.02)
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

        Creature spawn = fungusFactory.newFungus();

        spawn.x = x;
        spawn.y = y;

        spreadCount++;
    }
}

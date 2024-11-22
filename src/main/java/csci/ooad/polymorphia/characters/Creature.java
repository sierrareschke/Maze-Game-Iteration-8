package csci.ooad.polymorphia.characters;


public class Creature extends Character {
    public static final Double DEFAULT_INITIAL_HEALTH = 3.0;

    public Creature(String name) {
        this(name, DEFAULT_INITIAL_HEALTH);
    }

    public Creature(String name, double health) {
        super(name, health, new DoNothingStrategy());
    }

    public Creature(String name, Double initialHealth, Strategy strategy) {
        super(name, initialHealth, strategy);
    }

    @Override
    public Boolean isCreature() {
        return true;
    }
}

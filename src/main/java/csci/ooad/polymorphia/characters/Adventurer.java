package csci.ooad.polymorphia.characters;

import java.util.Optional;


public class Adventurer extends Character {

    public Adventurer(String name) {
        this(name, Character.DEFAULT_INITIAL_HEALTH, new BaseStrategy());
    }

    public Adventurer(String name, Double initialHealth) {
        super(name, initialHealth, new BaseStrategy());
    }

    public Adventurer(String name, Double initialHealth, Strategy strategy) {
        super(name, initialHealth, strategy);
    }

    protected boolean iAmHealthiestInRoom() {
        Optional<Adventurer> possibleAdventurer = getCurrentLocation().getHealthiestAdventurer();
        return possibleAdventurer.isPresent() && this.equals(possibleAdventurer.get());
    }

    @Override
    public Boolean isAdventurer() {
        return true;
    }


}
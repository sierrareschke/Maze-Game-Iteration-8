package csci.ooad.polymorphia.characters;

public class HungryStrategy extends BaseStrategy {


    @Override
    Boolean shouldFight(Character character) {
        // Don't fight if I can eat!
        return !character.getCurrentLocation().hasFood();
    }
}

package csci.ooad.polymorphia.characters;

public class CowardlyStrategy extends BaseStrategy {

    @Override
    Boolean shouldFight(Character character) {
        return character.cannotMove();
    }
}

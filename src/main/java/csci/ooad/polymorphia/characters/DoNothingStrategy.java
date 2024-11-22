package csci.ooad.polymorphia.characters;

public class DoNothingStrategy implements Strategy {

    @Override
    public Command generateCommand(Character character) {
        return new DoNothingCommand() {};
    }
}

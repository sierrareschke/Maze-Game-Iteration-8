package csci.ooad.polymorphia.characters;

public class DoNothingCommand implements Command {

    public void execute() {
        // Do nothing
    }

    @Override
    public String getName() {
        return "DO NOTHING";
    }
}

package csci.ooad.polymorphia.characters;


public abstract class PolymorphiaCommand implements Command {
    Character character;
    String name;

    PolymorphiaCommand(Character character, String name) {
        this.character = character;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

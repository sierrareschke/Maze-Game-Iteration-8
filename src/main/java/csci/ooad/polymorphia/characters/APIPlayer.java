package csci.ooad.polymorphia.characters;

public class APIPlayer extends Adventurer{
    public APIPlayer(String name) {
        super(name);
    }

    public APIPlayer(String name, Double initialHealth) {
        super(name, initialHealth);
    }

    public APIPlayer(String name, Double initialHealth, Strategy strategy) {
        super(name, initialHealth, strategy);
    }
}

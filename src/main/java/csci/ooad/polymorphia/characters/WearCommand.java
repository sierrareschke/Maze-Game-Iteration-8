package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Armor;
import csci.ooad.polymorphia.EventBus;
import csci.ooad.polymorphia.EventType;
import csci.ooad.polymorphia.Maze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WearCommand extends PolymorphiaCommand {
    private static final Logger logger = LoggerFactory.getLogger(WearCommand.class);

    final Armor armor;

    public WearCommand(Character character, Armor armor) {
        super(character, "WEAR ARMOR");
        this.armor = armor;
    }

    public void execute() {
        Maze.Room currentRoom = character.getCurrentLocation();
        if (currentRoom.hasArmor(armor)) {
            currentRoom.removeItem(armor);
            Character armoredAdventurer = new ArmoredCharacter(character, armor);

            currentRoom.replace(character, armoredAdventurer);

            String eventDescription = character.getName() + " put on " + armor;
            logger.info(eventDescription);
            EventBus.getInstance().postMessage(EventType.PickedUpSomething, eventDescription);
        } else {
            logger.warn("The room does not have an armor of {}", armor);
        }
    }
}

package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.Maze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EatCommand extends PolymorphiaCommand {
    private static final Logger logger = LoggerFactory.getLogger(EatCommand.class);

    final Food foodItem;

    public EatCommand(Character character, Food foodItem) {
        super(character, "EAT");
        this.foodItem = foodItem;
    }

    @Override
    public void execute() {
        Maze.Room currentRoom = character.getCurrentLocation();
        if (currentRoom.contains(foodItem)) {
            character.gainHealth(foodItem.healthValue());
            currentRoom.removeItem(foodItem);
        } else {
            logger.warn("The food item {} was not in room {}so nothing happened for this command", foodItem, currentRoom.getName());
        }

    }
}

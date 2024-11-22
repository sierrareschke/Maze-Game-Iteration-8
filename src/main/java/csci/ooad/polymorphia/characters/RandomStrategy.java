package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.Maze;

import java.util.*;

public class RandomStrategy extends BaseStrategy {
    private final Random random = new Random();


    @Override
    public Command generateCommand(Character character) {
        List<Command> possibleCommands = new ArrayList<>(Collections.singletonList(commandFactory.createDoNothingCommand()));
        Maze.Room currentRoom = character.getCurrentLocation();

        List<Creature> creatures = currentRoom.getLivingCreatures();
        if (!creatures.isEmpty()) {
            Character randomCreature = creatures.get(random.nextInt(creatures.size()));
            possibleCommands.add(commandFactory.createFightCommand(character, randomCreature));
        }

        if (currentRoom.hasFood()) {
            Food randomFoodItem = currentRoom.getFoodItems().get(random.nextInt(currentRoom.getFoodItems().size()));
            possibleCommands.add(commandFactory.createEatCommand(character, randomFoodItem));
        }

        if (currentRoom.hasNeighbors()) {
            Maze.Room randomRoom = currentRoom.getRandomNeighbor();
            possibleCommands.add(commandFactory.createMoveCommand(character, randomRoom));
        }
        return possibleCommands.get(random.nextInt(possibleCommands.size()));
    }
}

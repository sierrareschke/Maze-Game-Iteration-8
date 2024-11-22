package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Maze;
import java.util.Optional;

public class DemonStrategy extends BaseStrategy {
    final CommandFactory commandFactory = new CommandFactory();

    public Command generateCommand(Character character) {
        Maze.Room currentRoom = character.getCurrentLocation();

        Optional<Adventurer> healthiestAdventurer = currentRoom.getHealthiestAdventurer();
        if (healthiestAdventurer.isPresent()) {
            return commandFactory.createFightCommand(character, healthiestAdventurer.get());
        }

        // If there are no adventurers in this room, the demon goes hunting for them!
        Maze.Room nextRoom = currentRoom.getRandomNeighbor();
        if (nextRoom != null) {
            return commandFactory.createMoveCommand(character, currentRoom.getRandomNeighbor());
        }
        return commandFactory.createDoNothingCommand();
    }
}

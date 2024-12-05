package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.Maze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HumanStrategy implements Strategy {
    public enum CommandOption {EAT, FIGHT, MOVE, WEAR_ARMOR, DO_NOTHING}

    final CommandFactory commandFactory = new CommandFactory();
    private static final Logger logger = LoggerFactory.getLogger(HumanStrategy.class);

    @Override
    public Command generateCommand(Character character) {

        List<CommandOption> availableCommands = availableCommandOptions(character);
        CommandOption choice = getTopLevelChoice(availableCommands, character);
        return getCommand(character, choice);
    }

    public Command generateSelectedCommand(Character character, String commandString){
        CommandOption selectedCommand = getCommandOption(commandString);
        return getCommand(character,selectedCommand);
    }

    private CommandOption getCommandOption(String commandString) {
        switch(commandString){
            case "DO_NOTHING":
                return CommandOption.DO_NOTHING;
            case "MOVE":
                return CommandOption.MOVE;
            case "EAT":
                return CommandOption.EAT;
            case "WEAR_ARMOR":
                return CommandOption.WEAR_ARMOR;
            case "FIGHT":
                return CommandOption.FIGHT;
            case null:
                logger.info("command string was null");
                return CommandOption.DO_NOTHING;
            case "NULL":
                logger.info("command string was NULL");
                return CommandOption.DO_NOTHING;
            default:
                return CommandOption.DO_NOTHING;
        }
    }


    Command getCommand(Character character, CommandOption choice) {
        Maze.Room currentRoom = character.getCurrentLocation();
        switch (choice) {
            case EAT:
                Food foodItem = currentRoom.selectRandomFood();
                return commandFactory.createEatCommand(character, foodItem);
            case FIGHT:
                Optional<Creature> creature = currentRoom.getHealthiestCreature();
                if (creature.isPresent()) {
                    return commandFactory.createFightCommand(character, creature.get());
                }
            case MOVE:
                return commandFactory.createMoveCommand(character, currentRoom.getRandomNeighbor());
            case WEAR_ARMOR:
                return commandFactory.wearArmor(character, currentRoom.selectRandomArmor());
            default:
                return commandFactory.createDoNothingCommand();
        }
    }

    public List<CommandOption> availableCommandOptions(Character character) {
        Maze.Room currentRoom = character.getCurrentLocation();

        List<CommandOption> options = new ArrayList<>();
        options.add(CommandOption.DO_NOTHING);
        if (currentRoom.hasNeighbors()) {
            options.add(CommandOption.MOVE);
        }
        if (currentRoom.hasFood()) {
            options.add(CommandOption.EAT);
        }
        if (currentRoom.hasArmor()) {
            options.add(CommandOption.WEAR_ARMOR);
        }
        if (currentRoom.hasLivingCreatures()) {
            options.add(CommandOption.FIGHT);
        }

        return options;
    }

    CommandOption getTopLevelChoice(List<CommandOption> options, Character character) {
        Maze.Room currentRoom = character.getCurrentLocation();

        System.out.print("You are in room " + currentRoom + "\n\n");
        int currentChoiceNumber = 1;
        for (CommandOption option : options) {
            System.out.println(currentChoiceNumber + ": " + option);
            currentChoiceNumber++;
        }
        while (true) {  // This exits the method if a valid option is selected
            System.out.print("Enter your option: ");
            Scanner scanner = new Scanner(System.in);
            try {
                int choiceNumber = Integer.parseInt(scanner.nextLine()) - 1;
                System.out.println("Your chosen option is " + options.get(choiceNumber).name());
                return options.get(choiceNumber);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option selected");
            }
        }
    }
}

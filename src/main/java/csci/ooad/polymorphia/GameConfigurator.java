package csci.ooad.polymorphia;

import csci.ooad.layout.intf.IMazeObserver;
import csci.ooad.layout.intf.MazeObserver;
import csci.ooad.polymorphia.observer.AudibleObserver;
import org.apache.commons.cli.*;


public class GameConfigurator {
    static int SECONDS_TO_PAUSE_BETWEEN_TURNS = 1;

    private final Maze.Builder mazeBuilder;

    GameConfigurator(CommandLine cmdLine) {
        mazeBuilder = Maze.getNewBuilder();
        try {
            buildMazeFromArguments(cmdLine);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine cmdLine = parser.parse(getOptions(), args);
            GameConfigurator gameConfigurator = new GameConfigurator(cmdLine);
            gameConfigurator.createAndStartGame();
            System.exit(0);
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }
    }

    static Options getOptions() {
        Option numAdventurers = Option.builder("a")
                .longOpt("numberOfAdventurers")
                .argName("numAdventurers")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the number of adventurers to place in the maze")
                .build();

        Option numCreatures = Option.builder("c")
                .longOpt("numberOfCreatures")
                .argName("numCreatures")
                .hasArg()
                .type(Number.class)
                .numberOfArgs(1)
                .desc("the number of creatures to place in the maze")
                .build();

        Option numDemons = Option.builder("d")
                .longOpt("numberOfDemons")
                .argName("numDemons")
                .hasArg()
                .type(Number.class)
                .numberOfArgs(1)
                .desc("the number of demons to place in the maze")
                .build();

        Option numFoodItems = Option.builder("f")
                .longOpt("numberOfFoodItems")
                .argName("numFoodItems")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the number of food items to place in the maze")
                .build();

        Option humanPlayer = Option.builder("h")
                .longOpt("humanPlayer")
                .argName("humanPlayerName")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the human player's name")
                .build();

        Option numArmoredSuits = Option.builder("m")
                .longOpt("numberOfArmoredSuits")
                .argName("numArmoredSuits")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the number of armored suits in the maze")
                .build();

        Option numRooms = Option.builder("r")
                .longOpt("numberOfRooms")
                .argName("numRooms")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the number of rooms in the maze")
                .build();

        Option help = new Option("help", "print this message");

        Options options = new Options();
        options.addOption(numAdventurers);
        options.addOption(numCreatures);
        options.addOption(numDemons);
        options.addOption(numFoodItems);
        options.addOption(humanPlayer);
        options.addOption(numArmoredSuits);
        options.addOption(numRooms);
        options.addOption(help);

        return options;
    }

    void buildMazeFromArguments(CommandLine cmdLine) throws ParseException {
        if (cmdLine.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Polymorphia", getOptions());
            System.exit(0);
        }
        int numRooms = 6;
        if (cmdLine.hasOption("r")) {
            numRooms = ((Number) cmdLine.getParsedOptionValue("r")).intValue();
            mazeBuilder.createFullyConnectedRooms(numRooms);
        }
        int numAdventurers = 3;
        if (cmdLine.hasOption("a")) {
            numAdventurers = Integer.parseInt(cmdLine.getOptionValue("a"));
            mazeBuilder.createAndAddAdventurers(numAdventurers);
        }
        int numCreatures = 3;
        if (cmdLine.hasOption("c")) {
            numCreatures = ((Number) cmdLine.getParsedOptionValue("c")).intValue();
            mazeBuilder.createAndAddCreatures(numCreatures);
        }
        int numDemons = 3;
        if (cmdLine.hasOption("d")) {
            numDemons = ((Number) cmdLine.getParsedOptionValue("d")).intValue();
            mazeBuilder.createAndAddDemons(numDemons);
        }
        int numFoodItems = 6;
        if (cmdLine.hasOption("f")) {
            numFoodItems = ((Number) cmdLine.getParsedOptionValue("f")).intValue();
            mazeBuilder.createAndAddFoodItems(numFoodItems);
        }
        if (cmdLine.hasOption("h")) {
            mazeBuilder.createAndAddHumanPlayer(cmdLine.getOptionValue("h"));
        }
        int numArmorSuits = 2;
        if (cmdLine.hasOption("m")) {
            numArmorSuits = ((Number) cmdLine.getParsedOptionValue("m")).intValue();
            mazeBuilder.createAndAddArmor(numArmorSuits);
        }
    }

    public void createAndStartGame() {
        Polymorphia polymorphia = new Polymorphia(mazeBuilder.build());

        IMazeObserver mazeObserver = MazeObserver.getNewBuilder("Polymorphia Game")
                .useRadialLayoutStrategy()
                .setDelayInSecondsAfterUpdate(SECONDS_TO_PAUSE_BETWEEN_TURNS)
                .setRoomDimension(150)
                .setWidth(1000)
                .setHeight(1000)
                .build();
        polymorphia.attach(mazeObserver);

       new AudibleObserver(polymorphia, EventType.All);
        polymorphia.play();
    }
}

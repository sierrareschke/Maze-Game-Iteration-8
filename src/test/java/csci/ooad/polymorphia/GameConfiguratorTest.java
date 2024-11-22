package csci.ooad.polymorphia;

import org.junit.jupiter.api.Disabled;


class GameConfiguratorTest {
    @Disabled
    void testParsingLongArgumentsAndRunningGame() {
        String[] args = new String[]{
                "--numberOfRooms", "2",
                "--numberOfAdventurers", "1",
                "--numberOfCreatures", "1",
                "--numberOfFoodItems", "2",
                "--numberOfArmoredSuits", "2"
        };
        GameConfigurator.main(args);
    }

    @Disabled
    void testParsingShortArguments() {
        String[] args = new String[]{
                "-r", "3",
                "-a", "3",
                "-c", "3",
                "-f", "3"
        };
        GameConfigurator.main(args);
    }

    @Disabled
    void testHelp() {
        String[] args = new String[]{
                "-help"
        };
        GameConfigurator.main(args);
    }
}
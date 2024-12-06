package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Character;
import csci.ooad.polymorphia.characters.CharacterFactory;
import csci.ooad.polymorphia.characters.Creature;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PolymorphiaTest {
    static final String STARTING_ROOM_NAME = "Starting Room";

    @Test
    void testOneRoom() {
        // Baby steps!
        Maze oneRoomMaze = Maze.getNewBuilder()
                .createRoom(STARTING_ROOM_NAME)
                .createAndAddAdventurers("Frodo")
                .createAndAddCreatures("Ogre")
                .build();
        Polymorphia game = new Polymorphia("testOneRoom", oneRoomMaze);

        // Act
        game.play();

        // Assert
        assert game.isOver();
    }

    @Test
    public void test2x2Game() {
        // Arrange
        Maze twoByTwoMaze = Maze.getNewBuilder()
                .create2x2Grid()
                .createAndAddAdventurers("Frodo")
                .createAndAddCreatures("Ogre")
                .build();
        Polymorphia game = new Polymorphia("test2x2Game", twoByTwoMaze);

        // Act
        game.play();

        // Assert
        assert game.isOver();
    }

    @Test
    public void test3x3Game() {
        // Arrange
        Maze threeByThreeMaze = Maze.getNewBuilder()
                .create3x3Grid()
                .createAndAddAdventurers("Frodo")
                .createAndAddCreatures("Ogre")
                .build();
        Polymorphia game = new Polymorphia("test3x3Game", threeByThreeMaze);

        // Act
        game.play();

        // Test end of game play
        Character winner = game.getWinner();

        // Assert
        assertNotNull(winner);
        assert game.isOver();
    }

    @Test
    public void testHasLivingCharacters() {
        Maze threeByThreeMaze = Maze.getNewBuilder()
                .create3x3Grid()
                .createAndAddAdventurers("Frodo")
                .createAndAddCreatures("Ogre")
                .build();
        Polymorphia game = new Polymorphia("test3x3Game", threeByThreeMaze);
        List<Character> livingCharacters = game.getLivingCharacters();
        assertEquals(2,livingCharacters.size());
    }

    @Test
    public void testGetMaze(){
        Maze threeByThreeMaze = Maze.getNewBuilder()
                .create3x3Grid()
                .createAndAddAdventurers("Frodo")
                .createAndAddCreatures("Ogre")
                .build();
        Polymorphia game = new Polymorphia("test3x3Game", threeByThreeMaze);
        Maze maze = (Maze) game.getMaze();
        assert maze != null;
        assertEquals(9, maze.getRooms().size());
    }

    @Disabled
    void testFairPlay() {
        int adventurerWins = 0;
        int creatureWins = 0;
        int numTies = 0;

        int TOTAL_GAMES = 100;
        for (int i = 0; i < TOTAL_GAMES; i++) {
            Maze threeByThreeMaze = Maze.getNewBuilder()
                    .create3x3Grid()
                    .createAndAddAdventurers(2)
                    .createAndAddCreatures(5)
                    .createAndAddFoodItems(20)
                    .build();

            Polymorphia game = new Polymorphia(threeByThreeMaze);
            game.play();
            if (game.getWinner() == null) {
                numTies++;
            } else if (game.getWinner().isCreature()) {
                creatureWins++;
            } else {
                adventurerWins++;
            }
        }

        System.out.println("Adventurers won " + adventurerWins + " and creatures won " + creatureWins);
        System.out.println("There were " + numTies + " games with no winners");

        double adventureWinRatio = (double) adventurerWins / (double) TOTAL_GAMES;
        System.out.println("Adventures won " + (adventureWinRatio * 100) + "% of the games.");

        // Check to see that adventurers win at least 10% of the games
        assertTrue(adventureWinRatio > 0.1);
    }

    @Test
    void testIndividualRoomCreation() throws NoSuchRoomException {
        Adventurer adventure = new Adventurer("Frodo");
        Creature creature = new Creature("Ogre");
        Maze twoRoomMaze = Maze.getNewBuilder()
                .createFullyConnectedRooms("initial", "final")
                .addToRoom("initial", adventure)
                .addToRoom("final", creature)
                .build();

        assertEquals(2, twoRoomMaze.size());
        assertTrue(twoRoomMaze.getRoom("initial").hasLivingAdventurers());
        assertTrue(twoRoomMaze.getRoom("final").hasLivingCreatures());
    }


    @Test
    void testUseOfFactories() throws NoSuchRoomException {
        Maze maze = Maze.getNewBuilder(new CharacterFactory(), new ArtifactFactory())
                .createRoom("initial")
                .createAndAddAdventurers("Frodo")
                .createAndAddKnights("Sir Galahad")
                .createAndAddGluttons("Chubs")
                .createAndAddCowards("Sir Robin")
                .createAndAddCreatures("Ogre")
                .createAndAddDemons("Satan")
                .createAndAddFoodItems("Popcorn")
                .build();

        assertTrue(maze.hasLivingAdventurers());
        assertTrue(maze.hasLivingCreatures());
        assertTrue(maze.getRoom("initial").hasFood());
    }

    @Test
    void testSequentialDistribution() {
        Maze maze = Maze.getNewBuilder()
                .createFullyConnectedRooms(4)
                .distributeSequentially()
                .createAndAddFoodItems("hot dog", "popcorn", "chili dog", "Coke")
                .build();


        // Since we added four food items into a four-room maze, with sequential distribution, each room should have some food.
        for (Maze.Room room : maze.getRooms()) {
            assertTrue(room.hasFood());
        }
    }

    @Test
    void testRandomDistribution() {
        Maze maze = Maze.getNewBuilder()
                .createFullyConnectedRooms(10)
                .distributeRandomly()
                .createAndAddFoodItems("hot dog", "popcorn", "chili dog", "Coke", "apple", "bread", "orange", "banana", "ham", "salad")
                .build();


        // Since we added ten food items into a ten-room maze, with random distribution, it's likely (not assured)
        // that one room will not have any food in it.
        assertTrue(maze.getRooms().stream().anyMatch(r -> !r.hasFood()));
    }

    @Test
    void testRoomsWithNConnections() {
        int minimumNumberOfNeighbors = 2;
        Maze maze = Maze.getNewBuilder()
                .createConnectedRooms(2, 5)
                .distributeSequentially()
                .createAndAddFoodItems("hot dog", "popcorn", "chili dog", "Coke", "cookie")
                .build();

        for (Maze.Room room : maze.getRooms()) {
            assertTrue(room.numberOfNeighbors() >= minimumNumberOfNeighbors);
        }
    }

    @Test
    void testCreationViaNumber() {
        Maze maze = Maze.getNewBuilder()
                .createConnectedRooms(3, 4)
                .createAndAddAdventurers(4)
                .createAndAddCreatures(4)
                .createAndAddFoodItems(4)
                .build();

        assertEquals(4, maze.getRooms().size());
    }

    @Test
    void testWithEverything() {
        Maze maze = Maze.getNewBuilder()
                .createFullyConnectedRooms(7)
                .createAndAddAdventurers("Frodo", "Arwen")
                .createAndAddAdventurers(1)
                .createAndAddKnights("Sir Galahad")
                .createAndAddKnights(1)
                .createAndAddCowards("Sir Robin")
                .createAndAddCowards(1)
                .createAndAddGluttons("Chubs")
                .createAndAddGluttons(1)
                .createAndAddCreatures("Dragon", "Ogre", "Orc", "Shelob", "Troll", "Evil Wizard")
                .createAndAddCreatures(1)
                .createAndAddDemons("Satan", "Beelzebub")
                .createAndAddDemons(1)
                .createAndAddFoodItems("cupcake", "apple", "banana", "steak", "salad", "fries")
                .createAndAddFoodItems(1)
                .build();

        Polymorphia polymorphia = new Polymorphia("testWithEverything", maze);
        polymorphia.play();
    }

}

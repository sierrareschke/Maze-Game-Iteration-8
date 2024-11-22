package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.CharacterFactory;
import csci.ooad.polymorphia.characters.Creature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MazeTest {
    private static final Logger logger = LoggerFactory.getLogger(MazeTest.class);

    @Test
    void test2x2GridCreation() {
        Maze twoByTwoMaze = Maze.getNewBuilder()
                .create2x2Grid()
                .createAndAddAdventurers("Frodo")
                .createAndAddCreatures("Ogre")
                .createAndAddFoodItems("Cookie")
                .build();
        assertEquals(4, twoByTwoMaze.size());
    }

    @Test
    void testHasLivingAdventurers() {
        Maze maze = Maze.getNewBuilder()
                .create2x2Grid()
                .createAndAddAdventurers("Frodo")
                .build();

        assertTrue(maze.hasLivingAdventurers());

        maze.addToRandomRoom(new Adventurer("Arwen"));
        assertEquals(maze.getLivingAdventurers().size(), 2);
    }

    @Test
    void testHasLivingCreatures() {
        Maze maze = Maze.getNewBuilder()
                .create2x2Grid()
                .createAndAddCreatures("Ogre")
                .build();
        assertTrue(maze.hasLivingCreatures());

        maze.addToRandomRoom(new Creature("Dragon"));
        assertEquals(maze.getLivingCreatures().size(), 2);
    }

    @Test
    void testToString() {
        Maze maze = Maze.getNewBuilder()
                .create2x2Grid()
                .createAndAddAdventurers("Frodo")
                .createAndAddCreatures("Ogre")
                .build();

        String mazeString = maze.toString();
        logger.info(mazeString);

        assertTrue(mazeString.contains("Northwest"));   // Hard-coded room name for 2x2 grid
        assertTrue(mazeString.contains("Frodo"));
        assertTrue(mazeString.contains("Ogre"));
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
    void testNoSuchRoom() {
        Maze maze = Maze.getNewBuilder()
                .createConnectedRooms(3, 4)
                .build();

        assertThrows(NoSuchRoomException.class, () -> maze.getRoom("nonexistent"));
    }

}
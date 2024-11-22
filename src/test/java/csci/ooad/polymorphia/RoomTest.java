package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    CharacterFactory characterFactory = new CharacterFactory();
    ArtifactFactory artifactFactory = new ArtifactFactory();

    @Test
    void getRandomNeighbor() throws NoSuchRoomException {
        Maze maze = Maze.getNewBuilder().createFullyConnectedRooms("mainRoom", "neighbor").build();
        Maze.Room mainRoom = maze.getRoom("mainRoom");
        Maze.Room neighbor = maze.getRoom("neighbor");

        assertEquals(mainRoom.getRandomNeighbor(), neighbor);
    }

    @Test
    void testGetRandomNeighborOnRoomWithNoNeighbors() {
        Maze.Room room = new Maze.Room("onlyRoom");
        assertNull(room.getRandomNeighbor());
    }

    @Test
    void testToString() throws NoSuchRoomException {
        Maze maze = Maze.getNewBuilder()
                .createFullyConnectedRooms("onlyRoom")
                .createAndAddAdventurers("Frodo")
                .createAndAddCreatures("Ogre")
                .build();

        Maze.Room room = maze.getRoom("onlyRoom");

        System.out.println(room);

        assertTrue(room.toString().contains("onlyRoom"));
        assertTrue(room.toString().contains("Frodo"));
        assertTrue(room.toString().contains("Ogre"));
    }

    @Test
    void testGetHealthiestAdventurer() throws NoSuchRoomException {
        // Arrange
        Adventurer bilbo = characterFactory.createAdventurer("Bilbo");
        Creature troll = characterFactory.createCreature("Troll");
        Maze maze = Maze.getNewBuilder()
                .createFullyConnectedRooms("onlyRoom")
                .addAdventurers(bilbo)
                .addCreatures(troll)
                .build();

        Maze.Room room = maze.getRoom("onlyRoom");

        // Assert
        assertTrue(room.isHealthiestAdventurer(bilbo));
        assertTrue(room.isHealthiestCreature(troll));
    }

    @Test
    void testOneAdventurerEatsFood() {
        // Arrange
        double highestHealth = 5;
        double lowestHealth = 3;

        Food popcorn = artifactFactory.createFood("Popcorn");

        Adventurer bilbo = characterFactory.createAdventurer("Bilbo");
        Adventurer frodo = characterFactory.createAdventurer("Frodo");
        Maze.getNewBuilder()
                .createFullyConnectedRooms("onlyRoom")
                .addAdventurers(bilbo, frodo)
                .addToRoom("onlyRoom", popcorn)
                .build();

        // Act
        Command command = bilbo.getAction();
        System.out.println("Executing command: " + command);
        command.execute();

        // Assert
        System.out.println(bilbo);
        System.out.println(frodo);
        assertTrue((bilbo.getHealth() == highestHealth + ArtifactFactory.DEFAULT_FOOD_HEALTH_VALUE) ||
                (frodo.getHealth() == lowestHealth + ArtifactFactory.DEFAULT_FOOD_HEALTH_VALUE));
    }

    @Test
    void testEatNonExistentFood() {
        // Arrange
        Maze.Room room = new Maze.Room("onlyRoom");
        assertThrows(NotFoundInRoomException.class, room::removeFoodItem);
    }
}
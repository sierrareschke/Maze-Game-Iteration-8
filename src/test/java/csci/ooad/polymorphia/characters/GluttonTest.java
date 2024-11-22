package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.ArtifactFactory;
import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.NoSuchRoomException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GluttonTest {
    CharacterFactory characterFactory = new CharacterFactory();
    ArtifactFactory artifactFactory = new ArtifactFactory();

    @Test
    void testEating() {
        // Arrange
        Adventurer glutton = characterFactory.createGlutton("Glutton");
        Maze.getNewBuilder()
                .createFullyConnectedRooms(1)
                .addAdventurers(glutton)
                .createAndAddCreatures("Ogre")
                .createAndAddFoodItems("Cake")
                .build();

        // Act - the glutton should not fight. It should eat
        glutton.getAction().execute();

        // Assert – the glutton ate the cake and gain 1 health point
        assertEquals(Character.DEFAULT_INITIAL_HEALTH + 1, glutton.getHealth());
    }

    @Test
    void testFighting() throws NoSuchRoomException {
        // Arrange - put Demon in room with Glutton
        Adventurer glutton = characterFactory.createGlutton("Glutton");
        Creature satan = characterFactory.createDemon("Satan");
        Food steak = artifactFactory.createFood("Steak");
        Maze twoRoomMaze = Maze.getNewBuilder()
                .createFullyConnectedRooms("initial", "final")
                .addToRoom("initial", glutton)
                .addToRoom("initial", satan)
                .addToRoom("initial", steak)
                .build();

        // Act - the coward never fights a Demon, but a Demon fights it
        satan.getAction().execute();

        // Assert – the coward ran to the other room
        assertNotEquals(CharacterFactory.DEMON_INITIAL_HEALTH, satan.getHealth());
        assertTrue(twoRoomMaze.getRoom("initial").hasFood());
    }
}

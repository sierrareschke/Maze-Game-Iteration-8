package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.NoSuchRoomException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandTest {
    CharacterFactory characterFactory = new CharacterFactory();

    @Test
    void testMoveCommandCreation() throws NoSuchRoomException {
        Adventurer adventurer = characterFactory.createCoward("Adventurer");
        Maze twoRoomMaze = Maze.getNewBuilder()
                .createFullyConnectedRooms("initial", "final")
                .addToRoom("initial", adventurer)
                .build();

        Command moveCommand = new MoveCommand(adventurer, twoRoomMaze.getRoom("final"));
        moveCommand.execute();

        assertEquals(adventurer.getCurrentLocation(), twoRoomMaze.getRoom("final"));
    }

    @Test
    void testMoveCommand() throws NoSuchRoomException {
        Adventurer coward = characterFactory.createCoward("Coward");
        Creature dragon = characterFactory.createCreature("Dragon");
        Maze twoRoomMaze = Maze.getNewBuilder()
                .createFullyConnectedRooms("initial", "final")
                .addToRoom("initial", coward)
                .addToRoom("initial", dragon)
                .build();

        // Act - the weak knight should fight
        Command moveCommand = coward.getAction();
        moveCommand.execute();

        // Assert – the coward ran to the other room and lost some health doing it
        // since there was a creature in the room.
        assertTrue(twoRoomMaze.getRoom("final").hasLivingAdventurers());
        assertTrue(coward.getHealth() < Character.DEFAULT_INITIAL_HEALTH);

    }
}

package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Maze;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class KnightTest {
    CharacterFactory characterFactory = new CharacterFactory();

    @Test
    void testFighting() {
        // Arrange - put creature in room with two adventurers
        Double lowHealth = 2.0;
        Adventurer strongMan = characterFactory.createAdventurer("StrongMan");
        Adventurer weakKnight = characterFactory.createKnight("WeakKnight");
        Creature creature = characterFactory.createCreature("Ogre");
        Maze.getNewBuilder()
                .createRoom("only room")
                .addAdventurers(strongMan)
                .addAdventurers(weakKnight)
                .addCreatures(creature)
                .build();

        // Act - the weak knight should fight
        Command command = weakKnight.getAction();
        command.execute();

        // Assert – the fight did occur and changed the health of both combatants
        assertNotEquals(lowHealth, weakKnight.getHealth());
        assertNotEquals(Creature.DEFAULT_INITIAL_HEALTH, creature.getHealth());
    }
}

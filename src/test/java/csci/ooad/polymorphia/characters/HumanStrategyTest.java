package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Maze;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class HumanStrategyTest {
    CharacterFactory characterFactory = new CharacterFactory();

    // TODO: this is for a future assignment
    @Disabled
    void testOptionSelection() {
        Adventurer human = characterFactory.createHuman("Human");
        Adventurer knight = characterFactory.createKnight("Knight");
        Creature creature = characterFactory.createCreature("Ogre");
        Maze.getNewBuilder()
                .createRoom("only room")
                .addAdventurers(knight, human)
                .addCreatures(creature)
                .createAndAddFoodItems(2)
                .build();


        human.getAction().execute();
    }
}
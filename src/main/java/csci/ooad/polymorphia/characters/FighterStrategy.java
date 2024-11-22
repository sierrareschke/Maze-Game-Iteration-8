package csci.ooad.polymorphia.characters;


import csci.ooad.polymorphia.Armor;
import csci.ooad.polymorphia.Maze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FighterStrategy extends BaseStrategy {
    private static final Logger logger = LoggerFactory.getLogger(FighterStrategy.class);

    @Override
    public Command generateCommand(Character character) {
        Maze.Room currentRoom = character.getCurrentLocation();

        if (currentRoom.hasArmor()) {
            Armor armor = currentRoom.selectRandomArmor();
            return new WearCommand(character, armor);
        }

        return super.generateCommand(character);
    }

    @Override
    Boolean shouldFight(Character character) {
        return character.creatureInRoomWithMe();
    }
}

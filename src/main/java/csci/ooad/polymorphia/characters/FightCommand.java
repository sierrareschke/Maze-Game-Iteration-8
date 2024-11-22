package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Die;
import csci.ooad.polymorphia.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static csci.ooad.polymorphia.EventBus.post;

public class FightCommand extends PolymorphiaCommand {
    private static final Logger logger = LoggerFactory.getLogger(FightCommand.class);

    Character opponent;

    public FightCommand(Character character, Character opponent) {
        super(character, "FIGHT");
        this.opponent = opponent;
    }

    @Override
    public void execute() {
        Integer adventurerRoll = Die.rollSixSided();
        Integer creatureRoll = Die.rollSixSided();
        logger.info("{} is fighting {}", character.getName(), opponent);

        logger.info("{} rolled {}", character.getName(), adventurerRoll);
        logger.info("{} rolled {}", opponent, creatureRoll);

        if (adventurerRoll > creatureRoll) {
            post(EventType.FightOutcome, character.getName() + " won a battle against " + opponent.getName());
            opponent.loseFightDamage(adventurerRoll - creatureRoll);
        } else if (creatureRoll > adventurerRoll) {
            post(EventType.FightOutcome, opponent.getName() + " won a battle against " + character.getName());
            character.loseFightDamage(creatureRoll - adventurerRoll);
        } else {
            post(EventType.FightOutcome, character.getName() + " tied a battle against " + opponent.getName());
        }

        character.loseHealthForFighting();
        opponent.loseHealthForFighting();
    }
}

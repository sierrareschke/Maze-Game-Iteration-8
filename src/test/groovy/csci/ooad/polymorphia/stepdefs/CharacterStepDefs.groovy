package csci.ooad.polymorphia.stepdefs

import csci.ooad.polymorphia.characters.Character
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertTrue

class CharacterStepDefs {
    World world

    CharacterStepDefs(World aWorld) {
        world = aWorld
    }

    @Given(/^a ([^\s]*) "([^"]*)"$/)
    void createANamedCharacter(String characterType, String name) {
        String methodName = "create${characterType.capitalize()}"
        Character character = world.characterFactory."${methodName}"(name)
        world.addCharacter(character)
    }

    @Then(/^"([^"]*)" (lost|did not lose) some health$/)
    characterLostHealth(String characterName, String lostOrNot) {
        Character character = world.characters[characterName]
        boolean characterLostHealth = character.getHealth() < character.class.DEFAULT_INITIAL_HEALTH
        if (lostOrNot == "lost") {
            assertTrue(characterLostHealth)
        } else {
            assertFalse(characterLostHealth)
        }
    }

    @When("{string} executes his turn")
    void executeTurn(String characterName) {
        world.characters[characterName].doAction()
    }

    @And("{string} gained some health")
    void gainedSomeHealth(String characterName) {
        Character character = world.characters[characterName]
        assertTrue(character.getHealth() > character.class.DEFAULT_INITIAL_HEALTH)
    }
}

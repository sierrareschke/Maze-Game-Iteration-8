package csci.ooad.polymorphia.stepdefs

import csci.ooad.polymorphia.EventType
import csci.ooad.polymorphia.Maze
import csci.ooad.polymorphia.Polymorphia
import csci.ooad.polymorphia.observer.TestObserver
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertFalse

class PolymorphiaStepDefs {

    World world


    PolymorphiaStepDefs(World aWorld) {
        world = aWorld
    }

    @Before
    void setUp() {
        world.attachObserver(EventType.FightOutcome, new TestObserver())
    }

    @When("the game is played in the created maze")
    void iPlayTheGame() {
        Maze maze = world.builder.build()
        world.polymorphia = new Polymorphia(maze)
        world.polymorphia.play()
    }

    @Then("I should be told that either all the adventurers or all of the creatures have died")
    void iShouldBeToldThatEitherAllTheAdventurersOrAllOfTheCreaturesHaveDied() {
        !world.polymorphia.hasLivingAdventurers() || !world.polymorphia.hasLivingCreatures()
    }

    @Then("the game should be over")
    void theGameShouldBeOver() {
        println(world.polymorphia)
        assertTrue(world.polymorphia.isOver())
    }

    @Then(/^a fight did take place$/)
    void aFightTookPlace() {
        assertTrue(world.eventReceived(EventType.FightOutcome))
    }

    @Then(/^a fight did not take place$/)
    void aFightDoesNotTakePlace() {
        assertFalse(world.eventReceived(EventType.FightOutcome))
    }
}

package csci.ooad.polymorphia.stepdefs

import csci.ooad.polymorphia.Maze

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import static org.junit.jupiter.api.Assertions.assertEquals


class RoomStepDefs {
    World world
    RoomStepDefs(World aWorld) {
        world = aWorld
    }

    @Given(/^a room named "([^"]*)"$/)
    void createAndSaveRoom(String roomName) {
        world.addRoom(roomName, new Maze.Room(roomName))
    }

    @And("all characters are in room {string}")
    void allCharactersAreInRoom(String roomName) {
        world.characters.values().each { character ->
            world.builder.addToRoom(roomName, character)
        }
    }

    @And("room {string} has {int} food items")
    void roomHasFoodItems(String roomName, int numberOfFoodItems) {
        Maze.Room room = world.builder.maze.getRoom(roomName)
        assertEquals(numberOfFoodItems, room.getFoodItems().size())
    }

    @And("{string} is in room {string}")
    void isInRoom(String characterName, String roomName) {
        assertEquals(roomName, world.characters[characterName].getCurrentLocation().getName())
    }

}

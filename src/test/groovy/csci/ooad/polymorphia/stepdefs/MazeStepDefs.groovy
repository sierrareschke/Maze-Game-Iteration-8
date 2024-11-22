package csci.ooad.polymorphia.stepdefs

import csci.ooad.polymorphia.ArtifactFactory
import csci.ooad.polymorphia.Food
import io.cucumber.java.en.And
import io.cucumber.java.en.Given

class MazeStepDefs {
    static Map<String, String> featureFileAttributesToBuilderMethods = [
        "number of rooms": "createFullyConnectedRooms",
        "number of adventurers": "createAndAddAdventurers",
        "number of knights": "createAndAddKnights",
        "number of gluttons": "createAndAddKnights",
        "number of cowards": "createAndAddCowards",
        "number of creatures": "createAndAddCreatures",
        "number of food items": "createAndAddFoodItems",
        "number of demons": "createAndAddAdventurers",
    ]

    World world
    ArtifactFactory artifactFactory

    MazeStepDefs(World aWorld, ArtifactFactory artifactFactory) {
        world = aWorld
        this.artifactFactory = artifactFactory
    }

    @Given("a maze with the following attributes:")
    void iHaveAGameWithTheFollowingAttributes(Map<String, Integer> gameAttributes) {
        world.builder.createAndAddFoodItems()

        gameAttributes.each { key, value ->
            if (featureFileAttributesToBuilderMethods.containsKey(key)) {
                world.builder."${featureFileAttributesToBuilderMethods[key]}"(value)
            }
        }
    }

    @Given(/^a maze with the following rooms?:$/)
    void aMazeWithTheFollowingRooms(List<String> roomNames) {
        world.builder.createFullyConnectedRooms(roomNames.toArray(new String[0]))
    }

    @And("food {string} is placed in room {string}")
    void foodIsPlacedInRoom(String foodName, String roomName) {
        world.builder.addToRoom(roomName, artifactFactory.createFood(foodName))
    }
}

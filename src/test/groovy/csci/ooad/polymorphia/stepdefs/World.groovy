package csci.ooad.polymorphia.stepdefs

import csci.ooad.polymorphia.ArtifactFactory
import csci.ooad.polymorphia.EventBus
import csci.ooad.polymorphia.EventType
import csci.ooad.polymorphia.Maze
import csci.ooad.polymorphia.Polymorphia

import csci.ooad.polymorphia.characters.Character
import csci.ooad.polymorphia.characters.CharacterFactory
import csci.ooad.polymorphia.observer.TestObserver
import io.cucumber.java.After


class World {
    Polymorphia polymorphia
    CharacterFactory characterFactory = new CharacterFactory()
    Maze.Builder builder = Maze.getNewBuilder(characterFactory, new ArtifactFactory())
    Map<EventType, TestObserver> polymorphiaObservers = new HashMap<>()
    Map<String, Character> characters = new HashMap<>()

    @After
    void tearDown() {
        polymorphiaObservers.values().each { observer ->
            EventBus.getInstance().detach(observer)
        }
    }

    boolean eventReceived(EventType eventType) {
        return polymorphiaObservers.get(eventType).numberOfEventsReceived() > 0
    }

    void addCharacter(Character character) {
        characters.put(character.getName(), character)
    }

    void addRoom(String name, Maze.Room room) {
        rooms.put(name, room)
    }

    void attachObserver(EventType eventType, TestObserver testObserver) {
        EventBus.getInstance().attach(testObserver, eventType)
        polymorphiaObservers.put(eventType, testObserver)
    }
}
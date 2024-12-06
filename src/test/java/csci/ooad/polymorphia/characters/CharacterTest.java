package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {
    CommandFactory commandFactory = new CommandFactory();
    ArtifactFactory artifactFactory = new ArtifactFactory();
    CharacterFactory characterFactory = new CharacterFactory();

    Adventurer knight;

    @BeforeEach
    void setUp() {
        knight = characterFactory.createKnight("Knight");
    }

    @Test
    void testToString() {
        System.out.println(knight);

        assertTrue(knight.toString().contains("Knight"));
    }

    @Test
    void isAlive() {
        assertTrue(knight.isAlive());
    }

    @Test
    void testSorting() {
        List<Character> characters = new ArrayList<>(Arrays.asList(
                characterFactory.createKnight("Frodo"),
                characterFactory.createCreature("Ogre"),
                characterFactory.createKnight("Arwen")));

        Collections.sort(characters);

        System.out.println(characters);
    }

    @Test
    void testLoseHealthAndDeath() {
        double healthPointsToLose = 3.0;

        knight.loseFightDamage(healthPointsToLose);
        assertEquals(CharacterFactory.KNIGHT_INITIAL_HEALTH - healthPointsToLose, knight.getHealth());

        knight.loseFightDamage(knight.getHealth());
        assertFalse(knight.isAlive());
    }

    @Test
    void testCreateAdventurerWithHealth() {
        List<Adventurer> adventurer = characterFactory.createNumberOfAdventurers(1, 10.0);
        assertEquals(adventurer.size(), 1);
        assertEquals(adventurer.getFirst().getHealth(), 10);
    }

    @Test
    void testFightingMandatoryLossOfHealth() {
        Creature ogre = characterFactory.createCreature("Ogre");
        Command command = commandFactory.createFightCommand(knight, ogre);
        command.execute();
        System.out.println("After the fight, Joe's health is: " + knight.getHealth());

        // Joe should have lost 0.5 health and he started with a integer health value
        // of 5.0. After the fight he should have 4.5 health. Or 3.5, or 2.5, etc. depending
        // upon the outcome of the fight. So, we just check to make sure the health is x.5
        assertEquals(0.5, knight.getHealth() % 1);
    }

    @Test
    void testMovingRoomLossOfHealth() throws NoSuchRoomException {
        Maze maze = Maze.getNewBuilder()
                .createFullyConnectedRooms("mainRoom", "neighbor")
                .addToRoom("mainRoom", knight)
                .build();
        Maze.Room neighbor = maze.getRoom("neighbor");

        // Since nothing is in the current room with Joe, he should move to the new room
        knight.getAction().execute();

        System.out.println("After moving rooms, Joe's health is: " + knight.getHealth());
        assertEquals(CharacterFactory.KNIGHT_INITIAL_HEALTH - Character.HEALTH_LOST_IN_MOVING_ROOMS, knight.getHealth());
        assertEquals(neighbor, knight.getCurrentLocation());
    }

    @Test
    void testMovingWithNoNeighbors() {
        Maze.getNewBuilder()
                .createFullyConnectedRooms("onlyRoom")
                .addToRoom("onlyRoom", knight)
                .build();

        // Act -- no error occurs
        knight.getAction().execute();
    }

    @Test
    void testEatingFood() throws NoSuchRoomException {
        Food popcorn = artifactFactory.createFood("popcorn");

        Maze maze = Maze.getNewBuilder()
                .createFullyConnectedRooms("onlyRoom")
                .addToRoom("onlyRoom", knight)
                .addToRoom("onlyRoom", popcorn)
                .build();

        Maze.Room room = maze.getRoom("onlyRoom");

        knight.getAction().execute();

        assertEquals(knight.getHealth(), CharacterFactory.KNIGHT_INITIAL_HEALTH + popcorn.healthValue() );
        assertFalse(room.hasFood());
    }

    @Test
    void testFighting() {
        Adventurer adventurer = characterFactory.createAdventurer("Adventurer");
        Creature creature = characterFactory.createCreature("Creature");

        double initialHealth = adventurer.getHealth();
        Command command = commandFactory.createFightCommand(adventurer, creature);
        command.execute();

        assertNotEquals(initialHealth, adventurer.getHealth());
    }

    @Test
    void testCreatureDoesNotDoAction() {
        Creature creature = characterFactory.createCreature("Creature");
        creature.getAction().execute();
    }

    @Test
    void testIsHealthiestInRoom() throws NoSuchRoomException {
        Adventurer ted = characterFactory.createAdventurer("Ted");
        Adventurer charlie = characterFactory.createAdventurer("Charlie");
        Armor steelArmor = artifactFactory.createArmorSuit("Steel Armor");
        Maze maze = Maze.getNewBuilder()
                .createFullyConnectedRooms("onlyRoom")
                .addToRoom("onlyRoom", ted)
                .addToRoom("onlyRoom", charlie)
                .addToRoom("onlyRoom", steelArmor)
                .build();
        Command command = commandFactory.createDoNothingCommand();
        command.execute();
        System.out.println("Command" + command);
        assertNotNull(command);

        // Test armor
        Command wearArmor = commandFactory.wearArmor(ted, steelArmor);
        wearArmor.execute();
        Adventurer armoredTed = maze.getRoom("onlyRoom").getLivingAdventurers().getFirst();

        boolean hasArmor = maze.getRoom("onlyRoom").hasArmor();
        assertFalse(hasArmor);


        charlie.loseFightDamage(2);
        assertFalse(ted.iAmHealthiestInRoom());
    }

}
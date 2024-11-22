package csci.ooad.polymorphia.server.controllers;

public record PolymorphiaParameters(
        String name, String playerName, int numRooms, int numAdventurers, int numCreatures,
        int numKnights, int numCowards, int numGluttons, int numDemons, int numFood, int numArmor) {
}

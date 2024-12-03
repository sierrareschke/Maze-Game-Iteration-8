package csci.ooad.polymorphia.server.controllers;

import csci.ooad.polymorphia.Maze.Room;
import csci.ooad.polymorphia.Polymorphia;
import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Character;
import csci.ooad.polymorphia.characters.Creature;
import csci.ooad.polymorphia.characters.PolymorphiaCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PolymorphiaJsonAdaptor {
    public String name;
    public int turn;
    public boolean inMiddleOfTurn;
    public boolean gameOver;
    public String statusMessage;
    public List<String> livingAdventurers;
    public List<String> livingCreatures;
    public List<Map<String,Object>> rooms;
    public List<String> availableCommands;


    public PolymorphiaJsonAdaptor(String gameName, Polymorphia polymorphia) {
        name = gameName;
        turn = polymorphia.getTurnNumber();

        // TODO: COMEBACK AND FIX THIS
        inMiddleOfTurn = false;

        gameOver = polymorphia.isOver();

        // TODO: COME BACK AND FIX THIS
        statusMessage = "Status message not implemented";

        livingAdventurers = polymorphia.getLivingAdventurers()
                .stream()
                .map(Adventurer::toString)
                .collect(Collectors.toList());

        livingCreatures = polymorphia.getLivingCreatures()
                .stream()
                .map(Creature::toString)
                .collect(Collectors.toList());

        rooms = polymorphia.getThisMaze().getRooms()
                .stream()
                .map(room -> Map.of(
                        "name", room.getName(),
                        "neighbors", room.getNeighbors().stream().map(Room::getName).toList(),
                        "contents", room.getContents()
                ))
                .collect(Collectors.toList());

        availableCommands = new ArrayList<>();
    }

}

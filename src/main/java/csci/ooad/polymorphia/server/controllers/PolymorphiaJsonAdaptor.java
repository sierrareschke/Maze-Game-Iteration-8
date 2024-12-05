package csci.ooad.polymorphia.server.controllers;

import csci.ooad.polymorphia.Maze.Room;
import csci.ooad.polymorphia.Polymorphia;
import csci.ooad.polymorphia.characters.*;
import csci.ooad.polymorphia.characters.Character;

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

        inMiddleOfTurn = polymorphia.isInMiddleOfTurn();

        gameOver = polymorphia.isOver();

        if(polymorphia.isOver()){
            statusMessage = polymorphia.getEndOfGameStatus();
        }else{
            statusMessage = "Turn " + polymorphia.getTurnNumber();
        }

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

        availableCommands = polymorphia.getLivingCharacters().stream()
                .filter(character -> character.getStrategy() instanceof HumanStrategy) // Filter human players
                .flatMap(character -> {
                    HumanStrategy strategy = (HumanStrategy) character.getStrategy();
                    List<HumanStrategy.CommandOption> options = strategy.availableCommandOptions(character);
                    return options.stream().map(Enum::name); // Convert to string names
                })
                .distinct()
                .collect(Collectors.toList());
    }

}

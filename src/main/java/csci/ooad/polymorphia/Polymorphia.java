package csci.ooad.polymorphia;

import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.IMazeObserver;
import csci.ooad.layout.intf.IMazeSubject;
import csci.ooad.polymorphia.characters.*;
import csci.ooad.polymorphia.characters.Character;
import csci.ooad.polymorphia.observer.MazeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class Polymorphia implements IMazeSubject, IObservable {
    private static final Logger logger = LoggerFactory.getLogger(Polymorphia.class);
    private static int gameNumber = 1;

    List<IMazeObserver> observers = new ArrayList<>();

    public void attach(IMazeObserver observer) {
        observers.add(observer);
    }

    @Override
    public List<IMazeObserver> getObservers() {
        return observers;
    }

    private final String name;
    private final Maze maze;
    private Integer turnCount = 0;
    private final Random rand = new Random();
    private String selectedOption;
    private boolean inMiddleOfTurn = false;

    public Polymorphia(Maze maze) {
        this("Polymorphia Game " + gameNumber, maze);
    }

    public Polymorphia(String name, Maze maze) {
        this.name = name;
        this.maze = maze;
        gameNumber++;
    }

    String getName() {
        return name;
    }

    public boolean isInMiddleOfTurn(){return this.inMiddleOfTurn;}

    @Override
    public String toString() {
        return String.join("\n", status()) + "\n" + maze.toString();
    }

    private List<String> status() {
        return List.of(
                "Polymorphia " + getName() + " after turn " + turnCount,
                "Live adventurers(" + numberOfLivingAdventurers() + "): " +
                        getLivingAdventurers().stream().map(Character::getName).collect(Collectors.joining(",")),
                "Live creatures(" + numberOfLivingCreatures() + "): " +
                        getLivingCreatures().stream().map(Character::getName).collect(Collectors.joining(","))
        );
    }

    public int numberOfLivingAdventurers() {
        return getLivingAdventurers().size();
    }

    public List<Adventurer> getLivingAdventurers() {
        return maze.getLivingAdventurers();
    }

    public int numberOfLivingCreatures() {
        return getLivingCreatures().size();
    }

    // Game is over when all creatures are killed or all adventurers are killed
    public Boolean isOver() {
        return !hasLivingCreatures() || !hasLivingAdventurers();
    }

    public Boolean hasLivingCreatures() {
        return maze.hasLivingCreatures();
    }

    public Boolean hasLivingAdventurers() {
        return maze.hasLivingAdventurers();
    }

    public void setSelectedOption(String selectedCommand){
        this.selectedOption = selectedCommand;
    }

    public void playTurn() {
        if (isOver()) {
            return;
        }

        if (turnCount == 0) {
            logger.info("Starting play...");
        }



        logger.info("play turn called");
        if(!this.inMiddleOfTurn){
            turnCount += 1;
        }


        // Process all the characters in random order
        logger.info("Starting turn " + turnCount + "...");
        List<Character> remainingCharactersToExecuteTurn = getLivingCharacters();

        for(Character character: remainingCharactersToExecuteTurn){
            character.setTurnPending(true);
        }

        while (!remainingCharactersToExecuteTurn.isEmpty()) {
            int index = rand.nextInt(remainingCharactersToExecuteTurn.size());
            Character currentPlayer = remainingCharactersToExecuteTurn.get(index);

            // Make sure currentPlayer is still alive. It might have fought a Demon
            if (currentPlayer.isAlive()) {

                Command command;

                if(currentPlayer instanceof APIPlayer){
                    if(selectedOption.equals("NULL")){
                        logger.info("current player is API player and selected option is NULL");
                        inMiddleOfTurn = true;
                        return;
                    }
                    APIPlayer apiPlayer = (APIPlayer) currentPlayer;
                    HumanStrategy humanStrategy = (HumanStrategy) apiPlayer.getStrategy();
                    command = humanStrategy.generateSelectedCommand(apiPlayer,selectedOption);
                }else {
                    command = currentPlayer.getAction();
                }
                command.execute();
                currentPlayer.setTurnPending(false);
                logger.info("Turn " + turnCount + ": " + currentPlayer.getName() + " executed " + command.getName());
            }
            remainingCharactersToExecuteTurn.remove(currentPlayer);
            notifyObservers(status());
        }

        this.inMiddleOfTurn = false;
        this.selectedOption = null;
    }

    public List<Character> getLivingCharacters() {
        return maze.getLivingCharacters();
    }

    public void postMessage(EventType eventType, String eventDescription) {
        logger.info(eventDescription);
        EventBus.getInstance().postMessage(eventType, eventDescription);
    }

    public void play() {
        while (!isOver()) {
            logger.info(this.toString());
            playTurn();
        }
        postMessage(EventType.GameStart, getEndOfGameStatus());
    }

    public String getEndOfGameStatus() {
        String eventDescription = "The game ended after " + turnCount + " turns.\n";
        if (hasLivingAdventurers()) {
            eventDescription += "The adventurers won! Left standing are:\n" + getAdventurerNames() + "\n";
        } else if (hasLivingCreatures()) {
            eventDescription += "The creatures won! Left standing are:\n" + getCreatureNames() + "\n";
        } else {
            eventDescription += "No team won! Everyone died!\n";
        }
        return eventDescription;
    }

    String getAdventurerNames() {
        return String.join("\n ", getLivingCharacters().stream().map(Object::toString).toList());
    }

    String getCreatureNames() {
        return String.join("\n ", getLivingCreatures().stream().map(Object::toString).toList());
    }

    public List<Creature> getLivingCreatures() {
        return maze.getLivingCreatures();
    }

    public Character getWinner() {
        if (!isOver() || !hasLivingCharacters()) {
            // No one has won yet or no one won -- all died
            return null;
        }
        return getLivingCharacters().getFirst();
    }

    private boolean hasLivingCharacters() {
        return !getLivingCharacters().isEmpty();
    }

    @Override
    public IMaze getMaze() {
        return new MazeAdapter(maze);
    }

    public Maze getThisMaze() {return this.maze;}

    public int getTurnNumber() {
        return turnCount;
    }

}
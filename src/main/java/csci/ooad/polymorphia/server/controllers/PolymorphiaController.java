package csci.ooad.polymorphia.server.controllers;

import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.Polymorphia;
import csci.ooad.polymorphia.characters.Adventurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PolymorphiaController {
    private static final Logger logger = LoggerFactory.getLogger(PolymorphiaController.class);
    private static int sameName = 1;

    // TODO: UNSURE ABOUT THIS
    private Map<String,Polymorphia> games = new HashMap<>();

    public PolymorphiaController() {
        // Create a default game
        logger.info("Initializing default game...");
        Maze.Builder mazeBuilder = Maze.getNewBuilder()
                .createFullyConnectedRooms(4)
                .createAndAddAdventurers(2)
                .createAndAddCreatures(2)
                .createAndAddKnights(1)
                .createAndAddCowards(1)
                .createAndAddGluttons(0)
                .createAndAddDemons(1)
                .createAndAddFoodItems(3)
                .createAndAddArmor(1);

        Maze maze = mazeBuilder.build();
        Polymorphia defaultGame = new Polymorphia("DefaultGame", maze);

        // Add to games map
        games.put("DefaultGame", defaultGame);

        logger.info("Default game initialized: {}", defaultGame);
    }


    @GetMapping("/api/games")
    public ResponseEntity<?> getGames() {
        List<String> gameNames = new ArrayList<>(games.keySet());
        logger.info("Current games: {}", games.keySet());
        return new ResponseEntity<>(gameNames, HttpStatus.OK);
    }

    @GetMapping("/api/game/{gameId}")
    public ResponseEntity<?> getGame(@PathVariable(name = "gameId", required = false) String gameId) {

        if (games.containsKey(gameId.trim())) { // Trim spaces to avoid input issues
            Polymorphia foundGame = games.get(gameId.trim());
            PolymorphiaJsonAdaptor adaptor = new PolymorphiaJsonAdaptor(gameId, foundGame);
            return new ResponseEntity<>(adaptor, HttpStatus.OK); // Use OK for GET
        }

        logger.error("Game not found");
        return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/api/game/create")
    public ResponseEntity<?> createGame(@Validated @RequestBody PolymorphiaParameters params) {
        logger.info("Received request to create a game with parameters: {}", params);

        String gameName = params.name();

        if(gameName == null) {
            return new ResponseEntity<>("Game name cannot be null!",HttpStatus.BAD_REQUEST);
        }

        if(games.containsKey(gameName)) {
            return new ResponseEntity<>("Game with this name already exists!",HttpStatus.CONFLICT);
        }

        Maze.Builder mazeBuilder = Maze.getNewBuilder()
                .createFullyConnectedRooms(params.numRooms())
                .createAndAddAdventurers(params.numCreatures())
                .createAndAddCreatures(params.numCreatures())
                .createAndAddKnights(params.numKnights())
                .createAndAddCowards(params.numCowards())
                .createAndAddGluttons(params.numGluttons())
                .createAndAddDemons(params.numDemons())
                .createAndAddFoodItems(params.numFood())
                .createAndAddAPIPlayer(params.playerName())
                .createAndAddArmor(params.numArmor());

        Maze maze = mazeBuilder.build();

        Polymorphia newGame = new Polymorphia(gameName,maze);

        games.put(gameName,newGame);

        logger.info("Created new game: {}", gameName);

        PolymorphiaJsonAdaptor adaptor = new PolymorphiaJsonAdaptor(gameName,newGame);

        return new ResponseEntity<>(adaptor, HttpStatus.CREATED);
    }

    @PutMapping("/api/game/{gameId}/playTurn/{command}")
    public ResponseEntity<?> playTurn(@PathVariable(name = "gameId") String gameId, @PathVariable(name = "command") String command) {
        logger.info("Received request to play turn with command: {}", command);
        try {
            Polymorphia foundGame = games.get(gameId.trim());
            foundGame.setSelectedOption(command);
            logger.info("selected option is " + command);
            if(foundGame.isInMiddleOfTurn() && command.equals("NULL")){
                logger.info("awaiting API player response");
            } else {
                foundGame.playTurn();
            }

            PolymorphiaJsonAdaptor adaptor = new PolymorphiaJsonAdaptor(gameId, foundGame);
            return new ResponseEntity<>(adaptor, HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
        }
    }

}

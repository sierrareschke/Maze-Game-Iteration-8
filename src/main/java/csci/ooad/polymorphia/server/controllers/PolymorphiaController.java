package csci.ooad.polymorphia.server.controllers;

import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.Polymorphia;
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

    // TODO: UNSURE ABOUT THIS
    private Map<String,Polymorphia> games = new HashMap<>();

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

        if(games.containsKey(gameName)) {
            return new ResponseEntity<>("Game with this name already exists!",HttpStatus.CONFLICT);
        }

        Maze.Builder mazeBuilder = Maze.getNewBuilder()
                .createConnectedRooms(1, params.numRooms())
                .createAndAddAdventurers(params.numCreatures())
                .createAndAddCreatures(params.numCreatures())
                .createAndAddKnights(params.numKnights())
                .createAndAddCowards(params.numCowards())
                .createAndAddGluttons(params.numGluttons())
                .createAndAddDemons(params.numDemons())
                .createAndAddFoodItems(params.numFood())
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
        return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
    }

}

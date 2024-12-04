package csci.ooad.polymorphia.server.controllers;

import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.Polymorphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;

@RestController
public class PolymorphiaController {
    private static final Logger logger = LoggerFactory.getLogger(PolymorphiaController.class);
    private ArrayList<Polymorphia> games = new ArrayList<>();

    public PolymorphiaController() {
        // Create a default game here
        Maze twoByTwoMaze = Maze.getNewBuilder()
                .create2x2Grid()
                .createAndAddAdventurers("Frodo")
                .createAndAddCreatures("Ogre")
                .build();
        Polymorphia game = new Polymorphia("Default Game", twoByTwoMaze);
        games.add(game);
    }

    @GetMapping("/api/games")
    public ResponseEntity<?> getGames() {
        return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.OK);
    }

    @GetMapping("/api/game/{gameId}")
    public ResponseEntity<?> getGame(@PathVariable(name = "gameId", required = false) String gameId) {
        return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/api/game/create")
    public ResponseEntity<?> createGame(@Validated @RequestBody PolymorphiaParameters params) {
        logger.info("Received request to create a game with parameters: {}", params);
        try {
            Maze newMaze = Maze.getNewBuilder()
                    .createFullyConnectedRooms(params.numRooms())
                    .createAndAddAdventurers(params.numAdventurers())
                    .createAndAddCreatures(params.numCreatures())
                    .createAndAddKnights(params.numKnights())
                    .createAndAddCowards(params.numCowards())
                    .createAndAddGluttons(params.numGluttons())
                    .createAndAddDemons(params.numDemons())
                    .createAndAddFoodItems(params.numFood())
                    .createAndAddArmor(params.numArmor())
                    .build();

            Polymorphia game = new Polymorphia(params.name(), newMaze);
            games.add(game);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception e) {
            // Log the exception for debugging purposes
            logger.error("Error occurred while creating the game", e);
            return new ResponseEntity<>("Failed to create game", HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @PutMapping("/api/game/{gameId}/playTurn/{command}")
    public ResponseEntity<?> playTurn(@PathVariable(name = "gameId") String gameId, @PathVariable(name = "command") String command) {
        return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
    }

}

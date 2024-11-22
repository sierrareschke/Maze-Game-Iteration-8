package csci.ooad.polymorphia.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
public class PolymorphiaController {
    private static final Logger logger = LoggerFactory.getLogger(PolymorphiaController.class);

    public PolymorphiaController() {
        // TODO: Create a default game here
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
        return new ResponseEntity<>("Failed to create game", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PutMapping("/api/game/{gameId}/playTurn/{command}")
    public ResponseEntity<?> playTurn(@PathVariable(name = "gameId") String gameId, @PathVariable(name = "command") String command) {
        return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
    }

}

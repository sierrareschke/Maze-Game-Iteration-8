package csci.ooad.polymorphia.server.controllers;

import csci.ooad.polymorphia.server.PolymorphiaServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {PolymorphiaServerApplication.class, PolymorphiaController.class})
class PolymorphiaControllerTest {
    static String DEFAULT_GAME_ID = "MyGame";

    PolymorphiaController polymorphiaController;

    @Autowired
    public PolymorphiaControllerTest(PolymorphiaController polymorphiaController) {
        this.polymorphiaController = polymorphiaController;
    }

    @Test
    public void contextLoads() {
    }


    @Test
    void getGames() {
        ResponseEntity<?> response = polymorphiaController.getGames();
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        // Assert that the returned body is a list of game names
        assertNotNull(response.getBody());
        List<?> gameNames = (List<?>) response.getBody();
        assertFalse(gameNames.isEmpty(), "Game list should not be empty");
        assertTrue(gameNames.contains("DefaultGame"), "DefaultGame should be present in the list of games");
    }

    @Test
    void createGame() {
        String playerName = "Professor";
        String newGameName = "NewGame";
        ResponseEntity<?> getGamesResponseInitial = polymorphiaController.getGames();

        PolymorphiaParameters arcaneParameters = new PolymorphiaParameters(newGameName, playerName,
                2, 2, 7, 1,
                2, 2, 2, 10, 2);
        ResponseEntity<?> response = polymorphiaController.createGame(arcaneParameters);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());

        PolymorphiaJsonAdaptor jsonAdaptor = (PolymorphiaJsonAdaptor) response.getBody();
        assert jsonAdaptor != null;

        assertEquals(newGameName, jsonAdaptor.getName(), "Game ID should match the one provided");

        // Try to create duplicate game
        ResponseEntity<?> secondResponse = polymorphiaController.createGame(arcaneParameters);
        assertEquals(HttpStatusCode.valueOf(409), secondResponse.getStatusCode(), "Trying to create second game with same name should error");

        ResponseEntity<?> getGamesResponse = polymorphiaController.getGames();
        assertNotNull(getGamesResponse.getBody());
        List<?> gameNames = (List<?>) getGamesResponse.getBody();
        assertTrue(gameNames.contains("NewGame"), "NewGame should be present in the list of games");

        // Test invalid input: null name
        PolymorphiaParameters invalidParameters = new PolymorphiaParameters(null, playerName, 2, 2, 7, 1, 2, 2, 2, 10, 2);
        ResponseEntity<?> invalidResponse = polymorphiaController.createGame(invalidParameters);
        assertEquals(HttpStatusCode.valueOf(400), invalidResponse.getStatusCode(), "Response code should be 400 BAD REQUEST for null game name");
    }

    @Test
    void getGame() {
        String gameName = DEFAULT_GAME_ID;
        String playerName = "getGameTestPlayer";
        PolymorphiaParameters arcaneParameters = new PolymorphiaParameters(gameName, playerName,
                2, 2, 7, 1,
                2, 2, 2, 10, 2);
        ResponseEntity<?> response = polymorphiaController.createGame(arcaneParameters);

        ResponseEntity<?> getGameResponse = polymorphiaController.getGame(DEFAULT_GAME_ID);
        assertEquals(HttpStatusCode.valueOf(200), getGameResponse.getStatusCode());

        PolymorphiaJsonAdaptor jsonAdaptor = (PolymorphiaJsonAdaptor) response.getBody();
        assert jsonAdaptor != null;

        assertEquals(gameName, jsonAdaptor.getName(), "Game ID should match the one created");

        // Test invalid input: non-existent game
        ResponseEntity<?> invalidGameResponse = polymorphiaController.getGame("NonExistentGame");
        assertEquals(HttpStatusCode.valueOf(404), invalidGameResponse.getStatusCode(), "Response code should be 404 NOT FOUND for non-existent game");
    }

    @Test
    void playTurnWithNoHumanPlayer() {
        String gameName = "NoHumanPlayerGame";
        String playerName = "AIPlayer";
        PolymorphiaParameters parameters = new PolymorphiaParameters(gameName, playerName,
                2, 2, 7, 1,
                2, 2, 2, 10, 2);

        // Create the game
        ResponseEntity<?> createResponse = polymorphiaController.createGame(parameters);
        assertEquals(HttpStatusCode.valueOf(201), createResponse.getStatusCode(), "Game creation should succeed");

        // Simulate a turn with no human player and a valid command
        String command = "AUTO_TURN";
        ResponseEntity<?> playTurnResponse = polymorphiaController.playTurn(gameName, command);
        assertEquals(HttpStatusCode.valueOf(200), playTurnResponse.getStatusCode(), "Playing a turn should return 200 OK");

        // Verify the game state
        PolymorphiaJsonAdaptor gameState = (PolymorphiaJsonAdaptor) playTurnResponse.getBody();
        assertNotNull(gameState, "Game state should not be null");
        assertEquals(gameName, gameState.getName(), "Game name should match");
    }

    @Test
    void playTurnWithHumanPlayer() {
        String gameName = "HumanPlayerGame";
        String playerName = "HumanPlayer";
        PolymorphiaParameters parameters = new PolymorphiaParameters(gameName, playerName,
                2, 2, 7, 1,
                2, 2, 2, 10, 2);

        // Create the game
        ResponseEntity<?> createResponse = polymorphiaController.createGame(parameters);
        assertEquals(HttpStatusCode.valueOf(201), createResponse.getStatusCode(), "Game creation should succeed");

        // Simulate a turn with a human player and a valid command
        String command = "HUMAN_MOVE";
        ResponseEntity<?> playTurnResponse = polymorphiaController.playTurn(gameName, command);
        assertEquals(HttpStatusCode.valueOf(200), playTurnResponse.getStatusCode(), "Playing a turn should return 200 OK");

        // Verify the game state
        PolymorphiaJsonAdaptor gameState = (PolymorphiaJsonAdaptor) playTurnResponse.getBody();
        assertNotNull(gameState, "Game state should not be null");
        assertEquals(gameName, gameState.getName(), "Game name should match");

        // Simulate a null command while in the middle of a turn
        String nullCommand = "NULL";
        ResponseEntity<?> nullTurnResponse = polymorphiaController.playTurn(gameName, nullCommand);
        assertEquals(HttpStatusCode.valueOf(200), nullTurnResponse.getStatusCode(), "Null command while in the middle of a turn should return 200 OK");

        // Test for a non-existent game
        String nonExistentGame = "NonExistentGame";
        ResponseEntity<?> invalidGameResponse = polymorphiaController.playTurn(nonExistentGame, command);
        assertEquals(HttpStatusCode.valueOf(404), invalidGameResponse.getStatusCode(), "Non-existent game should return 404 NOT FOUND");
    }

}
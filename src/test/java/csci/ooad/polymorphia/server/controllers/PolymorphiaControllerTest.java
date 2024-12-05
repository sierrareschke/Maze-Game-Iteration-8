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
        // TODO: Add more assertions

        // Assert that the returned body is a list of game names
        assertNotNull(response.getBody());
        List<?> gameNames = (List<?>) response.getBody();
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

        ResponseEntity<?> secondResponse = polymorphiaController.createGame(arcaneParameters);
        assertEquals(HttpStatusCode.valueOf(409), secondResponse.getStatusCode(), "Trying to create second game with same name should error");

        ResponseEntity<?> getGamesResponse = polymorphiaController.getGames();
        assertNotNull(getGamesResponse.getBody());
        List<?> gameNames = (List<?>) getGamesResponse.getBody();
        assertTrue(gameNames.contains("NewGame"), "NewGame should be present in the list of games");
    }
    @Test
    void getGame() {
        String gameName = "getGameTestGameName";
        String playerName = "getGameTestPlayer";
        PolymorphiaParameters arcaneParameters = new PolymorphiaParameters(gameName, playerName,
                2, 2, 7, 1,
                2, 2, 2, 10, 2);
        ResponseEntity<?> response = polymorphiaController.createGame(arcaneParameters);

        ResponseEntity<?> getGameResponse = polymorphiaController.getGame("getGameTestGameName");
        assertEquals(HttpStatusCode.valueOf(200), getGameResponse.getStatusCode());

        PolymorphiaJsonAdaptor jsonAdaptor = (PolymorphiaJsonAdaptor) response.getBody();
        assert jsonAdaptor != null;
        // TODO: Add more assertions
    }

    @Test
    void playTurnWithNoHumanPlayer() {
        // TODO: Implement this test
    }

    @Test
    void playTurnWithHumanPlayer() {
        // TODO: Implement this test
    }

}
package passoffTests.serverTests;

import dataAccess.DataAccess;
import dataAccess.MemoryDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.Service;


import java.util.Collection;

public class ServiceTests {
    private Service service;

    @BeforeEach
    public void setUp() {
        DataAccess dataAccess = new MemoryDataAccess();
        service = new Service(dataAccess);
    }

    @Test
    public void testRegisterSuccessful() {
        UserData userData = new UserData("username", "password", "test@test.com");
        try {
            AuthData authData = service.register(userData);
            Assertions.assertNotNull(authData);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterUnsuccessful() {
        UserData userData = new UserData(null, "password", "test@test.com");
        try {
            service.register(userData);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(400, e.StatusCode());
        }
    }


    @Test
    public void testLoginSuccessful() throws ResponseException {
        UserData userData = new UserData("username", "password", "test@test.com");
        // Register the user first
        service.register(userData);
        try {
            AuthData authData = service.login(userData);
            Assertions.assertNotNull(authData);
            // Add more assertions if needed
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testLoginUnsuccessful() throws ResponseException {
        UserData userData = new UserData(null, "password", "test@test.com");
        try {
            service.register(userData);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(400, e.StatusCode());
        }
    }


    @Test
    public void testLogoutSuccessful() throws ResponseException {
        UserData userData = new UserData("username", "password", "test@test.com");
        AuthData authData = service.register(userData);
        String authToken = authData.getAuthToken();
        try {
            service.logout(authToken);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testLogoutUnsuccessful() throws ResponseException {
        String authToken = "invalidToken";
        try {
            service.logout(authToken);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(401, e.StatusCode());
        }
    }

    @Test
    public void testListGamesSuccessful() throws ResponseException {
        UserData userData = new UserData("username", "password", "test@test.com");
        AuthData authData = service.register(userData);
        String authToken = authData.getAuthToken();
        service.createGame(authToken, "gameName");
        try {
            Collection<GameData> games = service.listGames(authToken);
            Assertions.assertNotNull(games);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testListGamesUnsuccessful() throws ResponseException {
        UserData userData = new UserData("username", "password", "test@test.com");
        AuthData authData = service.register(userData);
        String authToken = authData.getAuthToken();
        service.createGame(authToken, "gameName");
        try {
            Collection<GameData> games = service.listGames(null);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(400, e.StatusCode());
        }
    }

    @Test
    public void testCreateGameSuccessful() throws ResponseException {
        UserData userData = new UserData("username", "password", "test@test.com");
        AuthData authData = service.register(userData);
        String authToken = authData.getAuthToken();
        try {
            int gameData = service.createGame(authToken, "gameName");
            Assertions.assertNotNull(gameData);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateGameUnsuccessful() throws ResponseException {
        UserData userData = new UserData("username", "password", "test@test.com");
        AuthData authData = service.register(userData);
        String authToken = authData.getAuthToken();
        try {
            int gameData = service.createGame(authToken, null);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(400, e.StatusCode());
        }
    }


    @Test
    public void testJoinGameSuccessful() throws ResponseException {
        UserData userData = new UserData("username", "password", "test@test.com");
        AuthData authData = service.register(userData);
        String authToken = authData.getAuthToken();
        service.createGame(authToken, "gameName");
        String clientColor = "WHITE";
        int gameID = 1;
        try {
            service.joinGame(clientColor, gameID, authToken);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

//    @Test
//    public void testJoinGameWatcherSuccessful() throws ResponseException {
//        UserData userData = new UserData("username", "password", "test@test.com");
//        AuthData authData = service.register(userData);
//        String authToken = authData.getAuthToken();
//        service.createGame(authToken, "gameName");
//        String clientColor = null;
//        int gameID = 1;
//        try {
//            service.joinGame(clientColor, gameID, authToken);
//        } catch (ResponseException e) {
//            Assertions.fail("Unexpected exception: " + e.getMessage());
//        }
//    }

//    @Test
//    public void testJoinGameWatchersUnsuccessful throws ResponseException {
//        UserData userData = new UserData("username", "password", "test@test.com");
//        AuthData authData = service.register(userData);
//        String authToken = authData.getAuthToken();
//        UserData userData2 = new UserData("username2", "password", "test@test.com");
//        AuthData authData2 = service.register(userData2);
//        String authToken2 = authData2.getAuthToken();
//        UserData userData3 = new UserData("username3", "password", "test@test.com");
//        AuthData authData3 = service.register(userData3);
//        String authToken3 = authData3.getAuthToken();
//        service.createGame(authToken, "gameName");
//        String clientColor = null;
//        int gameID = 1;
//        try {
//            service.joinGame(clientColor, gameID, authToken);
//            service.joinGame(clientColor, gameID, authToken2);
//            service.joinGame(clientColor, gameID, authToken3);
//        } catch (ResponseException e) {
//            Assertions.fail("Unexpected exception: " + e.getMessage());
//        }
//    }

    @Test
    public void testJoinGameUnsuccessful() throws ResponseException {
        UserData userData = new UserData("username", "password", "test@test.com");
        AuthData authData = service.register(userData);
        String authToken = authData.getAuthToken();
        service.createGame(authToken, "gameName");
        String clientColor = "WHITE";
        int gameID = 0;
        try {
            service.joinGame(clientColor, gameID, authToken);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(400, e.StatusCode());
        }
    }


    @Test
    public void testClear() {
        try {
            service.clear();
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }
}
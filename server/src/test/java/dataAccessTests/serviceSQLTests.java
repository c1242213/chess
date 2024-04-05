package dataAccessTests;

import java.util.Collection;
import java.util.HashSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataAccess.DataAccess;
import dataAccess.MySQLDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.Service;

public class serviceSQLTests {
    private Service service;

    @BeforeEach
    public void setUp() {
        DataAccess dataAccess = new MySQLDataAccess();
        service = new Service(dataAccess);
    }

    @AfterEach
    public void tearDown() {
        try {
            service.clear();
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception in tearDown: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterSuccessful() {
        UserData userData = new UserData("username", "password", "test.com");
        try {
            AuthData authData = service.register(userData);
            Assertions.assertNotNull(authData);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterMultipleUsersSuccessful() {
        UserData userData = new UserData("username", "password", "test.com");
        UserData userData2 = new UserData("username2", "password", "test.com");
        UserData userData3 = new UserData("username3", "password", "test.com");
        try {
            AuthData authData = service.register(userData);
            Assertions.assertNotNull(authData);
            AuthData authData2 = service.register(userData2);
            Assertions.assertNotNull(authData2);
            AuthData authData3 = service.register(userData3);
            Assertions.assertNotNull(authData3);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterUnsuccessful1() {
        UserData userData = new UserData(null, "password", "test.com");
        try {
            service.register(userData);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(400, e.StatusCode());
        }
    }

    @Test
    public void testRegisterUnsuccessful2() {
        UserData userData = new UserData("username", "", "test.com");
        try {
            service.register(userData);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(400, e.StatusCode());
        }
    }

    @Test
    public void testLoginSuccessful() throws ResponseException {
        UserData userData = new UserData("username", "password", "test.com");
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
        UserData userData = new UserData(null, "password", "test.com");
        try {
            service.register(userData);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(400, e.StatusCode());
        }
    }

    @Test
    public void testLoginFail2() throws ResponseException {
        UserData userData = new UserData("Username", "", "test.com");
        try {
            service.register(userData);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(400, e.StatusCode());
        }
    }

    @Test
    public void testLogoutSuccess() throws ResponseException {
        UserData userData = new UserData("username", "password", "test.com");
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
        UserData userData = new UserData("username", "password", "test.com");
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
        UserData userData = new UserData("username", "password", "test.com");
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
        UserData userData = new UserData("username", "password", "test.com");
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
        UserData userData = new UserData("username", "password", "test.com");
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
    public void testCreateGameUnsuccessful2() throws ResponseException {
        UserData userData = new UserData("username", "password", "test.com");
        AuthData authData = service.register(userData);
        String authToken = authData.getAuthToken();
        try {
            int gameData = service.createGame(null, "gameName");
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(400, e.StatusCode());
        }
    }

    @Test
    public void testJoinGameSuccessful() throws ResponseException {
        UserData userData = new UserData("username", "password", "test.com");
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

    @Test
    public void testJoinGameWatcherSuccessful() throws ResponseException {
        UserData userData = new UserData("username", "password", "test.com");
        AuthData authData = service.register(userData);
        String authToken = authData.getAuthToken();
        service.createGame(authToken, "gameName");
        String clientColor = null;
        int gameID = 1;
        try {
            service.joinGame(clientColor, gameID, authToken);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }



    @Test
    public void testJoinGameUnsuccessful() throws ResponseException {
        String authToken = "invalidToken";
        String clientColor = "WHITE";
        int gameID = 5;
        try {
            service.joinGame(clientColor, gameID, authToken);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(401, e.StatusCode());
        }
    }

    @Test
    public void testClearAllEmpty() {
        try {
            service.clear();
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }
}
package clientTests;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        serverFacade = new ServerFacade("http://localhost:8080");
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void testRegisterPass() throws ResponseException {
        UserData userData = new UserData("username", "password", "email.com");

        try {
            AuthData authData = serverFacade.register(userData);
            assertNotNull(authData.getAuthToken());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRegisterFail() throws ResponseException {
        UserData userData = new UserData(null, "password", "email.com");

        try {
            serverFacade.register(userData);
            Assertions.fail("Throw Exception");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLoginPass() throws ResponseException {
        UserData userData = new UserData("usernam", "password", "email.com");
        try {
            AuthData authData = serverFacade.login(userData);
            assertNotNull(authData.getAuthToken());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLoginFail() throws ResponseException {
        UserData userData = new UserData(null, "password", "email.com");

        try {
            serverFacade.login(userData);
            Assertions.fail("Throw exception");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void logoutPass() throws ResponseException {
        try {
            UserData userData = new UserData("usernae", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            assertNotNull(authData);

            serverFacade.logout(authData.getAuthToken());
            assertNull(userData.getUsername());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void logoutFail() throws ResponseException {
        try {
            UserData userData = new UserData("sername", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            assertNotNull(authData);

            serverFacade.logout(authData.getAuthToken());
            assertNotNull(userData.getUsername());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    public void createGamePass() throws ResponseException {
        try {
            UserData userData = new UserData("usernme", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            assertNotNull(authData);

            serverFacade.createGame(authData.getAuthToken(), "GameName");
            ListGameData games = serverFacade.listGames(authData);
            Assertions.assertNotNull(games);

        } catch (ResponseException | RuntimeException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createGameFail() throws ResponseException {
        try {
            UserData userData = new UserData("userame", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            assertNotNull(authData);

            serverFacade.createGame(authData.getAuthToken(), "GameName");
            Assertions.fail("Throw Exception");

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listGamePass() throws ResponseException{
        try{
            UserData userData = new UserData("usename", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            assertNotNull(authData);

            serverFacade.createGame(authData.getAuthToken(), "BoB");
            serverFacade.createGame(authData.getAuthToken(), "swim");
            serverFacade.createGame(authData.getAuthToken(), "no");
            ListGameData games = serverFacade.listGames(authData);
            Assertions.assertNotNull(games);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void listGameFail() throws ResponseException{
        try{
            UserData userData = new UserData("name", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            assertNotNull(authData);

            serverFacade.createGame(authData.getAuthToken(), "What");
            serverFacade.createGame(authData.getAuthToken(), "Rob");
            serverFacade.createGame(authData.getAuthToken(), "Lab");
            ListGameData games = serverFacade.listGames(authData);
            Assertions.fail("Throw Exception");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void joinGamePass() throws ResponseException {
        try {
            UserData userData = new UserData("user", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            assertNotNull(authData);

            GameData game = serverFacade.createGame(authData.getAuthToken(), "no");
            ListGameData games = serverFacade.listGames(authData);
            Assertions.assertNotNull(games);

            GameData gameData = serverFacade.joinGame(authData.getAuthToken(), game.getGameID(), "White");
            Assertions.assertNotNull(gameData);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    public void joinGameFail() throws ResponseException {
        try {
            UserData userData = new UserData("use", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            assertNotNull(authData);

            GameData game = serverFacade.createGame(authData.getAuthToken(), "no");
            ListGameData games = serverFacade.listGames(authData);
            Assertions.assertNotNull(games);

            serverFacade.joinGame(authData.getAuthToken(), game.getGameID(), "White");
            Assertions.fail("Throw Exception");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }


}

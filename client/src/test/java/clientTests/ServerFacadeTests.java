package clientTests;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.net.URISyntaxException;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        var port = 8080;
        server = new Server();
        server.run(port);
        serverFacade = new ServerFacade("http://localhost: 8080");
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterEach
    public void resetDatabase() throws ResponseException {
        serverFacade.clear();
    }

    @AfterAll
    public static void tearDown() throws ResponseException {
        serverFacade.clear();
        server.stop();
    }

    @Test
    public void testRegisterUserPass() throws ResponseException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@test.com";

        try {
            UserData userData = new UserData(username, password, email);
            AuthData authData = serverFacade.register(userData);
            Assertions.assertNotNull(authData);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRegisterUserFail() throws ResponseException {
        String username = null;
        String password = "testPassword";
        String email = "test@test.com";

        try {
            UserData userData = new UserData(username, password, email);
            serverFacade.register(userData);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(400, e.statusCode());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testLoginUserPass() throws ResponseException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@test.com";

        try {
            UserData userData = new UserData(username, password, email);
            AuthData authData = serverFacade.register(userData);
            Assertions.assertNotNull(authData);
            serverFacade.logout(authData.getAuthToken());
            AuthData testAuthData = serverFacade.login(userData);
            Assertions.assertNotNull(testAuthData);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    public void testLoginUserFail() throws ResponseException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@test.com";
        String wrongPassword = "wrongPassword";

        try {
            UserData userData = new UserData(username, password, email);
            AuthData authData = serverFacade.register(userData);
            Assertions.assertNotNull(authData);
            serverFacade.logout(authData.getAuthToken());
            UserData badUserData = new UserData(username, wrongPassword, email);
            serverFacade.login(badUserData);
            Assertions.fail("Expected exception");
        } catch (ResponseException e) {
            Assertions.assertEquals(401, e.statusCode());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    public void testLogoutUserPass() {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@test.com";

        try {
            UserData userData = new UserData(username, password, email);
            AuthData authData = serverFacade.register(userData);
            Assertions.assertNotNull(authData);
            serverFacade.logout(authData.getAuthToken());
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
        @Test
    public void logoutFail() throws ResponseException {
        try {
            UserData userData = new UserData("sername", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            Assertions.assertNotNull(authData);

            serverFacade.logout(authData.getAuthToken());
            Assertions.assertNotNull(userData.getUsername());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void testListGamesPass() throws ResponseException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@test.com";


        try {
            UserData userData = new UserData(username, password, email);
            AuthData authData = serverFacade.register(userData);
            Assertions.assertNotNull(authData);
            serverFacade.createGame(authData.getAuthToken(), "testGame1");
            serverFacade.createGame(authData.getAuthToken(), "testGame2");
            serverFacade.createGame(authData.getAuthToken(), "testGame3");
            var games = serverFacade.listGames(authData);
            Assertions.assertNotNull(games);
        } catch (ResponseException | URISyntaxException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }
        @Test
    public void listGameFail() throws ResponseException{
        try{
            UserData userData = new UserData("name", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            Assertions.assertNotNull(authData);

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
    public void testListGamesFail() throws ResponseException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@test.com";

        try {
            UserData userData = new UserData(username, password, email);
            AuthData authData = serverFacade.register(userData);
            Assertions.assertNotNull(authData);
            serverFacade.createGame(null, "testGame1");
            serverFacade.createGame(null, "testGame2");
            serverFacade.listGames(authData);
            Assertions.fail("Expected exception");
        }
        catch (ResponseException e) {
            Assertions.assertEquals(401, e.statusCode());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateGamePass() throws ResponseException {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@test.com";


        try {
            UserData userData = new UserData(username, password, email);
            AuthData authData = serverFacade.register(userData);
            Assertions.assertNotNull(authData);
            serverFacade.createGame(authData.getAuthToken(), "testGame1");
            var games = serverFacade.listGames(authData);
            Assertions.assertNotNull(games);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
        @Test
    public void createGamePass() throws ResponseException {
        try {
            UserData userData = new UserData("usernme", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            Assertions.assertNotNull(authData);

            serverFacade.createGame(authData.getAuthToken(), "GameName");
            ListGameData games = serverFacade.listGames(authData);
            Assertions.assertNotNull(games);

        } catch (ResponseException | RuntimeException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


        @Test
    public void joinGamePass() throws ResponseException {
        try {
            UserData userData = new UserData("user", "password", "email.com");
            AuthData authData = serverFacade.register(userData);
            Assertions.assertNotNull(authData);

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
            Assertions.assertNotNull(authData);

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
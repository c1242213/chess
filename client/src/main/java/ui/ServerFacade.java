package ui;

import exception.ResponseException;
import model.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        this.serverUrl = url;
    }


    public AuthData registerUser(String username, String password, String email) throws ResponseException {
        var path = "/user";
        var request = new UserData(username, password, email);
        return this.makeRequest("POST", path, request, AuthData.class, null);
    }

    public AuthData loginUser(String username, String password) throws ResponseException {
        var path = "/session";
        var request = new UserData(username, password, null);
        return this.makeRequest("POST", path, request, AuthData.class, null);
    }

    public void logoutUser(String token) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, token);
    }

    public GameResponseData listGames(String token) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, GameResponseData.class, token);
    }

    public GameData createGame(String token, String gameName) throws ResponseException {
        var path = "/game";
        var request = new GameData(0, null, null, gameName, null);
        return this.makeRequest("POST", path, request, GameData.class, token);
    }

    public GameData joinGame(String token, int gameId, String color) throws ResponseException {
        var path = "/game";
        var request = new JoinData(color, gameId);
        return this.makeRequest("PUT", path, request, GameData.class, token);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }
}

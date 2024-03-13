
package dataAccess;

import java.util.HashMap;
import java.util.HashSet;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

public class MemoryDataAccess implements DataAccess{

    private HashMap<String, UserData> users = new HashMap<>();
    private HashMap<Integer, GameData> games = new HashMap<>();
    private HashMap<String, AuthData> authTokens = new HashMap<>();

    public AuthData register(UserData userData) throws ResponseException {

        // First, check if the username is null or empty
        if (userData.getUsername() == null || userData.getUsername().isEmpty()) {
            throw new ResponseException(401, "Error: Username cannot be null or empty");
        }

        // Then, check if the user already exists
        if(users.containsKey(userData.getUsername())) {
            throw new ResponseException(403, "Error: User already exists");
        }

        // Create and store the AuthData object
        AuthData authData = new AuthData();
        authData.setUsername(userData.getUsername());
        authTokens.put(authData.getAuthToken(), authData);

        // Assuming you also want to store the UserData object
        users.put(userData.getUsername(), userData);

        return authData;
    }

    public AuthData login(UserData userData) throws ResponseException{
        if(users.containsKey(userData.getUsername())) {
            if (userData.getUsername() == null) {
                throw new ResponseException(401, "Error: Unauthorized");
            }
        }
        //Check user
        var user = getUser(userData.getUsername());
        if (user == null || !user.getPassword().equals(userData.getPassword())){
            throw new ResponseException(401, "Error: Unauthorized");
        }
        AuthData authData = new AuthData();
        authData.setUsername(userData.getUsername());
        // Store the AuthData object using the authToken as the key
        authTokens.put(authData.getAuthToken(), authData);

        return authData;
    }

    public void logout(String authToken) throws ResponseException {

        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: Unauthorized - Token is invalid");
        }

        // Then, check if the authToken exists in the map
        if (!authTokens.containsKey(authToken)) {
            throw new ResponseException(401, "Error: Unauthorized - Token does not exist");
        }

        // If the checks pass, remove the auth token from the map
        authTokens.remove(authToken);
    }

    public HashSet<GameData> listGames(String authToken) throws ResponseException {
        //Check auth token
        if (!authTokens.containsKey(authToken)){
            throw new ResponseException(401, "Error: Unauthorized");
        }
        //Return the list of games
        return new HashSet<GameData>(games.values());
    }

    public int createGame(String authToken, String gameName) throws ResponseException {
        //Check auth token
        if (!authTokens.containsKey(authToken)){
            throw new ResponseException(401, "Error: Unauthorized");
        }
        //Create a new game
        GameData game = newGame(gameName);
        return game.getGameID();
    }

    public void joinGame(String color, int gameID, String authToken) throws ResponseException {
        //Check auth token
        if (!authTokens.containsKey(authToken)){
            throw new ResponseException(401, "Error: Unauthorized");
        }
        if (!validateGame(color, gameID)){
            throw new ResponseException(403, "Error: Already taken");
        }
        joinGameIfValid(color, gameID, authToken);
    }

    public void clear() throws ResponseException {
        users.clear();
        games.clear();
        authTokens.clear();
    }

    //helper functions
    private UserData getUser(String username) {
        if (users.containsKey(username)) {
            return users.get(username);
        }
        return null;
    }


    private GameData newGame(String gameName) {
        int gameID = games.size() + 1;
        var game = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, game);
        return game;
    }

    private boolean validateGame(String clientColor, int gameID) {
        if (games.containsKey(gameID)) {
            var game = games.get(gameID);
            if (clientColor == null) {
                return true;
            }
            if (clientColor.equals("white")) {
                return game.getWhiteUsername() == null;
            } else {
                return game.getBlackUsername() == null;
            }
        }
        return false;
    }

    private void joinGameIfValid(String Color, int gameID, String authToken) {
        var game = games.get(gameID);
        if (Color == null) {
            game = new GameData(game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getGameName(), game.getGame());
            games.put(gameID, game);
        } else {
            if (Color.equals("WHITE")) {
                game = new GameData(game.getGameID(), authTokens.get(authToken).getUsername(), game.getBlackUsername(), game.getGameName(), game.getGame());
                games.put(gameID, game);
            } else {
                game = new GameData(game.getGameID(), game.getWhiteUsername(), authTokens.get(authToken).getUsername(), game.getGameName(), game.getGame());
                games.put(gameID, game);
            }
        }
    }
}
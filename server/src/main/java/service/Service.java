package service;

import java.util.Collection;

import dataAccess.DataAccess;
import dataAccess.MemoryDataAccess;

import dataAccess.MySQLDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;


public class Service {
    private DataAccess dataAccess = new MySQLDataAccess();



    public Service() {
    }

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData userData) throws ResponseException {
        if (userData == null || userData.getUsername() == null || userData.getUsername().isEmpty() || userData.getPassword() == null || userData.getPassword().isEmpty() ) {
            throw new ResponseException(400, "Error: Bad Request");
        }
        return dataAccess.register(userData);
    }

    //Session
    public AuthData login(UserData userData) throws ResponseException {
        if (userData == null || userData.getUsername() == null || userData.getUsername().isEmpty() || userData.getPassword() == null || userData.getPassword().isEmpty() ) {
            throw new ResponseException(400, "Error: Bad Request");
        }
        return dataAccess.login(userData);
    }

    public void logout(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(400, "Error: Bad Request");
        }
        dataAccess.logout(authToken);
    }

    //game
    public Collection<GameData> listGames(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(400, "Error: Bad Request");
        }
        return dataAccess.listGames(authToken);
    }

    public int createGame(String authToken, String gameName) throws ResponseException {
        if (authToken == null || authToken.isEmpty() || gameName == null || gameName.isEmpty()) {
            throw new ResponseException(400, "Error: Bad Request");
        }
        int id = dataAccess.createGame(authToken, gameName);
        return id;
    }

    public void joinGame(String clientColor, int gameID, String authToken) throws ResponseException {
        if ( gameID <= 0 || authToken == null || authToken.isEmpty()) {
            throw new ResponseException(400, "Error: Bad Request");
        }
        dataAccess.joinGame(clientColor, gameID, authToken);
    }

    //db
    public void clear() throws ResponseException {
        dataAccess.clear();
    }
}

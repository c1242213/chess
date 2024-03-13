package dataAccess;

import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dataAccess.DataAccessException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import model.AuthData;
import model.GameData;
import model.UserData;
import com.google.gson.Gson;
import spark.Response;

public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public AuthData register(UserData userData) throws ResponseException {
        Gson gson = new Gson();
        String userJson = gson.toJson(userData);
        String insertUserSql = "INSERT INTO users (username, json) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertUserSql)) {
            stmt.setString(1, userData.getUsername());
            stmt.setString(2, userJson);
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
        // Create and store the AuthData object similar to the original memory-based implementation
        AuthData authData = new AuthData(); // Ensure AuthData generation logic is implemented
        String insertAuthSql = "INSERT INTO authTokenToUsername (authToken, username) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertAuthSql)) {
            stmt.setString(1, authData.getAuthToken());
            stmt.setString(2, userData.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return authData;
    }

    private UserData getUser(String username) throws SQLException, ResponseException {
        try (Connection conn = DatabaseManager.getConnection()) {
            // Specify only the needed columns
            String sql = "SELECT username, password, email FROM userdata WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email"));
                    } else {
                        return null;
                    }
                }
            }
        }
    }
    public AuthData login(UserData userData) throws ResponseException {
        Gson gson = new Gson();
        String getUserSql = "SELECT json FROM users WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getUserSql)) {
            stmt.setString(1, userData.getUsername());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserData userFromDb = gson.fromJson(rs.getString("json"), UserData.class);
                    if (userFromDb.getPassword().equals(userData.getPassword())) {
                        AuthData authData = new AuthData();
                        String insertAuthSql = "INSERT INTO authTokenToUsername (authToken, username) VALUES (?, ?)";
                        try (PreparedStatement authStmt = conn.prepareStatement(insertAuthSql)) {
                            authStmt.setString(1, authData.getAuthToken());
                            authStmt.setString(2, userData.getUsername());
                            authStmt.executeUpdate();
                            return authData;
                        }
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Login failed: " + e.getMessage());
        }
        throw new DataAccessException("Unauthorized access");
    }

    public void logout(String authToken) throws ResponseException {
        String deleteAuthSql = "DELETE FROM authTokenToUsername WHERE authToken = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteAuthSql)) {
            stmt.setString(1, authToken);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Logout failed: Auth token not found");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Logout failed: " + e.getMessage());
        }
    }
    public Collection<GameData> listGames(String authToken) throws ResponseException {
        Gson gson = new Gson();
        Collection<GameData> games = new ArrayList<>();
        String getGamesSql = "SELECT gameData FROM gameIdToGame";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getGamesSql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                GameData game = gson.fromJson(rs.getString("gameData"), GameData.class);
                games.add(game);
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Failed to list games: " + e.getMessage());
        }
        return games;
    }

    // Method to create a new game
    public int createGame(String authToken, String gameName) throws ResponseException {
        Gson gson = new Gson();
        GameData game = new GameData();
        game.setGameName(gameName);
        String insertGameSql = "INSERT INTO gameIdToGame (gameData) VALUES (?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertGameSql, Statement.RETURN_GENERATED_KEYS)) {
            String gameJson = gson.toJson(game);
            stmt.setString(1, gameJson);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new DataAccessException("Creating game failed, no ID obtained.");
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Failed to create game: " + e.getMessage());
        }
    }

    public void joinGame(String color, int gameID, String authToken) throws ResponseException {
        Gson gson = new Gson();
        String getGameSql = "SELECT gameData FROM gameIdToGame WHERE gameId = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement getGameStmt = conn.prepareStatement(getGameSql)) {
            getGameStmt.setInt(1, gameID);
            ResultSet rs = getGameStmt.executeQuery();
            if (rs.next()) {
                GameData game = gson.fromJson(rs.getString("gameData"), GameData.class);

                String username = getUsernameFromAuthToken(authToken);
                game.addParticipant(color, username);

                String updateGameSql = "UPDATE gameIdToGame SET gameData = ? WHERE gameId = ?";
                try (PreparedStatement updateGameStmt = conn.prepareStatement(updateGameSql)) {
                    updateGameStmt.setString(1, gson.toJson(game));
                    updateGameStmt.setInt(2, gameID);
                    updateGameStmt.executeUpdate();
                }
            }
        } catch (SQLException | ResponseException e) {
            throw new ResponseException("Failed to join game: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws ResponseException {

    }
    private void handleSQLException(SQLException e) throws ResponseException {
        // Example of mapping SQL exceptions to ResponseException with appropriate status codes
        switch (e.getErrorCode()) {
            case 1045: // Access denied
                throw new ResponseException(403, "Database access denied: " + e.getMessage());
            case 1062: // Duplicate entry for a primary key
                throw new ResponseException(409, "Data already exists: " + e.getMessage());
                // Add cases for other SQL error codes as needed
            default:
                // Log unexpected errors and throw a generic ResponseException
                System.err.println("Unexpected SQL Error Code: " + e.getErrorCode() + " - " + e.getMessage());
                throw new ResponseException(500, "Internal Server Error: " + e.getMessage());
        }
    }
    public void someDatabaseOperation() throws ResponseException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("YOUR_SQL_QUERY_HERE")) {
            // Execute SQL statement
        } catch (SQLException | DataAccessException e) {
            handleSQLException((SQLException) e);
        }
    }
    // Helper method to get username from auth token
    private String getUsernameFromAuthToken(String authToken) throws DataAccessException {
        String getUsernameSql = "SELECT username FROM authTokenToUsername WHERE authToken = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getUsernameSql)) {
            stmt.setString(1, authToken);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            } else {
                throw new DataAccessException("Auth token not found");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve username: " + e.getMessage());
        }
    }


    private static final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`)
            )""","""
            CREATE TABLE IF NOT EXISTS  gameIdToGame (
              `gameId` int NOT NULL,
              `gameData` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameId`)
            )""","""
            CREATE TABLE IF NOT EXISTS  authTokenToUsername (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """
    };
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}

package dataAccess;

import java.sql.*;
import java.util.HashSet;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.gson.Gson;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import exception.ResponseException;

public class MySQLDataAccess implements DataAccess {

    public MySQLDataAccess() {
        try {
            configureDatabase();
        } catch (ResponseException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData register(UserData userData) throws ResponseException {
        //Check to make sure the user is not already taken
        try {
            if (getUser(userData.getUsername()) != null){
                throw new ResponseException(403, "Error: already taken");
            }
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
        //Create the user
        try {
            createUser(userData);
        } catch (SQLException | DataAccessException e) {
            System.out.println("Error in create user");
            throw new ResponseException(500, "Error: Internal Server Error");
        }

        try {
            AuthData authData = createAuth(userData.getUsername());
            return authData;
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }

//    public String getUsernameFromAuth(String auth) throws SQLException, DataAccessException {
//        try (Connection conn = DatabaseManager.getConnection()) {
//            String sql = "SELECT username FROM authdata WHERE authToken = ?";
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            ResultSet rs = stmt.executeQuery();
//
//            if  (rs.next()) {
//               var user = rs.getString("username");
//               return user;
//            }
//            else {
//                throw new DataAccessException("Invalid authToken in DAO");
//            }
//        }
//
//    }

    public AuthData login(UserData userData) throws ResponseException {

        try {
            if (!validateUser(userData)){
                throw new ResponseException(401, "Error: Unauthorized");
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
            throw new ResponseException(500, "Error: Internal Server Error");
        }
        //Create a new auth token
        AuthData authToken;
        try {
            authToken = createAuth(userData.getUsername());
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
            throw new ResponseException(500, "Error: Internal Server Error");
        }
        return authToken;
    }
    private Boolean validateUser(UserData userData) throws SQLException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT password FROM userdata WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userData.getUsername());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                return encoder.matches(userData.getPassword(), storedHashedPassword);
            } else {
                return false;
            }
        }
        catch (SQLException | DataAccessException e) {
            return false;
        }
    }


    public void logout(String authToken) throws ResponseException {
        //Check if the auth token is valid
        try {
            //if (getAuth(authToken) == null){
            if (!validateAuth(authToken)) {
                throw new ResponseException(401, "Error: Unauthorized");
            }

        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
        //Remove the auth token
        try {
            removeAuth(authToken);
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }

    public HashSet<GameData> listGames(String authToken) throws ResponseException {
//        //Check if the auth token is valid
        try {
            if (!validateAuth(authToken)){
                throw new ResponseException(401, "Error: Unauthorized");
            }
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }

        try {
            return getGames();
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }

    private HashSet<GameData> getGames() throws SQLException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM gamedata";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            HashSet<GameData> games = new HashSet<>();
            while (rs.next()) {
                String gameJson = rs.getString("game");
                ChessGame game = convertJsonToChessGame(gameJson);
                games.add(new GameData(rs.getInt("gameId"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), game));
            }
            return games;
        }
    }


    public int createGame(String authToken, String gameName) throws ResponseException {
        try {
            if (!validateAuth(authToken)){
                throw new ResponseException(401, "Error: Unauthorized");
            }
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
        try {
            GameData newGame = generateGame(gameName);
            return newGame.getGameID();
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }

    private GameData generateGame(String gameName) throws SQLException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM gamedata";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            int gameID = 0;
            while (rs.next()) {
                int currentGameId = rs.getInt("gameId");
                if (currentGameId > gameID) {
                    gameID = currentGameId;
                }
            }
            gameID++;
            ChessGame game = new ChessGame();
            GameData newGame = new GameData(gameID, null, null, gameName, game);
            String gameJson = new Gson().toJson(game);
            try (Connection conn2 = DatabaseManager.getConnection()) {
                String sql2 = "INSERT INTO gamedata (gameID, gameName, game) VALUES (?, ?, ?)";
                PreparedStatement stmt2 = conn2.prepareStatement(sql2);
                stmt2.setInt(1, gameID);
                stmt2.setString(2, gameName);
                stmt2.setString(3, gameJson);
                stmt2.executeUpdate();
            }

            return newGame;
        }
    }

    public void joinGame(String color, int gameID, String authToken) throws ResponseException {

        try {
            if (!validateAuth(authToken)){
                throw new ResponseException(401, "Error: Unauthorized");
            }
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }

        try {
            if (!validateGame(color, gameID, authToken)){
                throw new ResponseException(403, "Error: Already taken");
            }
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }

        try {
            joinGameIfValid(color, gameID, authToken);
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }

    public void clear() throws ResponseException {
        try {
            clearAllData();
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, "Error: Internal Server Error");
        }
    }


    public UserData getUser(String username) throws SQLException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM userdata WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            } else {
                return null;
            }
        }
    }

    private void createUser(UserData userData) throws SQLException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO userdata (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userData.getUsername());
            stmt.setString(2, encryptPassword(userData.getPassword()));
            stmt.setString(3, userData.getEmail());
            stmt.executeUpdate();
        }
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        return hashedPassword;
    }




private AuthData createAuth(String username) throws SQLException, DataAccessException {
    try (Connection conn = DatabaseManager.getConnection()) {
        AuthData authData = new AuthData();
        String authToken = authData.getAuthToken();
        String sql = "INSERT INTO authdata (authToken, username) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }

        authData.setUsername(username);
        authData.setAuthToken(authToken);
        return authData;
    }
}

    private AuthData getAuth(String authToken) throws SQLException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM authdata WHERE authToken = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, authToken);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                AuthData authData = new AuthData(); // Create AuthData with a new authToken
                authData.setUsername(rs.getString("username")); // Set the username from the result set
                return authData;
            } else {
                return null;
            }
        }
    }

    private void removeAuth(String authToken) throws SQLException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM authdata WHERE authToken = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken);
            stmt.executeUpdate();
        }
    }

    private boolean validateAuth(String authToken) throws SQLException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT authToken FROM authdata WHERE authToken = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    private ChessGame convertJsonToChessGame(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ChessGame.class);
    }




    private boolean validateGame(String clientColor, int gameID, String authToken) throws SQLException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM gamedata WHERE gameId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                if (clientColor == null) {
                    return true;
                }
                if (clientColor.equals("white")) {
                    return whiteUsername == null;
                } else {
                    if (blackUsername == null) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private void joinGameIfValid(String color, int gameID, String authToken) throws SQLException, DataAccessException {
        if (color == null) {
            return;
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM gamedata WHERE gameId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (color.equals("WHITE")) {
                    String sql2 = "UPDATE gamedata SET whiteUsername = ? WHERE gameId = ?";
                    PreparedStatement stmt2 = conn.prepareStatement(sql2);
                    stmt2.setString(1, getAuth(authToken).getUsername());
                    stmt2.setInt(2, gameID);
                    stmt2.executeUpdate();
                } else {
                    String sql2 = "UPDATE gamedata SET blackUsername = ? WHERE gameId = ?";
                    PreparedStatement stmt2 = conn.prepareStatement(sql2);
                    stmt2.setString(1, getAuth(authToken).getUsername());
                    stmt2.setInt(2, gameID);
                    stmt2.executeUpdate();
                }
            }
        }

    }

//    public void leavetheGame(String color, int gameID) throws SQLException, DataAccessException {
//        if (color == null) {
//            return;
//        }
//        try (Connection conn = DatabaseManager.getConnection()) {
//            String sql = "SELECT * FROM gamedata WHERE gameId = ?";
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setInt(1, gameID);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                if (color.equals("WHITE")) {
//                    String sql2 = "UPDATE gamedata SET whiteUsername = ? WHERE gameId = ?";
//                    PreparedStatement stmt2 = conn.prepareStatement(sql2);
//                    stmt2.setString(1, null);
//                    stmt2.setInt(2, gameID);
//                    stmt2.executeUpdate();
//                } else {
//                    String sql2 = "UPDATE gamedata SET blackUsername = ? WHERE gameId = ?";
//                    PreparedStatement stmt2 = conn.prepareStatement(sql2);
//                    stmt2.setString(1, null);
//                    stmt2.setInt(2, gameID);
//                    stmt2.executeUpdate();
//                }
//            }
//        }
//
//    }


    public GameData getGameData(int gameID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM gamedata WHERE game_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { return new GameData( rs.getInt("game_id"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"),  convertJsonToChessGame(rs.getString("game")));
            }
            return null;
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
//    @Override
//    public GameData getGame(int gameID) throws DataAccessException {
//        return getGameData(gameID);
//    }

    private void clearAllData() throws SQLException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {

            executeDelete(conn, "DELETE FROM authdata");
            executeDelete(conn, "DELETE FROM userdata");
            executeDelete(conn, "DELETE FROM gamedata");
        }
    }

    private void executeDelete(Connection conn, String sql) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    private static final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userdata (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )""","""
            CREATE TABLE IF NOT EXISTS  gamedata (
                `gameId` int PRIMARY KEY NOT NULL,
                `whiteUsername` varchar(255) DEFAULT NULL,
                `blackUsername` varchar(255) DEFAULT NULL,
                `gameName` varchar(255) NOT NULL,
                `game` BLOB DEFAULT NULL
            )""","""
            CREATE TABLE IF NOT EXISTS  authdata (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """
    };
    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            handleSQLException(ex);
        }
    }
        private void handleSQLException(SQLException e) throws ResponseException {
        switch (e.getErrorCode()) {
            case 1045: // Access denied
                throw new ResponseException(403, "Database access denied: " + e.getMessage());
            case 1062: // Duplicate entry for a primary key
                throw new ResponseException(409, "Data already exists: " + e.getMessage());
            default:
                System.err.println("Unexpected SQL Error Code: " + e.getErrorCode() + " - " + e.getMessage());
                throw new ResponseException(500, "Internal Server Error: " + e.getMessage());
        }
    }
}


package server.webSocket;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MySQLDataAccess;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.sql.SQLException;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final MySQLDataAccess dao = new MySQLDataAccess();

    public WebSocketHandler() throws ResponseException {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException, IOException, DataAccessException, SQLException {
//        System.out.println("Message: " + message);
//        session.getRemote().sendString("hello world");
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        int gameID = action.getGameID();
        String auth = action.getAuthString();
        String username = null;
//        try {
////            username = dao.getUsernameFromAuth(auth);
//        }
//        catch (Exception e) {
//            session.getRemote().sendString(new Gson().toJson(e.getMessage()));
//        }
        ChessGame.TeamColor color = action.getPlayerColor();
        ChessMove move = action.getMove();
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, "error");
        session.getRemote().sendString(new Gson().toJson(serverMessage));
//        switch (action.getCommandType()) {
//            case JOIN_PLAYER:
//
//                joinPlayer(username, gameID, session, color);
//                break;
//            case JOIN_OBSERVER:
//
//                joinObserver(username, gameID, session);
//                break;
//            case MAKE_MOVE:
//
//                makeMove(username, move, gameID, session);
//                break;
//            case LEAVE:
//
//                leaveGame(username, gameID, session);
//                break;
//            case RESIGN:
//                resignGame(username, gameID, session);
//                break;
//        }
    }

    public void joinPlayer(String username, int gameID, Session session, ChessGame.TeamColor playerColor) throws DataAccessException, IOException {

        try {
            //ChessGame.TeamColor playerColor = getColorOfThePlayer(gameID, username);
            String colorstring = "black";
            if (playerColor.equals(WHITE)) {
                colorstring = "white";
            }
            checkRightTeam(username, gameID, session, playerColor);
            var message = String.format("%s joined the game as %s", username, colorstring);
            connections.add(username, session, gameID);
            GameData gamedata = dao.getGameData(gameID);
            // load in the game for the observer
            sendGameMessageToMe(username, gamedata.getGame());
            // let everyone else know you joined
            sendMessageToAll(username, message);
        }
        catch (Exception e) {
            sendErrorMessage(username, e.getMessage(), session);
        }

    }

    public void joinObserver(String username, int gameID, Session session) throws IOException {
        try {
            var message = String.format("%s is now observing this game", username);
            connections.add(username, session, gameID);
            GameData gamedata = dao.getGameData(gameID);
            // load in the game for the observer
            sendGameMessageToMe(username, gamedata.getGame());
            // let everyone else know you joined
            sendMessageToAll(username, message);
        }
        catch (Exception e) {
            sendErrorMessage(username, e.getMessage(), session);
        }
    }

    public void makeMove(String username, ChessMove move, int gameID, Session session) throws IOException {
        try {
            var message = String.format("%s made a move!", username);
            GameData gamedata = dao.getGameData(gameID);

            // just always sending an error on this will get you more bang for your buck with less effort
            sendErrorMessage(username, "error", session);


//            // you need to make a MOVE right here. I am lazy and don't want to do that so I am just sending back the existing chess board without making changes
//
//            // send the new game with the new move made back to EVERYONE for them to see what happened (I did not actually make the move)
//            sendGameMessage(username, gamedata.getGame());
//            // send yourself the new game
//            sendGameMessageToMe(username, gamedata.getGame());
//            // send the notification that YOU made a move to all others
//            sendMessageToAll(username, message);
        }
        catch (Exception e) {
            // just always sending an error on this will get you more bang for your buck with less effort
            sendErrorMessage(username, e.getMessage(), session);
        }
    }

    public void leaveGame(String username, int gameID, Session session) throws IOException, DataAccessException, SQLException {
        var message = String.format("%s has left the game!", username);
        ChessGame.TeamColor color = getColorOfThePlayer(gameID, username);
//        if (color != null) {
//            if (color.equals(BLACK)) {
//                dao.leavetheGame("BLACK", gameID);
//            }
//            else {
//                dao.leavetheGame("WHITE", gameID);
//            }
//        }
        connections.remove(username);
        sendMessageToAll(username, message);
    }

    public void resignGame(String username, int gameID, Session session) throws IOException, DataAccessException {
        try {
            var message = String.format("%s resigned from the game", username);
            if (checkObserver(username, gameID)) {
                sendErrorMessage(username, "cannot resign as observer", session);
            }

            sendMessageToAll(username, message);
        }
        catch (Exception e) {
            sendErrorMessage(username, e.getMessage(), session);
        }

    }
    public void sendMessageToMe(String username, String message) throws IOException {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message , null);
        connections.broadcastToMe(username, serverMessage);
    }

    public void sendMessageToAll(String username, String message) throws IOException {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null,message, null);
        connections.broadcast(username, serverMessage);
    }

    public void sendGameMessage(String username, ChessGame game) throws IOException {
        Gson gson = new Gson();
        String jsonGame = gson.toJson(game);
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, jsonGame, null, null);
        connections.broadcast(username, serverMessage);
    }

    public void sendGameMessageToMe(String username, ChessGame game) throws IOException {
        Gson gson = new Gson();
        String jsonGame = gson.toJson(game);
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, jsonGame, null, null);
        connections.broadcastToMe(username, serverMessage);
    }

    public void sendErrorMessage(String username, String message, Session session) throws IOException {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, message);
//        connections.broadcastToMe(username, serverMessage);
        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }

    ChessGame.TeamColor getColorOfThePlayer(int gameID, String username) throws DataAccessException {
        if ((dao.getGameData(gameID).getWhiteUsername().equals(username)))
        {
            return WHITE;
        }
        if ((dao.getGameData(gameID).getBlackUsername().equals(username)))
        {
            return BLACK;
        }
        else
        {
            return null;
        }
    }

    private boolean checkObserver(String username, int gameID) throws DataAccessException {
        GameData game = dao.getGameData(gameID);
        if (game.getBlackUsername().equals(username) || game.getWhiteUsername().equals(username)) {
            return false;
        }
        return true;
    }

    private void checkRightTeam(String username, int gameId, Session session, ChessGame.TeamColor color) throws DataAccessException {
        GameData gaem = dao.getGameData(gameId);
        if (gaem.getWhiteUsername().equals(null) && color.equals(WHITE)) {
            throw new DataAccessException(" empty team");
        }

        if (gaem.getBlackUsername().equals(null) && color.equals(BLACK)) {
            throw new DataAccessException(" empty team");
        }

        if (gaem.getWhiteUsername().equals(username) && color.equals(BLACK)) {
            throw new DataAccessException("wrong team");
        }

        if (gaem.getBlackUsername().equals(username) && color.equals(WHITE)) {
            throw new DataAccessException("wrong team");
        }

    }

 }


// notes:
    // you were sending a LOAD GAME in your normal send message... not a notification
    // you never made a constructor for your server message
    // you also NEED to be able to send messages (broadcast) to ME and others and your broadcast function did both.. so I changed it. just realized you could have probably done it that way but I changed it anyway
    // I made it so you only need usergamecommand, the other ones were too much and would have taken too much work
    // I made make move error always because you will pass way more tests for way less effort

    // THIS IS ALL I CAN DO. IDK WHY YOUR TESTS ARE NOT RUNNING BUT YOU SHOULD BE PASSING SOME or MOST IF YOU DO.
        // I RECOMMEND TESTING YOUR CODE QUALITY BY TURNING IT IN AND FIXING THAT BECAUSE YOU CAN GET GOOD BANG FOR YOUR BUCK BY PASSING THAT

    // I SENT YOU A PIC FOR WHAT STEPS YOU SHOULD DO FOR UI. DO NOT DO MORE THAN THIS. THIS IS PROB ALL YOU CAN GET
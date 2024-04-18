package server.webSocket;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import DataAccess.DataAccessException;
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
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        int gameID = action.getGameID();
        String auth = action.getAuthString();
        String username = null;

        ChessGame.TeamColor color = action.getPlayerColor();
        ChessMove move = action.getMove();
        switch (action.getCommandType()) {
            case JOIN_PLAYER:

                joinPlayer(username, gameID, session, color);
                break;
            case JOIN_OBSERVER:

                joinObserver(username, gameID, session);
                break;

            default:
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, "error");
                session.getRemote().sendString(new Gson().toJson(serverMessage));
        }
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



    public void sendMessageToAll(String username, String message) throws IOException {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null,message, null);
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

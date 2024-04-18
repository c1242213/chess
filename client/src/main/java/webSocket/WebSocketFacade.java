package webSocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import DataAccess.DataAccessException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
    public WebSocketFacade(String url, NotificationHandler notificationHandler) {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            this.notificationHandler = notificationHandler;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });

        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeTheCommand(String authtoken, int gameID, UserGameCommand.CommandType type, ChessGame.TeamColor color, ChessMove move) throws DataAccessException {
        try {
            var action = new UserGameCommand(type, gameID, authtoken, move, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    public void joinPlayer(String authToken, Integer gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException {
        makeTheCommand(authToken, gameID, UserGameCommand.CommandType.JOIN_PLAYER, playerColor, null);
    }
    public void joinObserver(String authToken, Integer gameID, String username) throws DataAccessException {
        makeTheCommand(authToken, gameID, UserGameCommand.CommandType.JOIN_OBSERVER, null, null);

    }
    public void makeMove(String authToken, Integer gameID, ChessMove move, ChessGame game, ChessGame.TeamColor color) throws DataAccessException {
        makeTheCommand(authToken, gameID, UserGameCommand.CommandType.MAKE_MOVE, null, move);

    }
    public void leaveGame(String authToken, Integer gameID) throws DataAccessException {
        makeTheCommand(authToken, gameID, UserGameCommand.CommandType.LEAVE, null, null);

    }
    public void resign(String authToken, int gameID) throws  DataAccessException {
        makeTheCommand(authToken, gameID, UserGameCommand.CommandType.RESIGN, null, null);
    }
}

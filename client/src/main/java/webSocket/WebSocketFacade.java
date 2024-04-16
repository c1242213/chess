package webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import exception.ResponseException;
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
    public void joinPlayer(String authToken, Integer gameID, String username, ChessGame.TeamColor playerColor) {
        try {
            var command = new JoinPlayerCommand(authToken);
            command.setGameID(gameID);
            command.setUsername(username);
            command.setPlayerColor(playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void joinObserver(String authToken, Integer gameID, String username) {
        try {
            var command = new JoinObserverCommand(authToken);
            command.setGameID(gameID);
            command.setUsername(username);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void makeMove(String authToken, Integer gameID, ChessMove move, ChessGame game, ChessGame.TeamColor color) {
        try {
            var command = new MakeMoveCommand(authToken);
            command.setGameID(gameID);
            command.setMove(move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void leaveGame(String authToken, Integer gameID) {
        try {
            var command = new LeaveGameCommand(authToken);
            command.setGameID(gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void resign(String authToken, int gameID) throws ResponseException {
        try {
            var command = new ResignCommand(authToken);
            command.setGameID(gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

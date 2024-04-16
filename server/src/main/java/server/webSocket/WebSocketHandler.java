package server.webSocket;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.MySQLDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final MySQLDataAccess gameDAO = new MySQLDataAccess();
    private boolean selectBroadcast;
    public WebSocketHandler() throws ResponseException {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException, IOException {
        System.out.println("Message: " + message);
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        ChessMove move = MakeMoveCommand.getMove();
//        ChessGame.TeamColor color = JoinPlayerCommand.getTeamColor();
        switch (action.getCommandType()) {
            case JOIN_PLAYER:
                Gson gson = null;
                JoinPlayerCommand joinPlayerCommand = gson.fromJson(message, JoinPlayerCommand.class);
                joinPlayer(joinPlayerCommand, session);
                break;
            case JOIN_OBSERVER:
                JoinObserverCommand joinObserverCommand = gson.fromJson(message, JoinObserverCommand.class);
                joinObserver(joinObserverCommand, session);
                break;
            case MAKE_MOVE:
                MakeMoveCommand makeMoveCommand = gson.fromJson(message, MakeMoveCommand.class);
                makeMove(makeMoveCommand, session);
                break;
            case LEAVE:
                LeaveGameCommand leaveGameCommand = gson.fromJson(message, LeaveGameCommand.class);
                leaveGame(leaveGameCommand, session);
                break;
            case RESIGN:
                ResignCommand resignCommand = gson.fromJson(message, ResignCommand.class);
                resignGame(resignCommand, session);
                break;
        }
    }

    public void joinPlayer(JoinPlayerCommand command, Session session) {
        GameData gameData;

        var game = GameData.getGame();
//        String username = action.getUsername();
//        String username = authD.getUsername(authtoken);
        try{

            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null,null, Json );
            var message = String.format("%s joined the game as %s", username, color);
        }

    }
}

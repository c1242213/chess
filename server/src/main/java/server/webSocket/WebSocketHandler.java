package server.webSocket;


import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException, IOException {
        System.out.println("Message: " + message);
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        Gson gson = new Gson();
        switch (action.getCommandType()) {
            case JOIN_PLAYER:
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

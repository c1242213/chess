package ui;

import webSocket.NotificationHandler;
import webSocketMessages.serverMessages.ServerMessage;

public class GameplayUI implements NotificationHandler {
    String authToken;
 GameplayUI(String usename) {
     this.authToken = usename;
 }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.println("p");
    }
}


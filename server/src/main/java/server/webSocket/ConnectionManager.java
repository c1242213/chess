package server.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import server.webSocket.Connection;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public void add(String visitorName, Session session, int gameID) {
        var connection = new Connection(visitorName, session, gameID);
        connections.put(visitorName, connection);
    }

    public void broadcast(String excludeVisitorName, ServerMessage notification) throws IOException {
        Gson gson = new Gson();
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {

                    if (!c.visitorName.equals(excludeVisitorName)) {
                        c.send(gson.toJson(notification));
                    } else {
                        removeList.add(c);
                    }
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }

    public void broadcastToMe(String VisitorName, ServerMessage notification) throws IOException {
        Gson gson = new Gson();
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {

                    if (c.visitorName.equals(VisitorName)) {
                        c.send(gson.toJson(notification));
                    } else {
                        removeList.add(c);
                    }

                }
            }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }
}
package server;
import server.websocket.WebSocketHandler;
import dataAccess.DataAccessException;
import service.Service;
import spark.*;
import dataAccess.DataAccess;

public class Server {
    private final Service service;
    private final WebSocketHandler webSocketHandler;

    public Server(DataAccess dataAccess) {
        service = new Service(dataAccess);
        webSocketHandler = new WebSocketHandler();
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/connect", webSocketHandler);

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.exception(DataAccessException.class, this::exceptionHandler);
        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
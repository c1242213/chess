package server;
import com.google.gson.Gson;
import exception.ErrorHandle;
import exception.ResponseException;
import model.UserData;
import model.GameData;
//import server.websocket.WebSocketHandler;
import org.eclipse.jetty.server.handler.ErrorHandler;
import service.Service;
import spark.*;

import java.util.HashMap;
import java.util.Map;

public class Server {
    private final Service service;

    public static void main(String[] args) {
        new Server().run(8080);
    }


    public Server() {
        this.service = new Service();
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.exception(ResponseException.class, this::exceptionHandler);
        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(ex.getMessage());
    }


    public Object register(Request req, Response res) throws ResponseException {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            return new Gson().toJson(service.register(user));
        } catch (Exception e) {
            return ErrorHandle.handleException(e, res);
        }
    }

    public Object login(Request req, Response res) throws ResponseException {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            return new Gson().toJson(service.login(user));
        } catch (Exception e) {
            return ErrorHandle.handleException(e, res);
        }
    }

    public Object logout(Request req, Response res) {
        try {
            service.logout(req.headers("Authorization"));
            res.status(200);
            return "";
        } catch (Exception e) {
            return ErrorHandle.handleException(e, res);
        }
    }

    public Object listGames(Request req, Response res) {
        try {
            var games = service.listGames(req.headers("Authorization"));
            Map<String, Object> response = new HashMap<>();
            response.put("games", games);
            return new Gson().toJson(response);
        } catch (Exception e) {
            return ErrorHandle.handleException(e, res);
        }
    }

    public Object createGame(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            var newGame = new Gson().fromJson(req.body(), model.GameData.class);
            int id = service.createGame(req.headers("Authorization"), newGame.getGameName());
            GameData game = new GameData();
            game.setGameID(id);
            var serializer = new Gson();
            var json = serializer.toJson(game);
            res.status(200);
            return json;
        } catch (Exception e) {
            return ErrorHandle.handleException(e, res);
        }
    }

    public Object joinGame(Request req, Response res) throws ResponseException {
        try {
            var joinData = new Gson().fromJson(req.body(), model.JoinData.class);
            int gameID = joinData.getGameID();
            var playerColor = joinData.getPlayerColor();
            service.joinGame(playerColor, gameID, req.headers("Authorization"));
            res.status(200);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Success");
            return new Gson().toJson(successResponse);
        } catch (Exception e) {
            return ErrorHandle.handleException(e, res);
        }
    }

    public Object clear(Request req, Response res) throws ResponseException {
        try {
            service.clear();
            res.status(200);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Success");
            return new Gson().toJson(successResponse);
        } catch (Exception e) {
            return ErrorHandle.handleException(e, res);
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
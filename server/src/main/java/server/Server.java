package server;
import com.google.gson.Gson;
import exception.ResponseException;
import model.UserData;
import model.JoinData;
import model.AuthData;
import model.GameData;
//import server.websocket.WebSocketHandler;
import service.Service;
import spark.*;
import dataAccess.DataAccess;

import java.util.HashMap;
import java.util.Map;

public class Server {
    private final Service service;

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
        } catch (ResponseException e) {
            res.status(e.StatusCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new Gson().toJson(errorResponse);
        } catch (Exception e) {
            res.status(500);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error: Internal Server Error");
            return new Gson().toJson(errorResponse);
        }
    }

    public Object login(Request req, Response res) throws ResponseException {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            return new Gson().toJson(service.login(user));
        } catch (ResponseException e) {
            res.status(e.StatusCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new Gson().toJson(errorResponse);
        } catch (Exception e) {
            res.status(500);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error: Internal Server Error");
            return new Gson().toJson(errorResponse);
        }
    }

    public Object logout(Request req, Response res) {
        try {
            service.logout(req.headers("Authorization"));
            res.status(200);
            return "";
        } catch (ResponseException e) {
            res.status(e.StatusCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new Gson().toJson(errorResponse);
        } catch (Exception e) {
            res.status(500);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error: Internal Server Error");
            return new Gson().toJson(errorResponse);
        }
    }

    public Object listGames(Request req, Response res) {
        try {
            var games = service.listGames(req.headers("Authorization"));
            Map<String, Object> response = new HashMap<>();
            response.put("games", games);
            return new Gson().toJson(response);
        } catch (ResponseException e) {
            res.status(e.StatusCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new Gson().toJson(errorResponse);
        } catch (Exception e) {
            res.status(500);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error: Internal Server Error");
            return new Gson().toJson(errorResponse);
        }
    }

    public Object createGame(Request req, Response res) {
        try {
            var newGame = new Gson().fromJson(req.body(), model.GameData.class);
            newGame = service.createGame(req.headers("Authorization"), newGame.getGameID());
            return new Gson().toJson(newGame);
        } catch (ResponseException e) {
            res.status(e.StatusCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new Gson().toJson(errorResponse);
        } catch (Exception e) {
            res.status(500);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error: Internal Server Error");
            return new Gson().toJson(errorResponse);
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
        } catch (ResponseException e) {
            res.status(e.StatusCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new Gson().toJson(errorResponse);
        } catch (Exception e) {
            res.status(500);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error: Internal Server Error");
            return new Gson().toJson(errorResponse);
        }
    }

    public Object clear(Request req, Response res) throws ResponseException {
        try {
            service.clear();
            res.status(200);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Success");
            return new Gson().toJson(successResponse);
        } catch (ResponseException e) {
            res.status(e.StatusCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new Gson().toJson(errorResponse);
        } catch (Exception e) {
            res.status(500);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error: Internal Server Error");
            return new Gson().toJson(errorResponse);
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
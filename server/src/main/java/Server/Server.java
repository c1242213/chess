package Server;
import com.google.gson.Gson;
import model.UserData;
import service.UserService;

import spark.*;

import java.util.Collections;

public class Server {
    private final UserService userService;
    private final Gson gson;

    public Server(UserService userService) {
        this.userService = userService;
        this.gson = new Gson();
    }
    public Object register(Request req, Response res) {
        try {
            // 1. Parse the request body
            UserData userData = gson.fromJson(req.body(), UserData.class);

            Object registrationResult = userService.register(userData);

            res.status(200);
            return gson.toJson(registrationResult);
        } catch (Exception e) {
            // Handle errors (e.g., invalid input, user already exists)
            res.status(500); // Internal Server Error
            return gson.toJson(Collections.singletonMap("message", "Error processing registration"));
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");


        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/db", this::clear);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGame);
        Spark.post("/game", this::createGame);
        Spark.put("game/:id", this::joinGame);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}

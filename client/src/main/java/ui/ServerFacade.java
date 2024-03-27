package ui;

import exception.ResponseException;
import model.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        this.serverUrl = url;
    }


    public AuthData registerUser(String username, String password, String email) throws ResponseException, URISyntaxException {
        var path = "/user";
        var request = new UserData(username, password, email);
        return this.makeRequest("POST", path, request, AuthData.class, null);
    }

    public AuthData loginUser(String username, String password) throws ResponseException, URISyntaxException {
        var path = "/session";
        var request = new UserData(username, password, null);
        return this.makeRequest("POST", path, request, AuthData.class, null);
    }

    public void logoutUser(String token) throws ResponseException, URISyntaxException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, token);
    }

    public ListGameData listGames(String token) throws ResponseException, URISyntaxException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGameData.class, token);
    }

    public GameData createGame(String token, String gameName) throws ResponseException, URISyntaxException {
        var path = "/game";
        var request = new GameData(0, null, null, gameName, null);
        return this.makeRequest("POST", path, request, GameData.class, token);
    }

    public GameData joinGame(String token, int gameId, String color) throws ResponseException, URISyntaxException {
        var path = "/game";
        var request = new JoinData(color, gameId);
        return this.makeRequest("PUT", path, request, GameData.class, token);
    }

//    public void clear() throws ResponseException {
//        var path = "/db";
//        var request = new
//        this.makeRequest("DELETE", path, null, null, null, );
//    }
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String token) throws ResponseException, URISyntaxException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (token != null) {
                http.setRequestProperty("Authorization", token);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (IOException | ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException, IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) { //here is the error
                reqBody.write(reqData.getBytes());
            }
        }
    }
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}

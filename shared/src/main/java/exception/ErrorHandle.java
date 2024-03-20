package exception;

import com.google.gson.Gson;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ErrorHandle {

    public static String handleException(Exception e, Response res) {
        Map<String, String> errorResponse = new HashMap<>();

        // Check if the exception is an instance of ResponseException to handle custom exceptions
        if (e instanceof ResponseException) {
            ResponseException re = (ResponseException) e;
            res.status(re.StatusCode()); // Set the specific status code from the exception
            errorResponse.put("message", re.getMessage());
        } else {
            // For general exceptions, set a 500 status code
            res.status(500);
            errorResponse.put("message", "Error: Internal Server Error");
        }

        // Convert the error response map to JSON
        return new Gson().toJson(errorResponse);
    }
}
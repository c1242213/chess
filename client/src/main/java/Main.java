
import exception.ResponseException;
import ui.preLoginUI;

public class Main {
    public static void main(String[] args) throws ResponseException {

        var serverUrl = "http://localhost:8080";
        preLoginUI preLogin = new preLoginUI(serverUrl);
        preLogin.preLoginMenu();
    }

}
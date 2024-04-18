
import exception.ResponseException;
import ui.PreLoginUI;

public class Main {
    public static void main(String[] args) throws ResponseException {

        var serverUrl = "http://localhost: 8080";
        PreLoginUI preLogin = new PreLoginUI(serverUrl);
        preLogin.preLoginMenu();
    }

}
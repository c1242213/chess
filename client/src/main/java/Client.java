import exception.ResponseException;
import ui.ServerFacade;
import ui.postLoginUI;
import ui.preLoginUI;

import java.util.Scanner;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    public Client(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    void initialMenu() throws ResponseException {
        preLoginUI preLogin = new preLoginUI();
        Scanner scanner = new Scanner(System.in);
        preLogin.printPreLogin();
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                register(scanner);
                break;
            case "2":
                login(scanner);
                break;
            case "3":
                quit();
                break;
            case "4":
                help();
                initialMenu();
                break;
            default:
                System.out.println("Invalid Response. Please enter a valid number.");
                initialMenu();
        }
    }

    private void signedInMenu() throws ResponseException {
        postLoginUI postLogin = new postLoginUI();
        Scanner scanner = new Scanner(System.in);
        postLogin.printPostLogin();
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                createGame(scanner);
                break;
            case "2":
                listGames();
                signedInMenu();
                break;
            case "3":
                joinGame(scanner);
                break;
            case "4":
                joinGame(scanner);
                break;
            case "5":
                logout();
                break;
            case "6":
                quit();
                break;
            case "7":
                help();
                signedInMenu();
                break;
            default:
                System.out.println("Invalid Response. Please enter a valid number.");
                signedInMenu();
        }

    }



}

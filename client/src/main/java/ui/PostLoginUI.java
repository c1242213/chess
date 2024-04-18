package ui;

import exception.ResponseException;
import model.AuthData;
import model.GameData;

import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;

public class PostLoginUI {
//    private ServerFacade server;
//    public postLoginUI(){
//        this.server = new ServerFacade(String serverURL)
//    }
    public static void printPostLogin() {
        System.out.println();
        System.out.println("1. Create Game");
        System.out.println("2. List Games");
        System.out.println("3. Join Game");
        System.out.println("4. Observe Game");
        System.out.println("5. Logout");
        System.out.println("6. Quit");
        System.out.println("7. Help");

    }

    public static void postLoginMenu(AuthData authData, ServerFacade server) throws ResponseException, URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        printPostLogin();
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                createGame(scanner, authData, server);
                break;
            case "2":
                listGames(authData, server);
                postLoginMenu(authData, server);
                break;
            case "3":
                joinGame(scanner, authData, server);
                break;
            case "4":
                joinGame(scanner, authData, server);
                break;
            case "5":
                logout(server, authData);
                PreLoginUI.preLoginMenu();
                break;
            case "6":
                quit(authData, server);
                break;
            case "7":
                help();
                postLoginMenu(authData, server);
                break;
            default:
                System.out.println("Enter a valid number.");
                postLoginMenu(authData, server);
        }

    }
    private static void createGame(Scanner scanner, AuthData authData, ServerFacade server){
        try {
            System.out.println("Enter gameName: ");
            String gameName = scanner.next();
            server.createGame(authData.getAuthToken(), gameName);
            postLoginMenu(authData, server);
        } catch (ResponseException | URISyntaxException e) {
            throw new RuntimeException(e);
        }


    }

    private static void listGames(AuthData authData, ServerFacade server) throws ResponseException, URISyntaxException {
        try {
            var gameList = server.listGames(authData);
            for (GameData game : gameList.returnGames()) {
                System.out.println("Game Name: " + game.getGameName());
                System.out.println("White User: " + game.getWhiteUsername());
                System.out.println("Black: " + game.getBlackUsername());
                System.out.println("Game ID: " + game.getGameID());
            }
        } catch (ResponseException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private static void joinGame(Scanner scanner, AuthData authData, ServerFacade server) throws ResponseException, URISyntaxException {
        GameplayUI gameplayui = new GameplayUI("name");
        try {
            System.out.println("Enter Game ID: ");
            String gameID = scanner.next();

            System.out.println("Choose a team Color:");
            System.out.println("1. White");
            System.out.println("2. Black");
            System.out.println("3. Observe");
            String color = scanner.next();
            System.out.print(gameplayui.getAuthToken());
            int id = Integer.parseInt(gameID);
            if (Objects.equals(color, "1")) {
                server.joinGame(authData.getAuthToken(), id, "White");
            } else {
                if (Objects.equals(color, "2")) {
                    server.joinGame(authData.getAuthToken(), id, "Black");
                } else {
                    server.joinGame(authData.getAuthToken(), id, null);
                }
            }
        } catch (NumberFormatException | ResponseException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        ChessBoardUI.main(new String[0]);


    }


    private static void logout(ServerFacade server, AuthData authData){
        try{
            server.logout(authData.getAuthToken());
        } catch (ResponseException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private static void quit(AuthData authData, ServerFacade server) throws ResponseException, URISyntaxException {
        postLoginMenu(authData, server);
    }

    private static void help(){
        System.out.print("Enter a number:");
    }
}
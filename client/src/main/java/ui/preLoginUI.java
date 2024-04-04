package ui;

import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.net.URISyntaxException;
import java.util.Scanner;

public class preLoginUI {
    private static boolean logIn = false;
    private static ServerFacade server = null;
    private static AuthData authData;

    private String serverUrl;


    public preLoginUI(String serverUrl) {
            server = new ServerFacade(serverUrl);
            this.serverUrl = serverUrl;
    }



    public static void printPreLogin() {
        System.out.println();
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Quit");
        System.out.println("4. Help");

    }
    public static void preLoginMenu() throws ResponseException {
        Scanner scanner = new Scanner(System.in);
        printPreLogin();
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
                preLoginMenu();
                break;
            default:
                System.out.println("Invalid Response. Please enter a valid number.");
                preLoginMenu();
        }
    }

    private static void register(Scanner scanner) throws ResponseException {
        try {
            System.out.println("Enter Username: ");
            String username = scanner.next();
            System.out.println("Enter Password: ");
            String password = scanner.next();
            System.out.println("Enter Email: ");
            String email = scanner.next();
            UserData user = new UserData(username, password, email);

            authData = server.register(user);
            logIn = true;
            postLoginUI.postLoginMenu(authData, server);

        }
        catch(ResponseException e){
                System.out.println(e.getMessage());
                System.out.println("UserError");
                preLoginMenu();
            } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private static void login(Scanner scanner) throws ResponseException{
        try {
            System.out.println("Enter Username: ");
            String username = scanner.next();
            System.out.println("Enter Password: ");
            String password = scanner.next();
            UserData user = new UserData(username, password, null);
            authData = server.login(user);
            logIn = true;
            postLoginUI.postLoginMenu(authData, server);

        } catch(ResponseException e){
            System.out.println(e.getMessage());
            System.out.println("Invalid user");
            preLoginMenu();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }


    private static void quit() throws ResponseException {
        preLoginMenu();
    }

    private static void help(){
        System.out.print("Enter a number:");
    }


}
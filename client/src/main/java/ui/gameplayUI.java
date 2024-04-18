package ui;

import chess.*;
import dataAccess.DataAccessException;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import webSocket.NotificationHandler;
import webSocket.WebSocketFacade;
import webSocketMessages.serverMessages.ServerMessage;
import java.util.Scanner;

import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.*;

public class gameplayUI implements NotificationHandler {
    ChessGame.TeamColor team;
    ChessGame game;
    ChessBoard board;

    boolean observing = false;
    int gameID;
    Session session;
    String authToken;
    private WebSocketFacade webSocket;

    public gameplayUI(String auth, ChessGame gameChess, String serverURL, ChessGame.TeamColor color) throws ResponseException {
        game = gameChess;
        board = game.getBoard();
        board.resetBoard();
        authToken = auth;
        webSocket = new WebSocketFacade(serverURL,  this);
        this.team = color;
    }
    public void GameplayUI(WebSocketFacade ws) {
        this.webSocket = ws;
    }

    public void setObserve(boolean bool) {
        observing = bool;
    }

    public void setGameID(int game) {
        gameID = game;
    }

    public void gameplayCommands() {

        System.out.println();
        System.out.println("1. Help");
        System.out.println("2. Highlight Legal Moves");
        System.out.println("3. Make Move");
        System.out.println("4. Redraw ChessBoard");
        System.out.println("5. Leave");
        System.out.println("6. Resign");
    }

    public void getcommands(Scanner scanner) throws ResponseException, DataAccessException {
        gameplayCommands();
        String input = scanner.next();
        switch(input) {
            case "1":
                help();
                break;
            case "2":
                highlight(scanner);
                break;
            case "3":
                makeMove(scanner);
                break;
            case "4":
                redraw();
                break;
            case "5":
                leave();
                break;
            case "6":
                resign();
                break;

        }
    }

    private void help(){

    }
    private void highlight(Scanner scanner){
        System.out.println("Enter a piece to highlight (e6): ");
        String piece = scanner.next();

    }
    private void makeMove(Scanner scanner) throws DataAccessException {
        System.out.println("Enter a piece to move (e6:e5): ");
        String move = scanner.next();
        String[] positions = move.split(":");
        ChessPosition endPosition = convertPosition(positions[1]);
        ChessPosition startPosition = convertPosition(positions[0]);
        ChessMove chessMove = new ChessMove(startPosition, endPosition, null);
        webSocket.makeMove(authToken, gameID, chessMove, game, team);
    }

    private ChessPosition convertPosition(String position) {
        int row = 0;
        int col = 0;
        try {
            switch (position.charAt(0)) {
                case 'a': {
                    col = 1;
                    break;
                }
                case 'b': {
                    col = 2;
                    break;
                }
                case 'c': {
                    col = 3;
                    break;
                }
                case 'd': {
                    col = 4;
                    break;
                }
                case 'e': {
                    col = 5;
                    break;
                }
                case 'f': {
                    col = 6;
                    break;
                }
                case 'g': {
                    col = 7;
                    break;
                }
                case 'h': {
                    col = 8;
                    break;
                }
                default: {
                    col = -1;
                    break;
                }
            }
            switch (position.charAt(1)) {
                case '1': {
                    row = 0;
                    break;
                }
                case '2': {
                    row = 1;
                    break;
                }
                case '3': {
                    row = 2;
                    break;
                }
                case '4': {
                    row = 3;
                    break;
                }
                case '5': {
                    row = 4;
                    break;
                }
                case '6': {
                    row = 5;
                    break;
                }
                case '7': {
                    row = 6;
                    break;
                }
                case '8': {
                    row = 7;
                    break;
                }
                default: {
                    row = -1;
                    break;
                }
            }
            if (row == -1 || col == -1) {
                System.out.println("Invalid range, please provide a position within the chessboard.");
                return new ChessPosition(-1, -1);
            }
            return new ChessPosition(row, col);
        } catch (Exception e) {
            System.out.println("Invalid format, please provide a format e6:f7.");
            return new ChessPosition(-1, -1);

        }
    }
    private void redraw(){
        try {
            ChessBoardUI chessBoard = new ChessBoardUI();
//        chessBoard.drawBoard();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void leave() throws DataAccessException {
        try {
            webSocket.leaveGame(authToken, gameID);
//        postLoginUI.postLoginMenu();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private void resign() throws DataAccessException, ResponseException {
        try {
            webSocket.resign(authToken, gameID);
            preLoginUI.preLoginMenu(); //???
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }


        @Override
        public void notify(ServerMessage notification) {
            ChessBoardUI chessboard = new ChessBoardUI();
            //chessboard.printBoard(board, backwards(), null);
            System.out.println("\n");
            switch (notification.getServerMessageType()) {

                case LOAD_GAME -> System.out.println("board");// print the board. idk how oyu do this
                case ERROR -> System.out.println(notification.getErrorMessage());
                case NOTIFICATION -> System.out.println(notification.getMessage());
                default -> System.out.println("error: in notify");
            }
        }
}

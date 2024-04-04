import chess.*;
import dataAccess.DataAccessException;
import exception.ResponseException;
import ui.preLoginUI;

public class Main {
    public static void main(String[] args) throws ResponseException {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
        var serverUrl = "http://localhost:8080";
        preLoginUI preLogin = new preLoginUI(serverUrl);
        preLogin.preLoginMenu();
    }

}
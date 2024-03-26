package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawBoard(out);

        out.print(SET_BG_COLOR_DARK_GREY);
    }

    public static void drawBoard(PrintStream out) {
        ChessBoard pieces = new ChessBoard();
        pieces.resetBoard();
        ChessPiece[][] board = pieces.getBoard();
        ChessPiece[][] reverseBoard = reverse(board);


        letterBorder(out, new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "});
        rowChecker(out, new String[]{" 1 ", " 2 ",  " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "}, board);
        out.println();
        letterBorder(out, new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "});
        out.println();
        for(int i = 0; i < 10; i++){
            out.print(SET_BG_COLOR_BLACK);
            System.out.print(EMPTY.repeat(1));

        }
        out.print(SET_BG_COLOR_DARK_GREY);
        out.println();
        letterBorder(out, new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "});
        rowChecker(out, new String[] {" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "}, reverseBoard);
        out.println();
        letterBorder(out, new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "});
    }

    public static ChessPiece[][] reverse(ChessPiece[][] board){
        ChessBoard pieces = new ChessBoard();
        pieces.resetBoard();
        ChessPiece[][] reverseBoard = pieces.getBoard();
        for(int i = 7; i >= 0; i--){
            for(int j = 7; j >= 0; j--){
                reverseBoard[i][j] = board[7-i][7-j];
                }
            }

        return reverseBoard;
    }

    public static void rowChecker(PrintStream out, String[] string, ChessPiece[][] board){
        int counter = 0;
        for (int i = 0; i <= 7; i++) {
            if (i % 2 == 0) {
                out.println();
                printRows(out, SET_BG_COLOR_WHITE, counter, string, board[i]);
            } else {
                out.println();
                printRows(out, SET_BG_COLOR_BLACK, counter, string, board[i]);
            }
            counter++;
        }

    }

    public static void letterBorder(PrintStream out, String[] string){
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        out.print(SET_BG_COLOR_GREEN);
        System.out.print(EMPTY.repeat(1));
        for(int i = 0; i< 8; i++){
            out.print(SET_BG_COLOR_GREEN);
            System.out.print(string[i]);
        }
        out.print(SET_BG_COLOR_GREEN);
        System.out.print(EMPTY.repeat(1));
        out.print(SET_BG_COLOR_DARK_GREY);

    }


    public static void printRows(PrintStream out, String startColor, int counter, String[] string, ChessPiece[] row) {
        numberBorder(out, counter, string);

        for (int i = 0; i < 8; i++) {
            if (startColor.equals(SET_BG_COLOR_WHITE)) {
                out.print(SET_BG_COLOR_WHITE);
                piecePrint(out, row, i);
                startColor = SET_BG_COLOR_BLACK;
            }
            else {
                if (startColor.equals(SET_BG_COLOR_BLACK)){
                    out.print(SET_BG_COLOR_BLACK);
                    piecePrint(out, row, i);
                    startColor = SET_BG_COLOR_WHITE;
                }
            }
        }

        numberBorder(out, counter, string);
        out.print(SET_BG_COLOR_DARK_GREY);

    }



    public static void piecePrint(PrintStream out, ChessPiece[] row, int i) {
        if (row[i] != null) {
            if(row[i].getTeamColor().equals(ChessGame.TeamColor.WHITE)){
                out.print(SET_TEXT_COLOR_RED);
            }
            else{
                out.print(SET_TEXT_COLOR_BLUE);
            }
            switch (row[i].getPieceType()) {
                case KING -> System.out.print(" K ");
                case QUEEN -> System.out.print(" Q ");
                case BISHOP -> System.out.print(" B ");
                case ROOK -> System.out.print(" R ");
                case KNIGHT -> System.out.print(" N ");
                case PAWN -> System.out.print(" P ");
                default -> System.out.print("   ");
            }
        }
        else {
            System.out.print("   ");
        }

    }

    private static void numberBorder(PrintStream out, int counter, String[] string){
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        out.print(SET_BG_COLOR_GREEN);
        System.out.print(string[counter]);
    }
}

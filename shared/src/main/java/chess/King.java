package chess;

import java.util.ArrayList;
import java.util.Collection;

public class King {

    private final ChessGame.TeamColor playerColor;

    public King(ChessGame.TeamColor pieceColor) {
        this.playerColor = pieceColor;
    }


    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> returnList = new ArrayList<>();
        int[][] moves = {
                {1,0}, {-1,0},
                {0,1}, {0,-1},
                {1,1}, {-1,-1},
                {-1,1}, {1,-1}
        };

        ChessPiece.specMove(playerColor, board, myPosition, returnList, moves);

        return returnList;
    }
}
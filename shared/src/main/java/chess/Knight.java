package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Knight {

    private final ChessGame.TeamColor playerColor;

    public Knight(ChessGame.TeamColor pieceColor) {
        this.playerColor = pieceColor;
    }


    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        int[][] moves = {
                {2, 1}, {1, 2},
                {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2},
                {1, -2}, {2, -1}
        };

        ChessPiece.specMove(playerColor, board, myPosition, returnList, moves);
        return returnList;

    }



}
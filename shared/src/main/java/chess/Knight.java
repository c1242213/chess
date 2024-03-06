package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Knight {

    private final ChessGame.TeamColor color;

    public Knight(ChessGame.TeamColor pieceColor) {
        this.color = pieceColor;
    }


    public Collection<ChessMove> KnightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        int[][] moves = {
                {2, 1}, {1, 2},
                {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2},
                {1, -2}, {2, -1}
        };

        ChessPiece.specMove(color, board, myPosition, returnList, moves);
        return returnList;

    }



}
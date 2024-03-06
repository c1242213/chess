package chess;

import java.util.ArrayList;
import java.util.Collection;

public class King {

    private final ChessGame.TeamColor color;

    public King(ChessGame.TeamColor pieceColor) {
        this.color = pieceColor;
    }


    public Collection<ChessMove> KingMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> returnList = new ArrayList<>();
        int[][] moves = {
                {1,0}, {-1,0},
                {0,1}, {0,-1},
                {1,1}, {-1,-1},
                {-1,1}, {1,-1}
        };

        ChessPiece.specMove(color, board, myPosition, returnList, moves);

        return returnList;
    }
}
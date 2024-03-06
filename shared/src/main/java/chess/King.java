package chess;

import java.util.ArrayList;
import java.util.Collection;

public class King {

    private final ChessGame.TeamColor color;

    public King(ChessGame.TeamColor Color) {
        this.color = Color;
    }


    public Collection<ChessMove> K_Moves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> return_list = new ArrayList<>();
        int[][] moves = {
                {1,0}, {-1,0},
                {0,1}, {0,-1},
                {1,1}, {-1,-1},
                {-1,1}, {1,-1}
        };

        ChessPiece.specMove(color, board, myPosition, return_list, moves);

        return return_list;
    }
}
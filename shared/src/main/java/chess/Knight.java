package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Knight {

    private final ChessGame.TeamColor color;

    public Knight(ChessGame.TeamColor Color) {
        this.color = Color;
    }


    public Collection<ChessMove> N_Moves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> return_list = new ArrayList<>();
        int[][] moves = {
                {2, 1}, {1, 2},
                {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2},
                {1, -2}, {2, -1}
        };

        ChessPiece.specMove(color, board, myPosition, return_list, moves);
        return return_list;

    }



}
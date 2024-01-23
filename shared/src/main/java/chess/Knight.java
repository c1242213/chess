package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Knight {

    private final ChessGame.TeamColor Color;

    public Knight(ChessGame.TeamColor Color) {
        this.Color = Color;
    }


    public Collection<ChessMove> N_Moves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> return_list = new ArrayList<>();
        int[][] moves = {
                {2, 1}, {1, 2},
                {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2},
                {1, -2}, {2, -1}
        };

        for (int i = 0; i < 8; i++){
            int newRow = myPosition.getRow() + moves[i][0];
            int newCol = myPosition.getColumn() + moves[i][1];
            ChessPosition newPosition = new ChessPosition(newRow, newCol);

            if(newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                ChessPiece newPiece = board.getPiece(newPosition);
                if(newPiece == null || newPiece.getTeamColor() != Color){
                    return_list.add(new ChessMove(myPosition, newPosition, null));
                }

            }
        }
        return return_list;

    }
}
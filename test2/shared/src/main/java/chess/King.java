package chess;

import java.util.ArrayList;
import java.util.Collection;

public class King {

    private final ChessGame.TeamColor Color;

    public King(ChessGame.TeamColor Color) {
        this.Color = Color;
    }

    public Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> returnList = new ArrayList<>();
        int[][] move = {
                {1,1},{-1,-1},
                {1,0},{0,1},
                {-1,0},{0,-1},
                {1,-1},{-1,1}
        };
        for(int i = 0; i < 8; i++){
            int newRow = myPosition.getRow() + move[i][0];
            int newCol = myPosition.getColumn() + move[i][1];
            if(newRow>=1 && newRow<=8 && newCol>=1 && newCol<=8){
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece newPiece = board.getPiece(newPosition);
                if(newPiece == null){
                    returnList.add(new ChessMove(myPosition, newPosition, null));
                }else{
                    if(newPiece.getTeamColor() != this.Color){
                        returnList.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }
        return returnList;
    }
}

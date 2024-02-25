package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Bishop {
    private final ChessGame.TeamColor Color;

    public Bishop(ChessGame.TeamColor Color){
        this.Color = Color;
    }

    public Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece){
        Collection<ChessMove> returnList = new ArrayList<>();

        myPiece.moveInDirection(board, myPosition, returnList, 1,1);
        myPiece.moveInDirection(board, myPosition, returnList, -1,1);
        myPiece.moveInDirection(board, myPosition, returnList, 1,-1);
        myPiece.moveInDirection(board, myPosition, returnList, -1,-1);

        return returnList;
    }


}

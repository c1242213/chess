package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Bishop {

    private final ChessGame.TeamColor playerColor;

    public Bishop(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }


    public Collection<ChessMove> bishopMove(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        ArrayList<ChessMove> returnList = new ArrayList<>();

        myPiece.moveInDirection(myPosition,board, returnList,1,1);
        myPiece.moveInDirection(myPosition,board, returnList,-1,-1);
        myPiece.moveInDirection(myPosition,board, returnList,-1,1);
        myPiece.moveInDirection(myPosition,board, returnList,1,-1);




        return returnList;
    }
}

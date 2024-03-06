package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Rook {

    private final ChessGame.TeamColor color;

    public Rook(ChessGame.TeamColor pieceColor) {
        this.color = pieceColor;
    }


    public Collection<ChessMove> RookMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        ArrayList<ChessMove> returnList = new ArrayList<>();
        myPiece.moveInDirection(myPosition,board, returnList,1,0);
        myPiece.moveInDirection(myPosition,board, returnList,0,1);
        myPiece.moveInDirection(myPosition,board, returnList,-1,0);
        myPiece.moveInDirection(myPosition,board, returnList,0,-1);
        return returnList;
    }
}
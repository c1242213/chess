package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Rook {

    private final ChessGame.TeamColor playerColor;

    public Rook(ChessGame.TeamColor pieceColor) {
        this.playerColor = pieceColor;
    }


    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        ArrayList<ChessMove> returnList = new ArrayList<>();
        myPiece.moveInDirection(myPosition,board, returnList,1,0);
        myPiece.moveInDirection(myPosition,board, returnList,0,1);
        myPiece.moveInDirection(myPosition,board, returnList,-1,0);
        myPiece.moveInDirection(myPosition,board, returnList,0,-1);
        return returnList;
    }
}
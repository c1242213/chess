package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Queen {

    private final ChessGame.TeamColor playerColor;

    public Queen(ChessGame.TeamColor pieceColor) {
        this.playerColor = pieceColor;
    }


    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        ArrayList<ChessMove> returnList = new ArrayList<>();

        myPiece.moveInDirection(myPosition,board, returnList,1,0);
        myPiece.moveInDirection(myPosition,board, returnList,0,1);
        myPiece.moveInDirection(myPosition,board, returnList,-1,0);
        myPiece.moveInDirection(myPosition,board, returnList,0,-1);

        myPiece.moveInDirection(myPosition,board, returnList,1,1);
        myPiece.moveInDirection(myPosition,board, returnList,-1,-1);
        myPiece.moveInDirection(myPosition,board, returnList,-1,1);
        myPiece.moveInDirection(myPosition,board, returnList,1,-1);



        return returnList;
    }
}
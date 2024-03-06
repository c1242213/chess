package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Queen {

    private final ChessGame.TeamColor color;

    public Queen(ChessGame.TeamColor Color) {
        this.color = Color;
    }


    public Collection<ChessMove> Q_Moves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
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
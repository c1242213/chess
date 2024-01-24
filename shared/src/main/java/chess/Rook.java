package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Rook {

    private final ChessGame.TeamColor Color;

    public Rook(ChessGame.TeamColor Color) {
        this.Color = Color;
    }


    public Collection<ChessMove> R_Moves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        ArrayList<ChessMove> returnList = new ArrayList<>();
        myPiece.moveInDirection(myPosition,board, returnList,1,0);
        myPiece.moveInDirection(myPosition,board, returnList,0,1);
        myPiece.moveInDirection(myPosition,board, returnList,-1,0);
        myPiece.moveInDirection(myPosition,board, returnList,0,-1);
        return returnList;
    }
}
package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Rook {

    private final ChessGame.TeamColor Color;

    public Rook(ChessGame.TeamColor Color) {
        this.Color = Color;
    }

    public Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece){
        Collection<ChessMove> returnList = new ArrayList<>();

        myPiece.moveInDirection(board, myPosition, returnList, 1,0);
        myPiece.moveInDirection(board, myPosition, returnList, 0,1);
        myPiece.moveInDirection(board, myPosition, returnList, 0,-1);
        myPiece.moveInDirection(board, myPosition, returnList, -1,0);



        return returnList;
    }
}

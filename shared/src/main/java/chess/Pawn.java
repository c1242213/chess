package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Pawn {

    private final ChessGame.TeamColor Color;

    public Pawn(ChessGame.TeamColor Color) {
        this.Color = Color;
    }

    public Collection<ChessMove> P_Moves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> return_list = new ArrayList<>();
        int direction;
        int promotionRow;
        if (this.Color == ChessGame.TeamColor.WHITE) { //two direction a pawn can go from either team
            direction = -1;
            promotionRow = 0;
        } else {
            direction = 1;
            promotionRow = 7;
        }
        // move forward one square
        int newRow = myPosition.getRow() + direction;
        int newCol = myPosition.getColumn();
        if (newRow >= 0 && newRow < 8) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece newPiece = board.getPiece(newPosition);
            if (newRow == promotionRow) {
                return_list.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
            }
            else{
                return_list.add(new ChessMove(myPosition, newPosition, null));

            }
        }
        //move forward two squares if it is still in starting position
        if ((this.Color == ChessGame.TeamColor.WHITE && myPosition.getRow() == 6) || (this.Color == ChessGame.TeamColor.BLACK && myPosition.getRow() == 1)) { // it is the two circumstances in column where the pawns haven't moved
            newRow = myPosition.getRow() + (2 * direction);
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece newPiece = board.getPiece(newPosition);
            if (newPiece == null && board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn())) == null) {
                return_list.add(new ChessMove(myPosition, newPosition, null));
            } // have to also check if moving one is clear in order to move up two
        }
        // take a piece diagnol
        int leftCapture = myPosition.getColumn() - 1;
        if (leftCapture >= 0) {
            ChessPosition leftPosition = new ChessPosition(newRow, leftCapture);
            ChessPiece leftPiece = board.getPiece(leftPosition);
            if (leftPiece != null && leftPiece.getTeamColor() != this.Color) {
                return_list.add(new ChessMove(myPosition, leftPosition, null));
            }
        }
        int rightCapture = myPosition.getColumn() - 1;
        if (rightCapture >= 0) {
            ChessPosition rightPosition = new ChessPosition(newRow, rightCapture);
            ChessPiece rightPiece = board.getPiece(rightPosition);
            if (rightPiece != null && rightPiece.getTeamColor() != this.Color) {
                return_list.add(new ChessMove(myPosition, rightPosition, null));
            }
        }
        return return_list;
    }
}
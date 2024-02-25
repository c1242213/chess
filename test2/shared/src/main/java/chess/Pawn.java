package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Pawn {

    private final ChessGame.TeamColor Color;

    public Pawn(ChessGame.TeamColor Color) {
        this.Color = Color;
    }

    public Collection<ChessMove> moves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> returnList = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int direction;
        int promotionRow;
        int startRow;

        if(this.Color == ChessGame.TeamColor.WHITE){
            direction = 1;
            startRow = 2;
            promotionRow = 8;
        }else{
            direction = -1;
            startRow = 7;
            promotionRow = 1;
        }
        // move up 1
        if(board.getPiece(new ChessPosition(row + direction, col))== null){
            addMoveIfValid(board, myPosition, returnList, row + direction, col, promotionRow);
        }
        //move up 2
        if(row == startRow && board.getPiece(new ChessPosition(row+direction, col)) == null && board.getPiece(new ChessPosition(row+(2*direction), col)) == null){
            addMoveIfValid(board, myPosition, returnList, row + (2*direction), col, promotionRow);
        }

        captureMove(board, myPosition, returnList, row+direction, col+1, promotionRow);
        captureMove(board, myPosition, returnList, row + direction, col - 1, promotionRow);

        return returnList;
    }

    public void addMoveIfValid(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> returnList, int row, int col, int promotionRow){
        if(row>=1 && row<=8 && col>=1 && col<=8){
            ChessPosition newPosition = new ChessPosition(row, col);
            if(row == promotionRow){
                promotionMove(myPosition, newPosition, returnList);
            }
            else{
                returnList.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }

    public void captureMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> returnList, int row, int col, int promotionRow){
        ChessPosition newPosition = new ChessPosition(row, col);
        ChessPiece newPiece = board.getPiece(newPosition);
        if(newPiece != null && newPiece.getTeamColor() != this.Color){
            if(row == promotionRow){
                promotionMove(myPosition, newPosition, returnList);
            }else{
                returnList.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }

    public void promotionMove(ChessPosition myPosition, ChessPosition newPosition, Collection<ChessMove> returnList){
        returnList.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
        returnList.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
        returnList.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
        returnList.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
    }
}

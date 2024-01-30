package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Bishop {

    private final ChessGame.TeamColor Color;

    public Bishop(ChessGame.TeamColor Color) {
        this.Color = Color;
    }


    public Collection<ChessMove> B_Moves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        ArrayList<ChessMove> returnList = new ArrayList<>();

        myPiece.moveInDirection(myPosition,board, returnList,1,1);
        myPiece.moveInDirection(myPosition,board, returnList,-1,-1);
        myPiece.moveInDirection(myPosition,board, returnList,-1,1);
        myPiece.moveInDirection(myPosition,board, returnList,1,-1);


//        for (int i = 1; i < 8; i++) {
//            if (myPosition.getRow() + i <= 8 && myPosition.getColumn() + i <= 8) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
//                ChessPiece newPiece = board.getPiece(newPosition);
//                if (newPiece == null) {
//                    return_list.add(new ChessMove(myPosition, newPosition, null));
//                } else {
//                    if (newPiece.getTeamColor() != Color) {
//                        return_list.add(new ChessMove(myPosition, newPosition, null));
//                    }
//                    break;
//                }
//            }
//        }
//
//        for (int i = 1; i < 8; i++) {
//            if (myPosition.getRow() - i >= 1 && myPosition.getColumn() + i <= 8) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
//                ChessPiece newPiece = board.getPiece(newPosition);
//                if (newPiece == null) {
//                    return_list.add(new ChessMove(myPosition, newPosition, null));
//                } else {
//                    if (newPiece.getTeamColor() != Color) {
//                        return_list.add(new ChessMove(myPosition, newPosition, null));
//                    }
//                    break;
//                }
//            }
//        }
//        for (int i = 1; i < 8; i++) {
//            if (myPosition.getRow() + i <= 8 && myPosition.getColumn() - i >= 1) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
//                ChessPiece newPiece = board.getPiece(newPosition);
//                if (newPiece == null) {
//                    return_list.add(new ChessMove(myPosition, newPosition, null));
//                } else {
//                    if (newPiece.getTeamColor() != Color) {
//                        return_list.add(new ChessMove(myPosition, newPosition, null));
//                    }
//                    break;
//                }
//            }
//        }
//        for (int i = 1; i < 8; i++) {
//            if (myPosition.getRow() - i >= 1 && myPosition.getColumn() - i >= 1) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
//                ChessPiece newPiece = board.getPiece(newPosition);
//                if (newPiece == null) {
//                    return_list.add(new ChessMove(myPosition, newPosition, null));
//                } else {
//                    if (newPiece.getTeamColor() != Color) {
//                        return_list.add(new ChessMove(myPosition, newPosition, null));
//                    }
//                    break;
//                }
//            }
//        }


        return returnList;
    }
}

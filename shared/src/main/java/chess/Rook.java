package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Rook {

    private final ChessGame.TeamColor Color;

    public Rook(ChessGame.TeamColor Color) {
        this.Color = Color;
    }


    public Collection<ChessMove> R_Moves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> return_list = new ArrayList<>();
        for (int i = 1; i < 9; i++) {

            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
            ChessPiece newPiece = board.getPiece(newPosition);
            if (myPosition.getRow() - i < 0) {
                break;
            } else {
                if (newPiece == null) {
                    return_list.add(new ChessMove(myPosition, newPosition, null));
                }

                if (newPiece != null) {
                    if (newPiece.getTeamColor() != Color) {
                        return_list.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;

                }
            }

        }
        for (int i = 1; i < 9; i++) {

            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);
            ChessPiece newPiece = board.getPiece(newPosition);
            if (myPosition.getColumn() - i < 0) {
                break;
            } else {
                if (newPiece == null) {
                    return_list.add(new ChessMove(myPosition, newPosition, null));
                }

                if (newPiece != null) {
                    if (newPiece.getTeamColor() != Color) {
                        return_list.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;

                }
            }

        }
        for (int i = 1; i < 9; i++) {

            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
            ChessPiece newPiece = board.getPiece(newPosition);
            if (myPosition.getRow() + i > 9) {
                break;
            } else {
                if (newPiece == null) {
                    return_list.add(new ChessMove(myPosition, newPosition, null));
                }

                if (newPiece != null) {
                    if (newPiece.getTeamColor() != Color) {
                        return_list.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;

                }
            }

        }
        for (int i = 1; i < 9; i++) {

            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);
            ChessPiece newPiece = board.getPiece(newPosition);
            if (myPosition.getColumn() + i > 9) {
                break;
            } else {
                if (newPiece == null) {
                    return_list.add(new ChessMove(myPosition, newPosition, null));
                }

                if (newPiece != null) {
                    if (newPiece.getTeamColor() != Color) {
                        return_list.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;

                }
            }

        }

        return return_list;
    }

}

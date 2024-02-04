package chess;
import java.util.ArrayList;
import java.util.Collection;
public class Pawn {

    private final ChessGame.TeamColor Color;

    public Pawn(ChessGame.TeamColor Color) {
        this.Color = Color;
    }


    public Collection<ChessMove> P_Moves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int direction;
        int startRow;
        int promotionRow;

        if (this.Color == ChessGame.TeamColor.WHITE) {
            direction = 1;//White pawns
            startRow = 2;
            promotionRow = 8;
        } else {
            direction = -1; // Black pawns
            startRow = 7;
            promotionRow = 1;
        }

        // Single move forward
        addMoveIfValid(board, myPosition, row + direction, col, returnList, promotionRow);

        // move up 2 if not change from initial position
        if (row == startRow && board.getPiece(new ChessPosition(row + direction, col)) == null) {
            addMoveIfValid(board, myPosition, row + (2 * direction), col, returnList, promotionRow);
        }

        // each option for capturing a piece diagnolly
        CaptureMove(board, myPosition, row + direction, col - 1, returnList, promotionRow);
        CaptureMove(board, myPosition, row + direction, col + 1, returnList, promotionRow);

        return returnList;
    }

    private void addMoveIfValid(ChessBoard board, ChessPosition fromPosition, int Row, int Col, Collection<ChessMove> moves, int promotionRow) {
        if (ChessPiece.inTheBounds(Row, Col) && board.getPiece(new ChessPosition(Row, Col)) == null) {
            ChessPosition toPosition = new ChessPosition(Row, Col);
            if (Row == promotionRow) {
                promotionMoves(fromPosition, toPosition, moves);
            } else {
                moves.add(new ChessMove(fromPosition, toPosition, null));
            }
        }
    }

    private void CaptureMove(ChessBoard board, ChessPosition fromPosition, int toRow, int toCol, Collection<ChessMove> moves, int promotionRow) {
        if (ChessPiece.inTheBounds(toRow, toCol)){
            ChessPosition toPosition = new ChessPosition(toRow, toCol);
            ChessPiece pieceAtDestination = board.getPiece(toPosition);
            if (pieceAtDestination != null && pieceAtDestination.getTeamColor() != this.Color) {
                if (toRow == promotionRow) {
                    promotionMoves(fromPosition, toPosition, moves);
                } else {
                    moves.add(new ChessMove(fromPosition, toPosition, null));
                }
            }
        }
    }

    private void promotionMoves(ChessPosition fromPosition, ChessPosition toPosition, Collection<ChessMove> moves) {
        moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.KNIGHT));
    }

}

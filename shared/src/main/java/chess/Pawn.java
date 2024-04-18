package chess;
import java.util.ArrayList;
import java.util.Collection;
public class Pawn {

    private final ChessGame.TeamColor playerColor;

    public Pawn(ChessGame.TeamColor pieceColor) {
        this.playerColor = pieceColor;
    }


    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> returnList = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int direction;
        int startRow;
        int promotionRow;

        if (this.playerColor == ChessGame.TeamColor.WHITE) {
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
        captureMove(board, myPosition, row + direction, col - 1, returnList, promotionRow);
        captureMove(board, myPosition, row + direction, col + 1, returnList, promotionRow);

        return returnList;
    }

    private void addMoveIfValid(ChessBoard board, ChessPosition fromPosition, int row, int col, Collection<ChessMove> moves, int promotionRow) {
        if (ChessPiece.inTheBounds(row, col) && board.getPiece(new ChessPosition(row, col)) == null) {
            ChessPosition toPosition = new ChessPosition(row, col);
            if (row == promotionRow) {
                promotionMoves(fromPosition, toPosition, moves);
            } else {
                moves.add(new ChessMove(fromPosition, toPosition, null));
            }
        }
    }

    private void captureMove(ChessBoard board, ChessPosition fromPosition, int row, int col, Collection<ChessMove> moves, int promotionRow) {
        if (ChessPiece.inTheBounds(row, col)){
            ChessPosition toPosition = new ChessPosition(row, col);
            ChessPiece pieceAtDestination = board.getPiece(toPosition);
            if (pieceAtDestination != null && pieceAtDestination.getTeamColor() != this.playerColor) {
                if (row == promotionRow) {
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

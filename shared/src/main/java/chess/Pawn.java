package chess;
import java.util.ArrayList;
import java.util.Collection;
public class Pawn {

    private final ChessGame.TeamColor Color;

    public Pawn(ChessGame.TeamColor Color) {
        this.Color = Color;
    }


    public Collection<ChessMove> P_Moves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> returnList = new ArrayList<>();
        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();
        int direction;
        int startRow;
        int promotionRow;

        if (this.Color == ChessGame.TeamColor.WHITE) {
            direction = 1;//WHite pawns
            startRow = 2;
            promotionRow = 8;
        } else {
            direction = -1; // Black pawns
            startRow = 7;
            promotionRow = 1;
        }

        // Single move forward
        addMoveIfValid(board, myPosition, currentRow + direction, currentCol, returnList, promotionRow);

        // move up 2 if it hasn't change from initial position
        if (currentRow == startRow && board.getPiece(new ChessPosition(currentRow + direction, currentCol)) == null) {
            addMoveIfValid(board, myPosition, currentRow + (2 * direction), currentCol, returnList, promotionRow);
        }

        // each option for capturing a piece diagnolly
        CaptureMove(board, myPosition, currentRow + direction, currentCol - 1, returnList, promotionRow);
        CaptureMove(board, myPosition, currentRow + direction, currentCol + 1, returnList, promotionRow);

        return returnList;
    }

    private void addMoveIfValid(ChessBoard board, ChessPosition fromPosition, int toRow, int toCol, ArrayList<ChessMove> moves, int promotionRow) {
        if (inTheBounds(toRow, toCol) && board.getPiece(new ChessPosition(toRow, toCol)) == null) {
            ChessPosition toPosition = new ChessPosition(toRow, toCol);
            if (toRow == promotionRow) {
                promotionMoves(fromPosition, toPosition, moves);
            } else {
                moves.add(new ChessMove(fromPosition, toPosition, null));
            }
        }
    }

    private void CaptureMove(ChessBoard board, ChessPosition fromPosition, int toRow, int toCol, ArrayList<ChessMove> moves, int promotionRow) {
        if (inTheBounds(toRow, toCol)) {
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

    private void promotionMoves(ChessPosition fromPosition, ChessPosition toPosition, ArrayList<ChessMove> moves) {
        moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.KNIGHT));
    }

    private boolean inTheBounds(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}

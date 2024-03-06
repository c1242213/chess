package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", promotionPiece=" + promotionPiece +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(getStartPosition(), chessMove.getStartPosition()) && Objects.equals(getEndPosition(), chessMove.getEndPosition()) && getPromotionPiece() == chessMove.getPromotionPiece();
    }

    public static void specMove(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition, Collection<ChessMove> return_list, int[][] moves) {
        for (int i = 0; i < 8; i++) {
            int newRow = myPosition.getRow() + moves[i][0];
            int newCol = myPosition.getColumn() + moves[i][1];

            if (ChessPiece.inTheBounds(newRow, newCol)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece newPiece = board.getPiece(newPosition);
                if (newPiece == null || newPiece.getTeamColor() != color) {
                    return_list.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartPosition(), getEndPosition(), getPromotionPiece());
    }
}

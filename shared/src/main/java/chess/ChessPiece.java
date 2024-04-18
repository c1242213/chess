package chess;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allMoves = null;
        switch (this.type) {
            case BISHOP:
                Bishop bishop = new Bishop(this.pieceColor);
                allMoves = bishop.bishopMove(board, myPosition, this);
                break;
            //Bishop.B_moves
            case KING:
                King king = new King(this.pieceColor);
                allMoves = king.kingMoves(board, myPosition);
                break;
            case KNIGHT:
                Knight knight = new Knight(this.pieceColor);
                allMoves = knight.knightMoves(board, myPosition);
                break;
            case QUEEN:
                Queen queen = new Queen(this.pieceColor);
                allMoves = queen.queenMoves(board,myPosition, this);
                break;
            case ROOK:
                Rook rook = new Rook(this.pieceColor);
                allMoves = rook.rookMoves(board,myPosition,this);
                break;
            case PAWN:
                Pawn pawn = new Pawn(this.pieceColor);
                allMoves = pawn.pawnMoves(board,myPosition);
                break;
        }
        return allMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
    public void moveInDirection(ChessPosition myPosition, ChessBoard board, Collection<ChessMove> returnList, int deltaRow, int deltaCol){

        for (int i = 1; i < 8; i++) {
            int newRow = myPosition.getRow() + deltaRow*i;
            int newCol = myPosition.getColumn() + deltaCol*i;
            if (ChessPiece.inTheBounds(newRow,newCol)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece newPiece = board.getPiece(newPosition);

                if (newPiece == null) {
                    returnList.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    if (newPiece.getTeamColor() != this.pieceColor) {
                        returnList.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
            }
        }
    }
    public static void specMove(ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition myPosition, Collection<ChessMove> return_list, int[][] moves) {
        for (int i = 0; i < 8; i++) {
            int newRow = myPosition.getRow() + moves[i][0];
            int newCol = myPosition.getColumn() + moves[i][1];

            if (ChessPiece.inTheBounds(newRow, newCol)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece newPiece = board.getPiece(newPosition);
                if (newPiece == null || newPiece.getTeamColor() != pieceColor) {
                    return_list.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }
    public static boolean inTheBounds(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}

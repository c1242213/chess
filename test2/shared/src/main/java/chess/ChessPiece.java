package chess;

import java.net.CookieHandler;
import java.util.ArrayList;
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
    private final ChessPiece.PieceType type;

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

        switch (this.type){
            case BISHOP:
                Bishop bishop = new Bishop(this.pieceColor);
                allMoves = bishop.moves(board, myPosition, this);
                break;
            case QUEEN:
                Queen queen = new Queen(this.pieceColor);
                allMoves = queen.moves(board, myPosition, this);
                break;
            case ROOK:
                Rook rook = new Rook(this.pieceColor);
                allMoves = rook.moves(board, myPosition, this);
                break;
            case KNIGHT:
                Knight knight = new Knight(this.pieceColor);
                allMoves = knight.moves(board, myPosition);
                break;
            case KING:
                King king = new King(this.pieceColor);
                allMoves = king.moves(board, myPosition);
                break;
            case PAWN:
                Pawn pawn = new Pawn(this.pieceColor);
                allMoves = pawn.moves(board, myPosition);
                break;
        }



        return allMoves;
    }

    public void moveInDirection(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> returnList, int deltaRow, int deltaCol){

        for(int i = 1; i < 8; i++){
            int newRow = myPosition.getRow() + deltaRow*i;
            int newCol = myPosition.getColumn() + deltaCol*i;
            if(newRow>=1 && newRow<=8 && newCol>=1 && newCol<=8) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece newPiece = board.getPiece(newPosition);
                if(newPiece == null){
                    returnList.add(new ChessMove(myPosition, newPosition, null));
                }else{
                    if(newPiece.getTeamColor() != this.pieceColor){
                        returnList.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
            }
        }
    }



    @Override
    public String toString() {
        return "ChessPiece{}";
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
}

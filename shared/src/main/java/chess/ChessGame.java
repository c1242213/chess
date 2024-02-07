package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor team;
    private ChessBoard board;

    public ChessGame() {
        this.team = TeamColor.WHITE;
        this.board = new ChessBoard();

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //after you make a move you're not in check
        ChessPiece thisPiece = board.getPiece(startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();
        if (thisPiece != null){
            Collection<ChessMove> returnMoves = thisPiece.pieceMoves(board, startPosition);
            for(ChessMove move: returnMoves) {
                ChessBoard newBoard = cloneBoard(board);
                board.addPiece(move.getEndPosition(), thisPiece);
                board.addPiece(startPosition, null);
                if(!isInCheck(thisPiece.getTeamColor())){
                    validMoves.add(move);
                }
                board = newBoard;
            }
        }

        return validMoves;
    }

    public ChessBoard cloneBoard(ChessBoard board){
        ChessBoard testBoard = new ChessBoard();

        for(int col = 1; col < 9; col++){
            for(int row = 1; row < 9; row++){
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece newPiece = board.getPiece(newPosition);
                if(newPiece != null){
                    testBoard.addPiece(newPosition, newPiece);
                }
            }
        }
        return testBoard;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if(moves == null){
            throw new InvalidMoveException();
        }
        if (board.getPiece(move.getStartPosition()).getTeamColor() != team){
            throw new InvalidMoveException();
        }
        if(moves.contains(move)) {

            if(move.getPromotionPiece() != null){
                ChessPiece promotionPiece = new ChessPiece(team, move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), promotionPiece);
                board.addPiece(move.getStartPosition(), null);

            }else {
                ChessPiece thisPiece = board.getPiece(move.getStartPosition());
                board.addPiece(move.getEndPosition(), thisPiece);
                board.addPiece(move.getStartPosition(), null);
            }

        }
        if(!(moves.contains(move))){
            throw new InvalidMoveException();
        }
        if(this.team == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        else{
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean check = false;
        Collection<ChessMove> oppMoves = oppMoves(board, teamColor);
        for(ChessMove move: oppMoves){
            if(move.getEndPosition().equals(ifKing(board, teamColor))){
                check = true;
            }
        }
        return check;
    }

    public Collection<ChessMove> oppMoves(ChessBoard board, TeamColor teamColor) {
        Collection<ChessMove> allMoves = new HashSet<>();
        for (int col = 1; col < 9; col++) {
            for (int row = 1; row < 9; row++) {
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece newPiece = board.getPiece(newPosition);
                if (board.getPiece(newPosition) != null) {
                    if (newPiece.getTeamColor() != teamColor) {
                        allMoves.addAll(newPiece.pieceMoves(board, newPosition));
                    }
                }
            }
        }
        return allMoves;
    }

    public ChessPosition ifKing(ChessBoard board, TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (int col = 1; col < 9; col++) {
            for (int row = 1; row < 9; row++) {
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece newPiece = board.getPiece(newPosition);
                if (board.getPiece(newPosition) != null) {
                    if (newPiece.getPieceType() == ChessPiece.PieceType.KING && newPiece.getTeamColor() == teamColor) {
                        kingPosition = newPosition;
                    }
                }
            }
        }
        return kingPosition;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }

        for (int col = 1; col < 9; col++) {
            for (int row = 1; row < 9; row++) {
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece newPiece = board.getPiece(newPosition);
                if(newPiece != null && newPiece.getTeamColor() == teamColor){
                    Collection<ChessMove> validMoves = validMoves(newPosition);
                    if (!(validMoves.isEmpty())) {
                        return false;
                    }
                }
            }
        }
        return true;

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }


        for (int col = 1; col <= 8; col++) {
            for (int row = 1; row <= 8; row++) {
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece newPiece = board.getPiece(newPosition);

                if (newPiece != null && newPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(newPosition);

                    if (!moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

}

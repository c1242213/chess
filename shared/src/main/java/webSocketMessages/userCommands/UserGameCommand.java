package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    CommandType commandType;
    int gameID;
    String authT;
    ChessMove move;

    ChessGame.TeamColor playerColor;

    public UserGameCommand(CommandType commandType, int gameID, String authT, ChessMove move, ChessGame.TeamColor playerColor) {
        this.commandType = commandType;
        this.gameID = gameID;
        this.authT = authT;
        this.move = move;
        this.playerColor = playerColor;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }




    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public ChessMove getMove() {
        return move;
    }

    public String getAuthString() {
        return authT;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
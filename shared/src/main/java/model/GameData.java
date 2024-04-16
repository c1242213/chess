package model;

import chess.ChessGame;

public class GameData {
    private int gameID;
    private static String whiteUsername;
    private static String blackUsername;
    private String gameName;
    private static ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }
    public GameData(){
        this.gameID = 0;
        this.whiteUsername = null;
        this.blackUsername = null;
        this.gameName = null;
        this.game = new ChessGame();
    }

    public int getGameID() {
        return gameID;
    }

    public static String getWhiteUsername() {
        return whiteUsername;
    }

    public static String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }



    public static ChessGame getGame(){return game;}

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setGameName(String gameName) {
    }

    public void addParticipant(String color, String username) {
    }

    public void setBlackName(String blackUser) {
    }

    public void setWhiteName(String whiteUser) {
    }

    public void setGame(ChessGame game) {
    }
}
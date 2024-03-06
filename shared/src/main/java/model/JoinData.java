package model;

public class JoinData {

    String playerColor;
    Integer gameID;

    public JoinData(String playerColor, Integer gameID){
        this.playerColor = playerColor;
        this.gameID = gameID;

    }

    public int getGameID() {
        return gameID;
    }

    public String getPlayerColor(){return playerColor;}

}

package model;

import java.util.HashSet;
import java.util.List;
public class ListGameData{
    HashSet<GameData> games;

    public ListGameData(HashSet<GameData> games) {
        this.games = games;
    }

    public HashSet<GameData> returnGames(){
        return games;
    }
}
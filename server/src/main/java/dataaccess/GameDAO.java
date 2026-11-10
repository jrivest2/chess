package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {


    int getSize();

    int createGame(String gameName);

    void joinGame(String playerColor, int gameID, String userName);

    GameData getGame(int gameID);

    HashMap<String, GameData> listGames();

    void clearDB();
}

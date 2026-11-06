package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {
    static HashMap<String, GameData> gameDataTable = new HashMap<>();

    @Override
    public int getSize() {
        return gameDataTable.size();
    }

    @Override
    public int createGame(String gameName) {
        Random rand = new Random();
        int gameID = rand.nextInt((10000 - 1) + 1) + 1;
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDataTable.put(Integer.toString(gameID),gameData);
        return gameID;
    }

    @Override
    public void clearDB() {
        gameDataTable.clear();
    }
}

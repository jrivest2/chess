package dataaccess.sql;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;

import java.util.HashMap;
import java.util.Random;

public class SqlGameDAO implements GameDAO {
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
    public void joinGame(String playerColor, int gameID, String userName) {
        String gameIDString = String.valueOf(gameID);
        GameData current = gameDataTable.get(gameIDString);
        if (playerColor.equals("white")) {
            GameData newGame = new GameData(gameID, userName, current.blackUsername(), current.gameName(), current.game());
            gameDataTable.put(gameIDString, newGame);
        } else {
            GameData newGame = new GameData(gameID, current.whiteUsername(), userName, current.gameName(), current.game());
            gameDataTable.put(gameIDString, newGame);
        }
    }

    @Override
    public GameData getGame(int gameID) {
        String gameIDString = String.valueOf(gameID);
        return gameDataTable.get(gameIDString);
    }

    @Override
    public HashMap<String, GameData> listGames() {
        return gameDataTable;
    }

    @Override
    public void clearDB() {
        gameDataTable.clear();
    }
}

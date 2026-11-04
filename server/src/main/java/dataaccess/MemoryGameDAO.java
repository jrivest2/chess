package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    static HashMap<String, GameData> gameDataTable = new HashMap<>();

    @Override
    public void clearDB() {
        gameDataTable.clear();
    }
}

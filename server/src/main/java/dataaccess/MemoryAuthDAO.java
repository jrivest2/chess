package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    static HashMap<String, AuthData> authDataTable = new HashMap<>();
    @Override
    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(username, authToken);
        authDataTable.put(username,authData);
        return authToken;
    }

    @Override
    public AuthData getAuth(String username) {
        return authDataTable.get(username);
    }

    @Override
    public int getSize() {
        return authDataTable.size();
    }

    @Override
    public void clearDB() {
        authDataTable.clear();
    }

    @Override
    public void deleteAuth(String username) {
        authDataTable.remove(username);
    }
}

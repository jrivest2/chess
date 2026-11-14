package dataaccess.sql;

import dataaccess.AuthDAO;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class SqlAuthDAO implements AuthDAO {
    static HashMap<String, AuthData> authDataTable = new HashMap<>();
    @Override
    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(username, authToken);
        authDataTable.put(authToken,authData);
        return authToken;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authDataTable.get(authToken);
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
    public void deleteAuth(String authToken) {
        authDataTable.remove(authToken);
    }
}

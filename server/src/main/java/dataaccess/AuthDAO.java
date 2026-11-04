package dataaccess;

import model.AuthData;

public interface AuthDAO {

    String createAuth(String username);
    AuthData getAuth(String username);

    void clearDB();
}

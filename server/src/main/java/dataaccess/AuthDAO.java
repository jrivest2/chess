package dataaccess;

import model.AuthData;

public interface AuthDAO {

    String createAuth(String username);
    AuthData getAuth(String username);
    int getSize();
    void clearDB();

    void deleteAuth(String username);
}

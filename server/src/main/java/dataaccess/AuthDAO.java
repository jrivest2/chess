package dataaccess;

import model.AuthData;

public interface AuthDAO {

    String createAuth(String username);
    AuthData getAuth(String authToken);

    int getSize();
    void clearDB();

    void deleteAuth(String username);
}

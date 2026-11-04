package dataaccess;

import model.RegisterRequest;
import model.UserData;

public interface UserDAO {
    UserData getUser(String username);
    void createUser(RegisterRequest registerRequest);
    String createAuth(String username);
}

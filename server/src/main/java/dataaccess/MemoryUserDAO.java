package dataaccess;

import model.RegisterRequest;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    static HashMap<String, UserData> userDataTable = new HashMap<>();
    @Override
    public UserData getUser(String username) {
        return userDataTable.get(username);
    }

    @Override
    public void createUser(RegisterRequest registerRequest) {
        UserData userData =  new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        userDataTable.put(registerRequest.username(),userData);
    }

    @Override
    public void clearDB() {
        userDataTable.clear();
    }
}

package dataaccess;

import model.AuthData;
import model.RegisterRequest;
import model.UserData;

import java.util.UUID;

public class MemoryUserDAO implements UserDAO {
    @Override
    public UserData getUser(String username) {
        //get UserData from memory or return null
        return null;
    }

    @Override
    public void createUser(RegisterRequest registerRequest) {
        UserData userData =  new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        //send userData to memory
    }

    @Override
    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(username, authToken);
        //send authData to memory
        return authToken;
    }
}

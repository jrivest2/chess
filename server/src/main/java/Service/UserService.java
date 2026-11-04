package Service;

import dataaccess.*;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;

import java.util.Objects;

public class UserService {
    UserDAO userDao = new MemoryUserDAO();
    AuthDAO authDao = new MemoryAuthDAO();
    GameDAO gameDao = new MemoryGameDAO();
    public void clearDB() {
        try {
        userDao.clearDB();
        authDao.clearDB();
        gameDao.clearDB();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong");
        }
    }
    public RegisterResult register(RegisterRequest registerRequest) {
        if (Objects.equals(registerRequest.username(), "") || registerRequest.username() == null
                || Objects.equals(registerRequest.password(), "") || registerRequest.password() == null
                || Objects.equals(registerRequest.email(), "") || registerRequest.email() == null) {
            throw new InvalidArgumentException("Please fill every field.");
        }

        UserData userData = userDao.getUser(registerRequest.username());
        if (userData == null) {
            userDao.createUser(registerRequest);
            String authToken = authDao.createAuth(registerRequest.username());

            return new RegisterResult(registerRequest.username(), authToken);
        }
        throw new AlreadyTakenException("The username: \"" + registerRequest.username() + "\" is already taken.");
    }
}

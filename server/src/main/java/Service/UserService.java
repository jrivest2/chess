package Service;

import dataaccess.*;
import model.*;

import java.util.Objects;

public class UserService {
    UserDAO userDao = new MemoryUserDAO();
    AuthDAO authDao = new MemoryAuthDAO();
    GameDAO gameDao = new MemoryGameDAO();
    public int[] getDBSizes() {
        return new int[]{userDao.getSize(),authDao.getSize(),gameDao.getSize()};
    }
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

    public LoginResult login(LoginRequest request) {
        UserData user = userDao.getUser(request.username());
        if (user == null) {
            throw new IncorrectCredentialsException("Incorrect username or password");
        }
        if (authDao.getAuth(request.username()) != null) {
            throw new IncorrectCredentialsException("Already Logged In");
        }
        if (Objects.equals(user.password(), request.password())) {
            String authToken = authDao.createAuth(user.username());
            return new LoginResult(user.username(), authToken);
        } else {
            throw new IncorrectCredentialsException("Incorrect username or password");
        }
     }

    public void logout(LogoutRequest request) {
        AuthData authData = authDao.getAuth(request.username());
        if (authData == null) {
            throw new IncorrectCredentialsException("Login Session not found");
        }
        if (Objects.equals(request.authToken(), authData.authToken())) {
            authDao.deleteAuth(authData.username());
        } else {
            throw new IncorrectCredentialsException("Login Session not found");
        }
    }
}

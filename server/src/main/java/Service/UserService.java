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
            throw new InvalidArgumentException("Error: bad request");
        }

        UserData userData = userDao.getUser(registerRequest.username());
        if (userData == null) {
            userDao.createUser(registerRequest);
            String authToken = authDao.createAuth(registerRequest.username());

            return new RegisterResult(registerRequest.username(), authToken);
        }
        throw new AlreadyTakenException("Error: The username: \"" + registerRequest.username() + "\" is already taken.");
    }

    public LoginResult login(LoginRequest request) {
        if (request.username() == null || request.password() == null) {
            throw new InvalidArgumentException("Error: Bad Request");
        }
        UserData user = userDao.getUser(request.username());
        if (user == null) {
            throw new IncorrectCredentialsException("Error: unauthorized");
        }
        if (authDao.getAuth(request.username()) != null) {
            throw new IncorrectCredentialsException("Already Logged In");
        }
        if (Objects.equals(user.password(), request.password())) {
            String authToken = authDao.createAuth(user.username());
            return new LoginResult(user.username(), authToken);
        } else {
            throw new IncorrectCredentialsException("Error: unauthorized");
        }
     }

    public void logout(LogoutRequest request) {
        if (request.authToken() == null) {
            throw new IncorrectCredentialsException("Error: Login Session not found");
        }
        AuthData authData = authDao.getAuth(request.authToken());
        if (authData == null) {
            throw new IncorrectCredentialsException("Error: Login Session not found");
        }
        if (Objects.equals(request.authToken(), authData.authToken())) {
            authDao.deleteAuth(authData.authToken());
        } else {
            throw new IncorrectCredentialsException("Error: Login Session not found");
        }
    }
    public CreateGameResult createGame(CreateGameRequest request) {
        if (request.authToken() == null) {
            throw new IncorrectCredentialsException("Error: unauthorized");
        }
        if (authDao.getAuth(request.authToken()) != null) {
            if (request.gameName() == null || request.gameName().isEmpty()) {
                throw new InvalidArgumentException("Error: No Game Name provided");
            } else {
                int gameID = gameDao.createGame(request.gameName());
                return new CreateGameResult(gameID);
            }
        } else {
            throw new IncorrectCredentialsException("Error: unauthorized");
        }
    }
}

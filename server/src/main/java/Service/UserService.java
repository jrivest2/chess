package Service;

import dataaccess.AlreadyTakenException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;

import java.util.Objects;

public class UserService {
    MemoryUserDAO userDao = new MemoryUserDAO();
    MemoryAuthDAO authDao = new MemoryAuthDAO();
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

package Service;

import dataaccess.AlreadyTakenException;
import dataaccess.MemoryUserDAO;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;

public class UserService {
    MemoryUserDAO dao = new MemoryUserDAO();

    public RegisterResult register(RegisterRequest registerRequest) {
        UserData userData = dao.getUser(registerRequest.username());
        if (userData == null) {
            dao.createUser(registerRequest);
            String authToken = dao.createAuth(registerRequest.username());

            return new RegisterResult(registerRequest.username(), authToken);
        }
        throw new AlreadyTakenException("The username: \"" + registerRequest.username() + "\" is already taken.");
    }
}

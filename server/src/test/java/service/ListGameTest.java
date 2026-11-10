package service;

import dataaccess.IncorrectCredentialsException;
import model.CreateGameRequest;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListGameTest {
    UserService userService = new UserService();
    String authToken;

    @BeforeEach
    public void setup() {
        userService.clearDB();
        RegisterRequest test = new RegisterRequest("testUser", "testPassword", "test@email");
        RegisterResult regRes = userService.register(test);
        CreateGameRequest gameReq = new CreateGameRequest(regRes.authToken(), "testGame");
        userService.createGame(gameReq);
        authToken = regRes.authToken();
    }

    @Test
    public void successfulTest() {
        userService.listGames(authToken);
    }

    @Test
    public void failedTest() {
        userService.listGames(authToken);
        userService.clearDB();
        assertThrows(IncorrectCredentialsException.class, () -> userService.listGames(authToken));
    }
}
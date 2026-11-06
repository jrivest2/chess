package service;

import dataaccess.IncorrectCredentialsException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameTest {
    UserService userService = new UserService();

    @BeforeEach
    public void setup() {
        userService.clearDB();
    }

    @Test
    public void successfulTest() {
        RegisterRequest testReg = new RegisterRequest("testUser", "testPassword", "test@email");
        RegisterResult testUserResult = userService.register(testReg);
        CreateGameRequest testRequest = new CreateGameRequest(testUserResult.authToken(),"testGame");
        CreateGameResult testResult = userService.createGame(testRequest);

        int[] sizes = userService.getDBSizes();
        assertEquals(1, sizes[0]);
        assertEquals(1, sizes[1]);
        assertEquals(1, sizes[2]);
        assertNotNull(testResult);
        assertTrue(testResult.gameID() != 0);
    }

    @Test
    public void failingTest() {
        RegisterRequest testReg = new RegisterRequest("testUser", "testPassword", "test@email");
        RegisterResult testUserResult = userService.register(testReg);
        CreateGameRequest testRequest = new CreateGameRequest(null,"testGame");
        CreateGameRequest testRequest2 = new CreateGameRequest("badAuth","testGame");
        CreateGameRequest testRequest3 = new CreateGameRequest(testUserResult.authToken(), null);
        CreateGameRequest testRequest4 = new CreateGameRequest(testUserResult.authToken(), "");


        assertThrows(IncorrectCredentialsException.class, () -> userService.createGame(testRequest));
        assertThrows(IncorrectCredentialsException.class, () -> userService.createGame(testRequest2));
        assertThrows(InvalidArgumentException.class, () -> userService.createGame(testRequest3));
        assertThrows(InvalidArgumentException.class, () -> userService.createGame(testRequest4));
    }
}
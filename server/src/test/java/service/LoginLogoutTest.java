package service;

import dataaccess.IncorrectCredentialsException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginLogoutTest {

    UserService userService = new UserService();
    RegisterRequest test;
    RegisterResult result;
    String registerToken;
    @BeforeEach
    public void setup() {
        userService.clearDB();
        test = new RegisterRequest("testUser", "testPassword", "test@email");
        result = userService.register(test);
        registerToken = result.authToken();
    }


    @Test
    public void successfulLogoutTest() {
        int authTableSize = userService.getDBSizes()[1];
        userService.logout(new LogoutRequest(registerToken));
        int authTableLogoutSize = userService.getDBSizes()[1];

        assertTrue(authTableSize != authTableLogoutSize);
        assertThrows(IncorrectCredentialsException.class, () -> userService.logout(new LogoutRequest(registerToken)));
    }
    @Test
    public void successfulLoginTest() {
        userService.logout(new LogoutRequest(result.authToken()));
        LoginResult loginResult = userService.login(new LoginRequest("testUser", "testPassword"));
        assertNotNull(loginResult);
        assertEquals("testUser", loginResult.username());
        assertNotSame(loginResult.authToken(), registerToken);
    }

    @Test
    public void failedLoginTest() {

        userService.logout(new LogoutRequest(registerToken));
        assertThrows(IncorrectCredentialsException.class, () -> userService.login(new LoginRequest("", registerToken)));
        assertThrows(IncorrectCredentialsException.class, () -> userService.login(new LoginRequest("testUser", "")));
    }
}

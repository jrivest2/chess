package service;

import dataaccess.AlreadyTakenException;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//        assertEquals(200, 100 + 100);
//        assertTrue(100 == 2 * 50);
//        assertNotNull(new Object(), "Response did not return authentication String");
//        assertThrows(InvalidArgumentException.class, () -> {
//            throw new InvalidArgumentException();
//        });

public class RegisterTest {
    UserService userService = new UserService();

    @BeforeEach
    public void setup() {
        userService.clearDB();
    }

    @Test
    public void successfulTest() {
        RegisterRequest test = new RegisterRequest("testUser", "testPassword", "test@email");
        RegisterResult result = userService.register(test);
        assertEquals("testUser", result.username());
    }

    @Test
    public void failedTest() {
        RegisterRequest test = new RegisterRequest("testUser2", "testPassword", "test2@email");
        userService.register(test);
        assertThrows(AlreadyTakenException.class, () -> userService.register(test));
    }

    @Test
    public void missingInputTest() {
        RegisterRequest test1 = new RegisterRequest("", "testPassword", "test2@email");
        RegisterRequest test2 = new RegisterRequest("testUser3", "", "test2@email");
        RegisterRequest test3 = new RegisterRequest("testUser4", "testPassword", "");
        RegisterRequest test4 = new RegisterRequest(null, "testPassword", "test4@email");
        RegisterRequest test5 = new RegisterRequest("testUser4", null, "test5@email");
        RegisterRequest test6 = new RegisterRequest("testUser4", "testPassword", null);
        assertThrows(InvalidArgumentException.class, () -> userService.register(test1));
        assertThrows(InvalidArgumentException.class, () -> userService.register(test2));
        assertThrows(InvalidArgumentException.class, () -> userService.register(test3));
        assertThrows(InvalidArgumentException.class, () -> userService.register(test4));
        assertThrows(InvalidArgumentException.class, () -> userService.register(test5));
        assertThrows(InvalidArgumentException.class, () -> userService.register(test6));
    }
}
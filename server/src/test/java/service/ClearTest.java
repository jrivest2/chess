package service;

import Service.UserService;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearTest {
    UserService userService = new UserService();

    @BeforeEach
    public void setup() {
        userService.clearDB();
    }

    @Test
    public void successfulTest() {
        RegisterRequest test = new RegisterRequest("testUser", "testPassword", "test@email");
        RegisterResult result = userService.register(test);
        userService.clearDB();
        int[] sizes = userService.getDBSizes();
        assertEquals(0, sizes[0]);
        assertEquals(0, sizes[1]);
        assertEquals(0, sizes[2]);
    }
}

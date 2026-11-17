package service;

import dataaccess.DataAccessException;
import dataaccess.sql.ResponseException;
import model.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearTest {
    UserService userService = new UserService();

    public ClearTest() throws ResponseException, DataAccessException {
    }

    @BeforeEach
    public void setup() {
        userService.clearDB();
    }

    @Test
    public void successfulTest() {
        RegisterRequest test = new RegisterRequest("testUser", "testPassword", "test@email");
        userService.register(test);
        int[] sizes = userService.getDBSizes();
        assertEquals(1, sizes[0]);
        assertEquals(1, sizes[1]);
        assertEquals(1, sizes[2]);
        RegisterRequest test2 = new RegisterRequest("testUser2", "testPassword", "test2@email");
        userService.register(test2);
        sizes = userService.getDBSizes();
        assertEquals(2, sizes[0]);
        assertEquals(2, sizes[1]);
        assertEquals(2, sizes[2]);
        userService.clearDB();
        assertEquals(0, sizes[0]);
        assertEquals(0, sizes[1]);
        assertEquals(0, sizes[2]);
    }
}

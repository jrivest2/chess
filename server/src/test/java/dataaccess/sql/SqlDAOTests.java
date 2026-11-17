package dataaccess.sql;

import dataaccess.DataAccessException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

public class SqlDAOTests {
        UserService userService = new UserService();

    public SqlDAOTests() throws DataAccessException {
    }

    @BeforeEach
    public void setup() {
        userService.clearDB();
    }

    @Test
    public void SQLTest()  {
        try {
            CreateGameTest createGameTest = new CreateGameTest();
            createGameTest.successfulTest();
            userService.clearDB();
            createGameTest.failingTest();
            userService.clearDB();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            RegisterTest registerTest = new RegisterTest();
            registerTest.failedTest();
            userService.clearDB();
            registerTest.missingInputTest();
            userService.clearDB();
            registerTest.successfulTest();
            userService.clearDB();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            ClearTest clearTest = new ClearTest();
            clearTest.successfulTest();
            userService.clearDB();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        JoinGameTest joinGameTest = new JoinGameTest();
        userService.clearDB();
        joinGameTest.setup();
        joinGameTest.failedTest();
        userService.clearDB();
        joinGameTest.setup();
        joinGameTest.successfulTestBlack();
        userService.clearDB();
        joinGameTest.setup();
        joinGameTest.successfulTestWhite();
        userService.clearDB();
        try {
            ListGameTest listGameTest = new ListGameTest();
            listGameTest.setup();
            listGameTest.failedTest();
            userService.clearDB();
            listGameTest.setup();
            listGameTest.successfulTest();
            userService.clearDB();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            LoginLogoutTest loginLogoutTest = new LoginLogoutTest();
            loginLogoutTest.setup();
            loginLogoutTest.successfulLogoutTest();
            userService.clearDB();
            loginLogoutTest.setup();
            loginLogoutTest.failedLoginTest();
            userService.clearDB();
            loginLogoutTest.setup();
            loginLogoutTest.successfulLoginTest();
            userService.clearDB();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            RegisterTest registerTest = new RegisterTest();
            registerTest.successfulTest();
            userService.clearDB();
            registerTest.missingInputTest();
            userService.clearDB();
            registerTest.failedTest();
            userService.clearDB();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

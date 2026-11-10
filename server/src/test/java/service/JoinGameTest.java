package service;

import dataaccess.IncorrectCredentialsException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameTest {
    UserService userService = new UserService();
    String authToken;
    int gameID;
    @BeforeEach
    public void setup() {
        userService.clearDB();
        RegisterRequest test = new RegisterRequest("testUser", "testPassword", "test@email");
        RegisterResult regRes = userService.register(test);
        CreateGameRequest gameReq = new CreateGameRequest(regRes.authToken(), "testGame");
        CreateGameResult createRes = userService.createGame(gameReq);
        authToken = regRes.authToken();
        gameID = createRes.gameID();
    }

    @Test
    public void successfulTestWhite() {
        JoinGameRequest joinReq = new JoinGameRequest(authToken, "white", gameID);
        userService.joinGame(joinReq);

        ListGameResult listRes = userService.listGames(authToken);
        ArrayList<GameData> games = listRes.games();
        assertTrue(games.stream().anyMatch(game -> "testUser".equals(game.whiteUsername())));
    }

    @Test
    public void successfulTestBlack() {
        JoinGameRequest joinReq = new JoinGameRequest(authToken, "black", gameID);
        userService.joinGame(joinReq);

        ListGameResult listRes = userService.listGames(authToken);
        ArrayList<GameData> games = listRes.games();
        assertTrue(games.stream().anyMatch(game -> "testUser".equals(game.blackUsername())));
    }

    @Test
    public void failedTest() {
        JoinGameRequest joinReq = new JoinGameRequest("badAuth", "white", gameID);
        assertThrows(IncorrectCredentialsException.class, () -> userService.joinGame(joinReq));

        ListGameResult listRes = userService.listGames(authToken);
        ArrayList<GameData> games = listRes.games();
        assertFalse(games.stream().anyMatch(game -> "testUser".equals(game.whiteUsername())));
    }
}
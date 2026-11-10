package server;

import service.InvalidArgumentException;
import service.UserService;
import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.IncorrectCredentialsException;
import io.javalin.*;
import io.javalin.http.Context;
import model.*;

import java.util.Map;


public class Server {
    UserService userService = new UserService();
    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", this::addUser);
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.post("/game", this::createGame);
        javalin.get("/game", this::listGames);
        javalin.put("/game", this::joinGame);
        javalin.delete("/db", this::clearDB);
    }

    private void joinGame(Context ctx) {
        ctx.contentType("application/json");
        String token = ctx.header("Authorization");
        JoinGameRequest temp = new Gson().fromJson(ctx.body(),JoinGameRequest.class);
        JoinGameRequest request = new JoinGameRequest(token, temp.playerColor(), temp.gameID());
        try {
            userService.joinGame(request);
            ctx.result("{}");
        } catch (InvalidArgumentException e) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        } catch (IncorrectCredentialsException e) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        } catch (AlreadyTakenException e) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }

    private void listGames(Context ctx) {
        ctx.contentType("application/json");
        String token = ctx.header("Authorization");
        try {
            ListGameResult result = userService.listGames(token);
            ctx.result(new Gson().toJson(result));
        } catch (Exception e) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }

    private void createGame(Context ctx) {
        ctx.contentType("application/json");
        String token = ctx.header("Authorization");
        CreateGameRequest temp = new Gson().fromJson(ctx.body(),CreateGameRequest.class);
        CreateGameRequest request = new CreateGameRequest(token, temp.gameName());
        try {
            CreateGameResult result = userService.createGame(request);
            ctx.result(new Gson().toJson(result));
        } catch (InvalidArgumentException e) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        } catch (IncorrectCredentialsException e) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }

    //Logout Endpoint
    public void logout(Context ctx) {
        ctx.contentType("application/json");
        LogoutRequest  request = new LogoutRequest(ctx.header("Authorization"));
        try {
            userService.logout(request);
            ctx.status(200);
            ctx.result("{}");
        } catch (Exception e) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
    //Login Endpoint
    public void login(Context ctx) {
        LoginRequest  request = new Gson().fromJson(ctx.body(),LoginRequest.class);
        ctx.contentType("application/json");
        try {
            LoginResult result = userService.login(request);
            ctx.result(new Gson().toJson(result));
        } catch (IncorrectCredentialsException e) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        } catch (InvalidArgumentException e) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
    //Clear Endpoint
    public void clearDB(Context ctx) {
        ctx.contentType("application/json");
        try {
            userService.clearDB();
        } catch (Exception e) {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));        }
    }
    //Register Endpoint
    public void addUser(Context ctx) {
        RegisterRequest request = new Gson().fromJson(ctx.body(),RegisterRequest.class);
        ctx.contentType("application/json");
        try {
            RegisterResult result = userService.register(request);
            ctx.result(new Gson().toJson(result));
        } catch (AlreadyTakenException e) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        } catch (InvalidArgumentException e) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }

    }
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}

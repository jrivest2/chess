package server;

import Service.UserService;
import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
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
        javalin.delete("/db", this::clearDB);
    }

    //Logout Endpoint
    public void logout(Context ctx) {
        ctx.contentType("application/json");
        LogoutRequest  request = new Gson().fromJson(ctx.body(), LogoutRequest.class);
        try {
            userService.logout(request);
            ctx.result(new Gson().toJson("Success"));
        } catch (Exception e) {
            ctx.status(404);
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
        } catch (Exception e) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", e.getMessage())));
        }
    }
    //Clear Endpoint
    public void clearDB(Context ctx) {
        ctx.contentType("application/json");
        try {
            userService.clearDB();
            ctx.result(new Gson().toJson("{}"));
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

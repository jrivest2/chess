package server;

import Service.UserService;
import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import io.javalin.*;
import io.javalin.http.Context;
import model.RegisterRequest;
import model.RegisterResult;

import java.util.Map;

public class Server {
    UserService userService = new UserService();
    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", this::addUser);
    }

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

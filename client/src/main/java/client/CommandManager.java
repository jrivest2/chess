package client;

import java.util.HashMap;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final ServerFacade serverFacade;

    public CommandManager(CommandHandler quitFunc, ServerFacade serverFacade) {
        commands.put("register", new Command(3, "Expected: <username> <password> <email>", this::register));
        commands.put("login", new Command(2, "Expected: <username> <password>", this::login));
        commands.put("create", new Command(1, "Expected: <name of new game>", this::create));
        commands.put("list", new Command(0, "", params -> list()));
        commands.put("logout", new Command(0, "", params -> logout()));
        commands.put("observe", new Command(0, "", params -> observe()));
        commands.put("help", new Command(0, "", params -> help()));
        commands.put("quit", new Command(0, "", quitFunc));
        this.serverFacade = serverFacade;
    }

    private String help() {
        return "IMPLEMENT THIS";
    }
    private String observe() {
        return "IMPLEMENT THIS";
    }
    private String logout() {
        return "IMPLEMENT THIS";
    }
    private String list() {
        return "IMPLEMENT THIS";
    }
    private String create(String[] params) {
        return "IMPLEMENT THIS";
    }

    private String login(String[] params) {
        serverFacade.login(params);
        return "IMPLEMENT THIS";
    }

    private String register(String[] params) {
        return "IMPLEMENT THIS";
    }

    public Command get(String name) {
        return commands.get(name) == null ? commands.get("help") : commands.get(name);
    }

}

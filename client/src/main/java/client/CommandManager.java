package client;

import chess.ChessGame;
import model.GameData;
import model.ListGameResult;
import model.LoginResult;
import model.RegisterResult;
import ui.ConsoleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final ServerFacade serverFacade;
    private ArrayList<GameData> gameList;
    private final ConsoleView view;

    public CommandManager(CommandHandler quitFunc, ServerFacade serverFacade, ConsoleView view) {
        commands.put("register", new Command(3, "Expected: <username> <password> <email>", this::register));
        commands.put("login", new Command(2, "Expected: <username> <password>", this::login));
        commands.put("create", new Command(1, "Expected: <name of new game>", this::create));
        commands.put("observe", new Command(1, "Expected: <Game Number>", this::observe));
        commands.put("play", new Command(1, "Expected: <Game Number> <White | Black>", this::join));
        commands.put("list", new Command(0, "", params -> list()));
        commands.put("logout", new Command(0, "", params -> logout()));
        commands.put("help", new Command(0, "", params -> help()));
        commands.put("quit", new Command(0, "", quitFunc));
        this.serverFacade = serverFacade;
        this.view = view;
    }

    private String join(String[] params) {
        GameData game = gameList.get(Integer.parseInt(params[0]));
        if (Objects.equals(params[1], "white")) {
            view.drawBoard(game.game(), ChessGame.TeamColor.WHITE);
        } else if (params[1].equals("black")) {
            view.drawBoard(game.game(), ChessGame.TeamColor.BLACK);
        }
        return "IMPLEMENT THIS";
    }
    private String observe(String[] params) {
        GameData game = gameList.get(Integer.parseInt(params[0]));
        view.drawBoard(game.game(), ChessGame.TeamColor.WHITE);
        return "IMPLEMENT THIS";
    }

    private String help() {
        if (!serverFacade.isSignedIn()) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - exit the chess program
                    help - display possible commands
                    """;
        }
        return """
                create <NAME> - creates a new game with a given name
                list - lists all games
                play <ID> <White | Black> - joins an existing chess game as the selected color
                observe <ID> - Watch an ongoing chess game
                logout - logs out the current user to switch users or quit
                help - displays possible commands
                """;
    }
    private String logout() throws ResponseException {
        serverFacade.logout();
        return "You have logged out successfully.";
    }
    private String list() throws ResponseException {
        ListGameResult list = serverFacade.list();
        this.gameList = list.games();
        if (list.games().isEmpty()) {
            return "No games available at the moment.";
        }
        var result = new StringBuilder();
        for (int i = 0; i < list.games().size(); i++) {
            GameData game = list.games().get(i);
            String whiteUser = game.whiteUsername() != null ? game.whiteUsername() : "...";
            String blackUser = game.blackUsername() != null ? game.blackUsername() : "...";
            result.append(String.format("Game %2d:\nName: %s\nWhite User: %s\nBlack User: %s\n\n", i + 1, game.gameName(), whiteUser,
                    blackUser));
            if (i + 1 != list.games().size()) result.append("\n");
        }
        return result.toString();
    }
    private String create(String[] params) throws ResponseException {
        serverFacade.create(params);
        return "Game " + params[0] + " created.";
    }

    private String login(String[] params) throws ResponseException {
        LoginResult result = serverFacade.login(params);
        return "You are logged in as " + result.username();
    }

    private String register(String[] params) throws ResponseException {
        RegisterResult result = serverFacade.register(params);
        return "You are logged in as " + result.username();
    }



    public Command get(String name) {
        return commands.get(name) == null ? commands.get("help") : commands.get(name);
    }

}

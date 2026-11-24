package client;

import ui.ConsoleView;

import java.util.Arrays;
import java.util.Scanner;

public class Repl {
    private final ServerFacade serverFacade;
    private final ConsoleView view;
    private final CommandManager commandManager;
    public enum State {QUIT,PRE_LOGIN,POST_LOGIN}
    private State state = State.PRE_LOGIN;

    public Repl(ServerFacade serverFacade, ConsoleView view) {
        this.serverFacade = serverFacade;
        this.view = view;
        this.commandManager = new CommandManager((params)->{
            state = State.QUIT;
            return "";
        }, this.serverFacade);
    }

    public void run(Scanner scanner) {
        view.welcome();
        while (state!=State.QUIT) {
            String line = scanner.nextLine();

            try {
                parse(line);
            } catch (Throwable e) {
                view.error(e.toString());
            }
        }
        view.quit();
    }

    private void parse(String input) {
        try {
            String[] tokens = input.trim().split("\\s+");
            String cmdName = (tokens.length > 0) ? tokens[0].toLowerCase() : "";
            String[] params = Arrays.copyOfRange(tokens,1,tokens.length);
            var command = commandManager.get(cmdName);
            if (params.length < command.requiredParams()) {
                view.usage(command.usage());
            } else {
                view.success(command.handler().run(params));
            }
        } catch (Exception e) {
            view.error(e.getMessage());
        }
    }
}

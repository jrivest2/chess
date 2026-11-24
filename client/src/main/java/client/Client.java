package client;

import java.util.Scanner;

public class Client {
    private final Repl repl = new Repl(new ServerFacade(), new ConsoleView());
    public void run() {
        repl.run(new Scanner(System.in));
    }
}

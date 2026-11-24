package client;

import ui.ConsoleView;

import java.util.Scanner;

public class Client {
    private final Repl repl = new Repl(new ServerFacade(), new ConsoleView(System.out));
    public void run() {
        repl.run(new Scanner(System.in));
    }
}

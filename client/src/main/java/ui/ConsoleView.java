package ui;

import java.io.PrintStream;

public class ConsoleView {

    private final PrintStream out;

    public ConsoleView(PrintStream out) {
        this.out = out;
    }

    public void welcome() {
        print(EscapeSequences.BLACK_KING + " Welcome to Chess!\nTry \"login <username> <password>\" or ask for \"help\".\n");
        printPrompt(EscapeSequences.RESET_TEXT_COLOR, "");
    }

    public void success(String message) {
        printPrompt(EscapeSequences.SET_TEXT_COLOR_BLUE, message);
    }

    public void usage(String message) {
        printPrompt(EscapeSequences.SET_TEXT_COLOR_BLUE, message);
    }

    public void error(String message) {
        printPrompt(EscapeSequences.SET_TEXT_COLOR_RED, message);
    }

    public void quit() {
        print(EscapeSequences.SET_TEXT_COLOR_GREEN + "Goodbye!\n" + EscapeSequences.RESET_TEXT_COLOR);
    }



    private void printPrompt(String color, String message) {
        print(color + message);
        print("\n" + EscapeSequences.RESET_TEXT_COLOR + "> ");
    }

    // Synchronized so we don't interleave messages from notifications
    private synchronized void print(String msg) {
        out.print(msg);
    }
}


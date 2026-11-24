package client;

public record Command(int requiredParams, String usage, CommandHandler handler) {
}

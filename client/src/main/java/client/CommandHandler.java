package client;

public interface CommandHandler {
    String run(String[] params) throws Exception;
}

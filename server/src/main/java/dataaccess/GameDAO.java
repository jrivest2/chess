package dataaccess;

public interface GameDAO {


    int getSize();

    int createGame(String gameName);

    void clearDB();
}

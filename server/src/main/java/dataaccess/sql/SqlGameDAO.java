package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import dataaccess.ServerIssueException;
import model.GameData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

public class SqlGameDAO implements GameDAO {

    @Override
    public int getSize() {
        int result;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT COUNT(*) AS row_count FROM GameData;")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                result = rs.getInt("row_count");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e) {
            throw new ServerIssueException("Error: There was an issue in the Server. If the Issue persists, please contact support.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int createGame(String gameName) {
        Random rand = new Random();
        int gameID = rand.nextInt((10000 - 1) + 1) + 1;

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT into GameData (gameID,gameName,game,whiteUsername,blackUsername) VALUES (?,?,?,?,?);")) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.setString(2, gameName);
                preparedStatement.setString(3, new Gson().toJson(new ChessGame()));
                preparedStatement.setString(4, null);
                preparedStatement.setString(5, null);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e) {
            throw new ServerIssueException("Error: There was an issue in the Server. If the Issue persists, please contact support.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return gameID;
    }

    @Override
    public void joinGame(String playerColor, int gameID, String userName) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * from GameData where gameID = ?;")) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.executeQuery();
                if (playerColor.equals("white")) {
                    try (var preparedStatement2 = conn.prepareStatement("UPDATE GameData SET whiteUsername = ? WHERE gameID = ?;")) {
                        preparedStatement2.setString(1,userName);
                        preparedStatement2.setInt(2,gameID);
                        preparedStatement2.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try (var preparedStatement2 = conn.prepareStatement("UPDATE GameData SET blackUsername = ? WHERE gameID = ?;")) {
                        preparedStatement2.setString(1, userName);
                        preparedStatement2.setInt(2, gameID);
                        preparedStatement2.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e) {
            throw new ServerIssueException("Error: There was an issue in the Server. If the Issue persists, please contact support.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameData getGame(int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * from GameData where gameID = ?;")) {
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    return null;
                }
                String whiteUser = rs.getString("whiteUsername");
                String blackUser = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                return new GameData(gameID,whiteUser,blackUser,gameName,game);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e) {
            throw new ServerIssueException("Error: There was an issue in the Server. If the Issue persists, please contact support.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public HashMap<String, GameData> listGames() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * from GameData;")) {
                var rs = preparedStatement.executeQuery();
                HashMap<String,GameData> result = new HashMap<>();
                while (rs.next()){
                    int gameID = rs.getInt("gameID");
                    String whiteUser = rs.getString("whiteUsername");
                    String blackUser = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    GameData gameData = new GameData(gameID,whiteUser,blackUser,gameName,game);
                    result.put(Integer.toString(rs.getInt("gameID")),gameData);
                }
                return result;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e) {
            throw new ServerIssueException("Error: There was an issue in the Server. If the Issue persists, please contact support.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearDB() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM GameData;")) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e) {
            throw new ServerIssueException("Error: There was an issue in the Server. If the Issue persists, please contact support.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

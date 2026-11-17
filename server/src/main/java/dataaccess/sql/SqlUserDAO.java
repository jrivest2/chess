package dataaccess.sql;

import dataaccess.*;
import model.RegisterRequest;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class SqlUserDAO implements UserDAO, SqlDAO {
    static HashMap<String, UserData> userDataTable = new HashMap<>();
    public SqlUserDAO() throws ResponseException, DataAccessException {
        this.configureDatabase();
    }
    @Override
    public UserData getUser(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * from userdata where username = ?;")) {
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    return null;
                }
                return new UserData(rs.getString(1),rs.getString(2),rs.getString(3));
            } catch (SQLException e) {
                throw new RuntimeException("Error: Unauthorized");
            }
        } catch (DataAccessException e) {
            throw new ServerIssueException("Error: There was an issue in the Server. If the Issue persists, please contact support.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(RegisterRequest registerRequest) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT into userdata (username,password,email) VALUES (?,?,?);")) {
                preparedStatement.setString(1, registerRequest.username());
                preparedStatement.setString(2, registerRequest.password());
                preparedStatement.setString(3, registerRequest.email());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate")) {
                    throw new AlreadyTakenException("Error: That username is already taken");
                }
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e) {
            throw new ServerIssueException("Error: There was an issue in the Server. If the Issue persists, please contact support.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getSize() {
        int result = -1;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT COUNT(*) AS row_count FROM UserData;")) {
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
    public void clearDB() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM userdata;")) {
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


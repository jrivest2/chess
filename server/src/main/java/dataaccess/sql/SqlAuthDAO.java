package dataaccess.sql;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class SqlAuthDAO implements AuthDAO {
    static HashMap<String, AuthData> authDataTable = new HashMap<>();
    @Override
    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT into authdata (username,authToken) VALUES (?,?);")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, authToken);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e) {
            throw new ServerIssueException("Error: There was an issue in the Server. If the Issue persists, please contact support.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return authToken;
    }

    @Override
    public AuthData getAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * from AuthData where authToken = ?;")) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    return null;
                }
                return new AuthData(rs.getString(1),rs.getString(2));
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
    public int getSize() {
        int result = -1;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT COUNT(*) AS row_count FROM AuthData;")) {
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
            try (var preparedStatement = conn.prepareStatement("DELETE FROM AuthData;")) {
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

    @Override
    public void deleteAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE * from AuthData where authToken = ?;")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException("Error: Unauthorized");
            }
        } catch (DataAccessException e) {
            throw new ServerIssueException("Error: There was an issue in the Server. If the Issue persists, please contact support.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

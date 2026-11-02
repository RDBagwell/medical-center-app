package repository;

import database.Database;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class UserRepository {

    public static Optional<User> findByUserName(String userName) {
        String[] filters = {};
        String where = "user_name = ?";
        try (
                Database db = new Database();
                ResultSet rs = db.select("users", filters, where, "", userName)
        ) {
            if (!rs.next()) {
                return Optional.empty();
            }
            User user = fromRow(rs);
            return Optional.of(user);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not look up user " + userName, e);
        }
    }


    public static void add(String userName, String passwordHash) {
        try (Database db = new Database()) {
            String[] columns = {"user_name", "password"};
            Object[] values = {userName, passwordHash};
            db.insert("users", columns, values);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not add user " + userName, e);
        }
    }

    public static void updatePassword(int id, String passwordHash) {
        try (Database db = new Database()) {
            String[] columns = {"password"};
            Object[] values = {passwordHash};
            db.update("users", columns, values, "id = ?", id);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not update password for user id " + id, e);
        }
    }

    private static User fromRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("user_name"),
                rs.getString("password")
        );
    }

    private UserRepository() {
    }
}

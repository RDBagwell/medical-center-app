package repository;

import database.Database;
import model.PasswordHash;
import model.User;
import model.Username;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class JdbcUserRepository implements UserRepository {

    @Override
    public Optional<User> findByUserName(Username userName) {
        String[] filters = {};
        String where = "user_name = ?";
        try (
                Database db = new Database();
                ResultSet rs = db.select("users", filters, where, "", userName.value())
        ) {
            if (!rs.next()) {
                return Optional.empty();
            }
            User user = fromRow(rs);
            return Optional.of(user);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not look up user " + userName.value(), e);
        }
    }

    @Override
    public void add(Username userName, PasswordHash passwordHash) {
        try (Database db = new Database()) {
            String[] columns = {"user_name", "password"};
            Object[] values = {userName.value(), passwordHash.value()};
            db.insert("users", columns, values);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not add user " + userName.value(), e);
        }
    }

    @Override
    public void updatePassword(int id, PasswordHash passwordHash) {
        try (Database db = new Database()) {
            String[] columns = {"password"};
            Object[] values = {passwordHash.value()};
            db.update("users", columns, values, "id = ?", id);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not update password for user id " + id, e);
        }
    }

    private static User fromRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                new Username(rs.getString("user_name")),
                new PasswordHash(rs.getString("password"))
        );
    }
}

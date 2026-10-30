package model;

import database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record User(int id, String userName, String passwordHash) {

    @Override
    public String toString() {
        return "User[id=" + id + ", userName=" + userName + "]";
    }

    public static void add(Object[] data) throws SQLException {
        try (Database db = new Database()) {
            String[] columns = {"user_name", "password"};
            db.insert("users", columns, data);
        }
    }

    public static List<User> getUsers() throws SQLException {
        String[] filters = {};
        try (
                Database db = new Database();
                ResultSet rs = db.select("users", filters, "", "")
        ) {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = fromRow(rs);
                users.add(user);
            }
            return users;
        }
    }

    public static Optional<User> getUser(String userName) throws SQLException {
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
        }
    }

    public static void update(
            String[] columns,
            Object[] vals,
            String clause,
            Object... whereParams
    ) throws SQLException {
        try (Database db = new Database()) {
            db.update("users", columns, vals, clause, whereParams);
        }
    }

    private static User fromRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("user_name"),
                rs.getString("password")
        );
    }
}

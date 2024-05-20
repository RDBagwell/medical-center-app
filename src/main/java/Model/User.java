package Model;

import database.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    public static void add(Object[] data) throws SQLException {
        database db = new database();
        String[] columns = {"user_name","password"};
        db.insert("users", columns, data);
        db.close();
    }

    public static ResultSet getUsers() throws SQLException {
        database db = new database();
        String[] filters = {};
        return db.select("users", filters, "", "");
    }

    public static ResultSet getUser(String userName) throws SQLException {
        database db = new database();
        String[] filters = {};
        String where = "user_name = '"+userName+"'";
        return db.select("users", filters, where, "");
    }

    public static void updateUser(String[] columns, Object[] vals, String clause) throws SQLException{
        database db = new database();
        db.update("users", columns, vals, clause);
        db.close();
    }
}

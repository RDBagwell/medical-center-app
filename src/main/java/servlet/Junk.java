package servlet;

import Model.User;

import java.sql.SQLException;

public class Junk {
    public static void main(String[] args) throws SQLException {
        User.getUsers().forEach(System.out::println);
//        database db = new database();
//        db.delete("users", 5);
    }
}

package helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import database.database;


public class Helper {

    public static boolean checkUser(String userName, String password) {
        try {
            String userPass = getObjects(userName);
            return encryptPassword(password).equals(userPass);
        } catch (NoSuchAlgorithmException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getObjects(String name) throws SQLException {
        database db = new database();
        String[] arr = {"password"};
        String uname;
        uname = name;
        String pass = "";
        try (ResultSet rs = db.select("`test`", arr, "`name` = '" + uname + "'", "")) {
            while (rs.next()) {
                pass = rs.getString("password");
            }
        }
        return pass;
    }


    private static String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        // Convert byte array to a string
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}

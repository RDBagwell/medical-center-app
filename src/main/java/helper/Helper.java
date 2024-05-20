package helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.database;
import Model.User;


public class Helper {

    public static boolean checkUser(String userName, String password) {
        try {
            ResultSet user = User.getUser(userName);
            String userPass = "";
            while (user.next()) {
                userPass = user.getString("password");
            }
            return encryptPassword(password).equals(userPass);
        } catch (NoSuchAlgorithmException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptPassword(String password) throws NoSuchAlgorithmException {
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

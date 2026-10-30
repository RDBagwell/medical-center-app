package helper;

import model.User;

import java.sql.SQLException;
import java.util.Optional;


public class Authenticator {

    public static boolean authenticate(String userName, String password) {
        try {
            Optional<User> result = User.getUser(userName);
            if (result.isEmpty()) {
                return false;
            }
            String userPass = result.get().passwordHash();
            return PasswordHasher.verify(password, userPass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package service;

import helper.CredentialsValidator;
import helper.PasswordHasher;
import model.User;
import repository.JdbcUserRepository;
import repository.UserRepository;

import java.util.List;
import java.util.Optional;

public final class UserService {

    private static final UserRepository users = new JdbcUserRepository();

    public static List<String> register(String userName, String password) {

        List<String> errors = CredentialsValidator.validate(userName, password);
        if (!errors.isEmpty()) {
            return errors;
        }

        Optional<User> existing = users.findByUserName(userName);
        if (existing.isPresent()) {
            return List.of("User already exists");
        }
        String hashed = PasswordHasher.hash(password);
        users.add(userName, hashed);

        return List.of();
    }

    public static List<String> changePassword(String userName, String newPassword) {
        List<String> errors = CredentialsValidator.validate(userName, newPassword);
        if (!errors.isEmpty()) {
            return errors;
        }

        Optional<User> existing = users.findByUserName(userName);
        if (existing.isEmpty()) {
            return List.of("No such user");
        }
        String hashed = PasswordHasher.hash(newPassword);
        users.updatePassword(existing.get().id(), hashed);

        return List.of();
    }

    public static boolean authenticate(String userName, String password) {
        Optional<User> result = users.findByUserName(userName);
        if (result.isEmpty()) {
            return false;
        }
        String userPass = result.get().passwordHash();
        return PasswordHasher.verify(password, userPass);
    }

    private UserService() {
    }
}

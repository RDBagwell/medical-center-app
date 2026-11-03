package service;

import helper.CredentialsValidator;
import helper.PasswordHasher;
import model.PasswordHash;
import model.User;
import model.Username;
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

        Username name = new Username(userName);

        Optional<User> existing = users.findByUserName(name);
        if (existing.isPresent()) {
            return List.of("User already exists");
        }
        String hashed = PasswordHasher.hash(password);
        users.add(name, new PasswordHash(hashed));

        return List.of();
    }

    public static List<String> changePassword(String userName, String newPassword) {
        List<String> errors = CredentialsValidator.validate(userName, newPassword);
        if (!errors.isEmpty()) {
            return errors;
        }
        Username name = new Username(userName);
        Optional<User> existing = users.findByUserName(name);
        if (existing.isEmpty()) {
            return List.of("No such user");
        }
        String hashed = PasswordHasher.hash(newPassword);
        users.updatePassword(existing.get().id(), new PasswordHash(hashed));

        return List.of();
    }

    public static boolean authenticate(String userName, String password) {
        Username name;
        try {
            name = new Username(userName);
        } catch (IllegalArgumentException e) {
            return false;
        }

        Optional<User> result = users.findByUserName(name);
        if (result.isEmpty()) {
            return false;
        }
        String userPass = result.get().passwordHash().value();
        return PasswordHasher.verify(password, userPass);
    }

    private UserService() {
    }
}

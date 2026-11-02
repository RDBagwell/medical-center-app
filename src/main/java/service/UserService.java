package service;

import helper.CredentialsValidator;
import helper.PasswordHasher;
import model.User;
import repository.UserRepository;

import java.util.List;
import java.util.Optional;

public final class UserService {

    public static List<String> register(String userName, String password) {

        List<String> errors = CredentialsValidator.validate(userName, password);
        if (!errors.isEmpty()) {
            return errors;
        }

        Optional<User> existing = UserRepository.findByUserName(userName);
        if (existing.isPresent()) {
            return List.of("User already exists");
        }
        String hashed = PasswordHasher.hash(password);
        UserRepository.add(userName, hashed);

        return List.of();
    }

    public static List<String> changePassword(String userName, String newPassword) {
        List<String> errors = CredentialsValidator.validate(userName, newPassword);
        if (!errors.isEmpty()) {
            return errors;
        }

        Optional<User> existing = UserRepository.findByUserName(userName);
        if (existing.isEmpty()) {
            return List.of("No such user");
        }
        String hashed = PasswordHasher.hash(newPassword);
        UserRepository.updatePassword(existing.get().id(), hashed);

        return List.of();
    }

    public static boolean authenticate(String userName, String password) {
        Optional<User> result = UserRepository.findByUserName(userName);
        if (result.isEmpty()) {
            return false;
        }
        String userPass = result.get().passwordHash();
        return PasswordHasher.verify(password, userPass);
    }

    private UserService() {
    }
}

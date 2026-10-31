package helper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class CredentialsValidator {
    private static final int MAX_USERNAME_LENGTH = 96;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_BYTES = 72;

    public static List<String> validate(String userName, String password) {
        List<String> errors = new ArrayList<>();
        validateUserName(userName, errors);
        validatePassword(password, errors);
        return errors;
    }

    private static void validateUserName(String userName, List<String> errors) {
        if (userName.isBlank()) {
            errors.add("Username is required");
        } else if (userName.length() > MAX_USERNAME_LENGTH) {
            errors.add("Username must be at most " + MAX_USERNAME_LENGTH + " characters");
        }
    }

    private static void validatePassword(String password, List<String> errors) {
        if (password.isBlank()) {
            errors.add("Password is required");
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            errors.add("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        } else if (password.getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES) {
            errors.add("Password is too long");
        }
    }

    private CredentialsValidator() {
    }
}

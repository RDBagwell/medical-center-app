package helper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PasswordHasher {
    private static final int COST = 12;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(COST);

    public static String hash(String raw) {
        return encoder.encode(raw);
    }

    public static boolean verify(String raw, String storedHash) {
        return encoder.matches(raw, storedHash);
    }

    private PasswordHasher() {
    }
}
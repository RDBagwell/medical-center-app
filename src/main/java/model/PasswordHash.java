package model;

public record PasswordHash(String value) {
    public static final int BCRYPT_LENGTH = 60;
    private static final String BCRYPT_ERROR = "Password hash must be a 60-character bcrypt string";

    @Override
    public String toString() {
        return "PasswordHash[redacted]";
    }

    public PasswordHash {
        if (value.length() != BCRYPT_LENGTH) {
            throw new IllegalArgumentException(BCRYPT_ERROR);
        }
        if (!value.startsWith("$2")) {
            throw new IllegalArgumentException(BCRYPT_ERROR);
        }
    }
}

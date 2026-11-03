package model;

public record Username(String value) {
    public static final int MAX_LENGTH = 96;

    public Username {
        value = value.strip();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Username must be at most "+MAX_LENGTH+" characters");
        }
    }
}
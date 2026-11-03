package model;

import helper.PasswordHasher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordHashTest {
    private static final String VALID_PASSWORD = "password123";

    @Test
    public void acceptsARealBcryptHash() {
        assertDoesNotThrow(()-> new PasswordHash(PasswordHasher.hash(VALID_PASSWORD)));
    }

    @Test
    public void rejectsAPlainPassword() {
        assertThrows(IllegalArgumentException.class, ()-> new PasswordHash(VALID_PASSWORD));
    }

    @Test
    public void rejectsABlankPassword() {
        assertThrows(IllegalArgumentException.class, ()-> new PasswordHash("   "));
    }

    @Test
    public void rejectsTheWrongLength() {
        String tooShort = "$2a$12$"+"x".repeat(10);
        assertThrows(IllegalArgumentException.class, ()-> new PasswordHash(tooShort));
    }

    @Test
    public void hidesTheHash() {
        PasswordHash hash = new PasswordHash(PasswordHasher.hash(VALID_PASSWORD));
        assertFalse(hash.toString().contains(hash.value()));
    }

    @Test
    public void rejectsSixtyCharactersThatAreNotBcrypt() {
        assertThrows(IllegalArgumentException.class, ()-> new PasswordHash("x".repeat(60)));
    }
}

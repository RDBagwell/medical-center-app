package helper;

import org.junit.jupiter.api.Test;

import static helper.PasswordHasher.hash;
import static helper.PasswordHasher.verify;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordHasherTest {
    @Test
    void aPasswordVerifiesAgainstItsOwnHash() {
        String hash = hash("password123");
        assertTrue(verify("password123", hash));
    }

    @Test
    void aWrongPasswordDoesNotVerify() {
        String hash = hash("password123");
        assertFalse(verify("wrong", hash));
    }

    @Test
    void hashingTwiceGivesDifferentHashesThatBothVerify() {
        String hash = hash("password123");
        String hash2 = hash("password123");
        assertNotEquals(hash, hash2);
        assertTrue(verify("password123", hash));
        assertTrue(verify("password123", hash2));
    }

    @Test
    void usesBcryptAtCost12() {
        String hash = hash("password123");
        assertTrue(hash.startsWith("$2a$12$"));
    }

    @Test
    void hashIs60Characters() {
        String hash = hash("password123");
        assertEquals(60, hash.length());
    }

    @Test
    void rejectsPasswordsOver72Bytes() {
        assertThrows(
                IllegalArgumentException.class,
                () -> PasswordHasher.hash("x".repeat(73))
        );
    }

    @Test
    void rejectsPasswordsOver72BytesEvenWhenUnder72Characters() {
        assertThrows(
                IllegalArgumentException.class,
                () -> PasswordHasher.hash("é".repeat(40))
        );
    }
}

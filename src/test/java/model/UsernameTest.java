package model;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class UsernameTest {
    private static final String VALID_USER = "test.nurse";

    @Test
    public void keepsAValidName() {
        assertEquals(VALID_USER, new Username(VALID_USER).value());
    }

    @Test
    public void rejectsABlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Username("   "));
    }

    @Test
    public void userNameOf96CharactersIsAccepted() {
        assertDoesNotThrow(() -> new Username("a".repeat(Username.MAX_LENGTH)));
    }

    @Test
    public void userNameOf97CharactersIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Username("a".repeat(Username.MAX_LENGTH + 1)));
    }

    @Test
    public void stripsSurroundingSpaces() {
        assertEquals(VALID_USER, new Username("   "+VALID_USER+"   ").value());
    }

    @Test
    public void valuesAreEqual() {
        Username a = new Username(VALID_USER);
        Username b = new Username(VALID_USER);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

}
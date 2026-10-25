package helper;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CredentialsValidatorTest {
    private static final String VALID_USER = "test.nurse";
    private static final String VALID_PASSWORD = "password123";

    @Test
    void validCredentialsHaveNoErrors() {
        assertEquals(
                List.of(),
                CredentialsValidator.validate(VALID_USER, VALID_PASSWORD)
        );
    }

    @Test
    void blankUserNameIsRequired() {
        assertEquals(
                List.of("Username is required"),
                CredentialsValidator.validate("   ", VALID_PASSWORD)
        );
    }

    @Test
    void blankPasswordIsRequired() {
        assertEquals(
                List.of("Password is required"),
                CredentialsValidator.validate(VALID_USER, "")
        );
    }

    @Test
    void userNameOf96CharactersIsAccepted() {
        assertEquals(
                List.of(),
                CredentialsValidator.validate("a".repeat(96), VALID_PASSWORD)
        );
    }

    @Test
    void userNameOf97CharactersIsRejected() {
        assertEquals(
                List.of("Username must be at most 96 characters"),
                CredentialsValidator.validate("a".repeat(97), VALID_PASSWORD)
        );
    }

    @Test
    void passwordOf8CharactersIsAccepted() {
        assertEquals(
                List.of(),
                CredentialsValidator.validate(VALID_USER, "12345678")
        );
    }

    @Test
    void passwordOf7CharactersIsRejected() {
        assertEquals(
                List.of("Password must be at least 8 characters"),
                CredentialsValidator.validate(VALID_USER, "1234567")
        );
    }

    @Test
    void passwordOf72BytesIsAccepted() {
        assertEquals(
                List.of(),
                CredentialsValidator.validate(VALID_USER, "é".repeat(36))
        );
    }

    @Test
    void passwordOver72BytesIsRejected() {
        assertEquals(
                List.of("Password is too long"),
                CredentialsValidator.validate(VALID_USER, "é".repeat(37))
        );
    }

    @Test
    void reportsEveryErrorAtOnce() {
        assertEquals(
                List.of(
                        "Username is required",
                        "Password must be at least 8 characters"
                ),
                CredentialsValidator.validate("", "short")
        );
    }

    @Test
    void blankUserNameOfAnyLengthIsOnlyRequired (){
        assertEquals(
                List.of("Username is required"),
                CredentialsValidator.validate(" ".repeat(97), VALID_PASSWORD)
        );

    }

    @Test
    void blankUserPasswordOfAnyLengthIsOnlyRequired (){
        assertEquals(
                List.of("Password is required"),
                CredentialsValidator.validate(VALID_USER, " ".repeat(73))
        );

    }
}

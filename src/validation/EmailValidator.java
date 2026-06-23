package src.validation;

/**
 * Validates an email address.
 * Rules:
 *  - Must not be blank
 *  - Must follow standard email format: local@domain.tld
 *  - Local part: letters, digits, dots, hyphens, underscores, plus signs
 *  - Domain: letters, digits, hyphens
 *  - TLD: 2–6 letters
 *  - Maximum 254 characters (RFC 5321 limit)
 */
public class EmailValidator {

    private static final String EMAIL_REGEX =
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final int MAX_LENGTH = 254;

    /**
     * @param value the email string to validate
     * @return null if valid, or an error message string if invalid
     */
    public static String validate(String value) {
        if (value == null || value.isBlank()) {
            return "Email address is required.";
        }
        String trimmed = value.trim();
        if (trimmed.length() > MAX_LENGTH) {
            return "Email address must not exceed " + MAX_LENGTH + " characters.";
        }
        if (!trimmed.matches(EMAIL_REGEX)) {
            return "Email address is invalid. Expected format: example@domain.com.";
        }
        return null;
    }

    /**
     * Validates that a confirmation email matches the original.
     * @param email        the original email
     * @param confirmEmail the confirmation email
     * @return null if they match, or an error message if they don't
     */
    public static String validateConfirmation(String email, String confirmEmail) {
        if (confirmEmail == null || confirmEmail.isBlank()) {
            return "Please confirm your email address.";
        }
        if (!confirmEmail.trim().equalsIgnoreCase(email == null ? "" : email.trim())) {
            return "Email addresses do not match.";
        }
        return null;
    }
}
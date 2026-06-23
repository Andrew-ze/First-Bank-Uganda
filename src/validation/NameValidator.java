package src.validation;

/**
 * Validates first name and last name fields.
 * Rules:
 *  - Must not be blank
 *  - Minimum 2 characters, maximum 50 characters
 *  - Letters, hyphens, and apostrophes only (e.g. O'Brien, Amin-Dada)
 */
public class NameValidator {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 50;
    private static final String NAME_REGEX = "^[A-Za-z\\-']+$";

    /**
     * Validates a name value.
     * @param value     the name string to validate
     * @param fieldName e.g. "First name" or "Last name" — used in the error message
     * @return null if valid, or an error message string if invalid
     */
    public static String validate(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return fieldName + " is required.";
        }
        String trimmed = value.trim();
        if (trimmed.length() < MIN_LENGTH) {
            return fieldName + " must be at least " + MIN_LENGTH + " characters.";
        }
        if (trimmed.length() > MAX_LENGTH) {
            return fieldName + " must not exceed " + MAX_LENGTH + " characters.";
        }
        if (!trimmed.matches(NAME_REGEX)) {
            return fieldName + " must contain letters only (hyphens and apostrophes allowed).";
        }
        return null;
    }
}
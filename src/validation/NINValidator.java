package src.validation;

/**
 * Validates a Ugandan National Identification Number (NIN).
 * Format: CM or CF + 9 digits + 3 uppercase letters  →  e.g. CM123456789ABC
 * Rules:
 *  - Must not be blank
 *  - Exactly 14 characters
 *  - Starts with "CM" or "CF" (case-insensitive)
 *  - Followed by exactly 9 digits
 *  - Ends with exactly 3 uppercase letters
 */
public class NINValidator {

    // (CM or CF) + 9 digits + 3 letters  (case-insensitive prefix and trailing letters)
    private static final String NIN_REGEX = "^[Cc][MmFf]\\d{9}[A-Za-z]{3}$";

    private static final int EXPECTED_LENGTH = 14;

    /**
     * @param value the NIN string to validate
     * @return null if valid, or an error message string if invalid
     */
    public static String validate(String value) {
        if (value == null || value.isBlank()) {
            return "NIN is required.";
        }
        String trimmed = value.trim();
        if (trimmed.length() != EXPECTED_LENGTH) {
            return "NIN must be exactly 14 characters (e.g. CM123456789ABC).";
        }
        if (!trimmed.matches(NIN_REGEX)) {
            return "NIN format is invalid. Expected format: CM or CF followed by 9 digits and 3 letters (e.g. CM123456789ABC).";
        }
        return null;
    }

    /**
     * Normalises a NIN to uppercase, since the brief requires the stored
     * value to be uppercase regardless of how the user typed it.
     *
     * @param value a NIN that has already passed validate()
     * @return the NIN in uppercase
     */
    public static String normalise(String value) {
        return value.trim().toUpperCase();
    }
}
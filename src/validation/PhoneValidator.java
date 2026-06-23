package src.validation;

/**
 * Validates a Ugandan phone number.
 * Accepted formats:
 *  - International: +256XXXXXXXXX  (12 chars total, 9 digits after country code)
 *  - Local:          07XXXXXXXXX   (10 digits, starts with 07)
 *                    03XXXXXXXXX   (10 digits, starts with 03)
 *
 * Valid Ugandan network prefixes (after +256 or leading 0):
 *  MTN:    70, 74, 75, 76, 77, 78
 *  Airtel: 70, 74, 75, 76, 77, 78  (shared prefixes)
 *  UTL:    71
 *  Lycamobile: 79
 */
public class PhoneValidator {

    // +256 followed by a valid prefix digit (7x or 3x) then 8 more digits
    private static final String INTL_REGEX  = "^\\+256[37]\\d{8}$";
    // Local format: 07x or 03x followed by 8 digits
    private static final String LOCAL_REGEX = "^0[37]\\d{8}$";

    /**
     * @param value the phone number string to validate
     * @return null if valid, or an error message string if invalid
     */
    public static String validate(String value) {
        if (value == null || value.isBlank()) {
            return "Phone number is required.";
        }
        String trimmed = value.trim();
        if (trimmed.matches(INTL_REGEX) || trimmed.matches(LOCAL_REGEX)) {
            return null;
        }
        return "Phone number is invalid. Use +256XXXXXXXXX or 07XXXXXXXXX format.";
    }

    /**
     * Normalises any accepted format to the international +256 form.
     * e.g. "0712345678" → "+256712345678"
     * @param value a phone number that has already passed validate()
     * @return normalised international format
     */
    public static String normalise(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("0")) {
            return "+256" + trimmed.substring(1);
        }
        return trimmed;
    }
}
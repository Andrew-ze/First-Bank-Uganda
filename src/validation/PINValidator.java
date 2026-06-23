package src.validation;

/**
 * Validates a customer's PIN.
 * Rules:
 *  - Must not be blank
 *  - Exactly 4 digits
 *  - Must not be all the same digit (e.g. 1111, 0000)
 *  - Must not be a simple sequence (e.g. 1234, 4321)
 */
public class PINValidator {

    private static final String PIN_REGEX = "^(\\d{4}|\\d{6})$";

    private static final String[] WEAK_PINS = {
        "1234", "4321", "0123", "3210",
        "1111", "2222", "3333", "4444",
        "5555", "6666", "7777", "8888",
        "9999", "0000", "1212", "0101"
    };

    /**
     * @param value the PIN string to validate
     * @return null if valid, or an error message string if invalid
     */
    public static String validate(String value) {
        if (value == null || value.isBlank()) {
            return "PIN is required.";
        }
        String trimmed = value.trim();
        if (!trimmed.matches(PIN_REGEX)) {
            return "PIN must be exactly 4-6 digits.";
        }
        for (String weak : WEAK_PINS) {
            if (trimmed.equals(weak)) {
                return "PIN is too weak. Avoid simple sequences or repeated digits.";
            }
        }
        return null;
    }

    /**
     * Validates that a confirmation PIN matches the original.
     * @param pin        the original PIN
     * @param confirmPin the confirmation PIN
     * @return null if they match, or an error message if they don't
     */
    public static String validateConfirmation(String pin, String confirmPin) {
        if (confirmPin == null || confirmPin.isBlank()) {
            return "Please confirm your PIN.";
        }
        if (!confirmPin.trim().equals(pin == null ? "" : pin.trim())) {
            return "PINs do not match.";
        }
        return null;
    }
}
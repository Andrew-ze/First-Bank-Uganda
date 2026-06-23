package src.validation;

/**
 * Validates the opening deposit amount against the minimum required
 * for a given account type.
 * Rules:
 *  - Must not be blank
 *  - Must be a valid positive number
 *  - Must be >= the account type's minimum deposit
 */
public class DepositValidator {

    /**
     * @param value          the raw deposit string from the form field
     * @param minimumDeposit the minimum deposit required for the chosen account type
     * @param accountTypeName e.g. "Savings Account" — used in the error message
     * @return null if valid, or an error message string if invalid
     */
    public static String validate(String value, double minimumDeposit, String accountTypeName) {
        if (value == null || value.isBlank()) {
            return "Opening deposit is required.";
        }
        double amount;
        try {
            // Strip commas in case the user typed e.g. "50,000"
            amount = Double.parseDouble(value.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return "Opening deposit must be a valid number.";
        }
        if (amount <= 0) {
            return "Opening deposit must be greater than zero.";
        }
        if (amount < minimumDeposit) {
            return String.format(
                "Minimum opening deposit for a %s is UGX %,.0f.",
                accountTypeName, minimumDeposit
            );
        }
        return null;
    }

    /**
     * Convenience parse — call only after validate() returns null.
     * @param value the raw deposit string (may contain commas)
     * @return the deposit as a double
     */
    public static double parse(String value) {
        return Double.parseDouble(value.trim().replace(",", ""));
    }
}
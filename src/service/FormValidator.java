package src.service;

import java.util.ArrayList;
import java.util.List;

import src.model.Account;
import src.validation.*;

/**
 * Orchestrates all field-level validators and account-level rules.
 * Returns a list of error messages — an empty list means the form is valid.
 *
 * Usage:
 *   List<String> errors = FormValidator.validate(account, rawFields);
 *   if (errors.isEmpty()) { // proceed to save }
 *   else { // display errors }
 */
public class FormValidator {

    /**
     * Holds the raw string values collected from the form fields
     * before they are parsed into an Account object.
     * The UI layer populates this and passes it to validate().
     */
    public static class FormFields {
        public String firstName;
        public String lastName;
        public String nin;
        public String email;
        public String confirmEmail;
        public String phone;
        public String pin;
        public String confirmPin;
        public String dobDay;
        public String dobMonth;
        public String dobYear;
        public String branch;
        public String accountType;
        public String openingDeposit;
        public String secondHolderNin;   // only for Joint accounts
    }

    /**
     * Runs all validation rules against the raw form fields and the
     * partially-constructed Account object.
     *
     * @param fields      raw strings from the form
     * @param account     the Account subclass already instantiated from the fields
     *                    (used for polymorphic rules like minimumDeposit and extraValidation)
     * @return a list of error messages; empty if everything is valid
     */
    public static List<String> validate(FormFields fields, Account account) {
        List<String> errors = new ArrayList<>();

        // ── Personal info ──────────────────────────────────────────
        addIfError(errors, NameValidator.validate(fields.firstName, "First name"));
        addIfError(errors, NameValidator.validate(fields.lastName,  "Last name"));
        addIfError(errors, NINValidator.validate(fields.nin));
        addIfError(errors, EmailValidator.validate(fields.email));
        addIfError(errors, EmailValidator.validateConfirmation(fields.email, fields.confirmEmail));
        addIfError(errors, PhoneValidator.validate(fields.phone));
        addIfError(errors, PINValidator.validate(fields.pin));
        addIfError(errors, PINValidator.validateConfirmation(fields.pin, fields.confirmPin));

        // ── Date of birth ──────────────────────────────────────────
        addIfError(errors, DOBHelper.validate(fields.dobDay, fields.dobMonth, fields.dobYear));

        // ── Account-type-specific rules (polymorphic) ─────────────
        if (account != null) {
            // Deposit check — uses the subclass's getMinimumDeposit()
            addIfError(errors, DepositValidator.validate(
                fields.openingDeposit,
                account.getMinimumDeposit(),
                account.getAccountTypeName()
            ));

            // Age eligibility — uses the subclass's min/max age range
            if (account.getDateOfBirth() != null && !account.isAgeEligible()) {
                errors.add(String.format(
                    "Age must be between %d and %d for a %s.",
                    account.getMinimumAge(),
                    account.getMaximumAge(),
                    account.getAccountTypeName()
                ));
            }

            // Any extra rules defined by the subclass (e.g. Joint NIN, Student age cap)
            addIfError(errors, account.getExtraValidationError());
        }

        // ── Branch ────────────────────────────────────────────────
        if (fields.branch == null || fields.branch.isBlank()) {
            errors.add("Please select a branch.");
        }

        // ── Account type ──────────────────────────────────────────
        if (fields.accountType == null || fields.accountType.isBlank()) {
            errors.add("Please select an account type.");
        }

        return errors;
    }

    /** Adds an error message to the list only if it is non-null. */
    private static void addIfError(List<String> errors, String error) {
        if (error != null) errors.add(error);
    }
}
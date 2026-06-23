package src.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Abstract base class for all First Bank Uganda account types.
 * Subclasses must implement the account-specific rules for minimum
 * deposit and age eligibility.
 */
public abstract class Account {

    // ── Core fields ───────────────────────────────────────────────
    private String firstName;
    private String lastName;
    private String nin;               // National Identification Number
    private String email;
    private String phone;
    private String pin;
    private LocalDate dateOfBirth;
    private String accountType;       // set by subclass constructor
    private String branch;
    private double openingDeposit;
    private String accountNumber;     // generated after submission

    // ── Constructor ───────────────────────────────────────────────
    public Account(String firstName, String lastName, String nin,
                   String email, String phone, String pin,
                   LocalDate dateOfBirth, String branch,
                   double openingDeposit) {
        this.firstName      = firstName;
        this.lastName       = lastName;
        this.nin            = nin;
        this.email          = email;
        this.phone          = phone;
        this.pin            = pin;
        this.dateOfBirth    = dateOfBirth;
        this.branch         = branch;
        this.openingDeposit = openingDeposit;
    }

    // ── Abstract methods (each subclass defines its own rules) ────

    /** Returns the minimum opening deposit required for this account type. */
    public abstract double getMinimumDeposit();

    /** Returns the minimum age allowed to open this account type. */
    public abstract int getMinimumAge();

    /** Returns the maximum age allowed to open this account type. */
    public abstract int getMaximumAge();

    /**
     * Returns a human-readable display name for this account type,
     * e.g. "Savings Account", "Student Account".
     */
    public abstract String getAccountTypeName();

    /**
     * Returns any extra validation rules specific to this account type.
     * Subclasses with no extra rules should return null.
     */
    public abstract String getExtraValidationError();

    // ── Derived / computed helpers ────────────────────────────────

    /** Computes the holder's age in whole years as of today. */
    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /** Checks whether the opening deposit meets this account's minimum. */
    public boolean isDepositSufficient() {
        return openingDeposit >= getMinimumDeposit();
    }

    /** Checks whether the holder's age is within the permitted range. */
    public boolean isAgeEligible() {
        int age = getAge();
        return age >= getMinimumAge() && age <= getMaximumAge();
    }

    /**
     * Builds a formatted account summary string, used when
     * displaying the result after a successful submission.
     */
    public String getSummary() {
        return String.format(
            "Account Type  : %s%n" +
            "Account Number: %s%n" +
            "Name          : %s %s%n" +
            "NIN           : %s%n" +
            "Email         : %s%n" +
            "Phone         : %s%n" +
            "Date of Birth : %s (Age: %d)%n" +
            "Branch        : %s%n" +
            "Opening Deposit: UGX %,.0f",
            getAccountTypeName(),
            accountNumber != null ? accountNumber : "Pending",
            firstName, lastName,
            nin,
            email,
            phone,
            dateOfBirth.toString(), getAge(),
            branch,
            openingDeposit
        );
    }

    // ── Getters & setters ─────────────────────────────────────────

    public String getFirstName()            { return firstName; }
    public void   setFirstName(String v)    { this.firstName = v; }

    public String getLastName()             { return lastName; }
    public void   setLastName(String v)     { this.lastName = v; }

    public String getNin()                  { return nin; }
    public void   setNin(String v)          { this.nin = v; }

    public String getEmail()                { return email; }
    public void   setEmail(String v)        { this.email = v; }

    public String getPhone()                { return phone; }
    public void   setPhone(String v)        { this.phone = v; }

    public String getPin()                  { return pin; }
    public void   setPin(String v)          { this.pin = v; }

    public LocalDate getDateOfBirth()       { return dateOfBirth; }
    public void      setDateOfBirth(LocalDate v) { this.dateOfBirth = v; }

    public String getAccountType()          { return accountType; }
    protected void setAccountType(String v) { this.accountType = v; }

    public String getBranch()               { return branch; }
    public void   setBranch(String v)       { this.branch = v; }

    public double getOpeningDeposit()       { return openingDeposit; }
    public void   setOpeningDeposit(double v) { this.openingDeposit = v; }

    public String getAccountNumber()        { return accountNumber; }
    public void   setAccountNumber(String v){ this.accountNumber = v; }
}
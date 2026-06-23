package src.model;

import java.time.LocalDate;

/**
 * Savings Account — minimum deposit UGX 50,000, ages 18–75.
 */
public class SavingsAccount extends Account {

    public SavingsAccount(String firstName, String lastName, String nin,
                          String email, String phone, String pin,
                          LocalDate dateOfBirth, String branch,
                          double openingDeposit) {
        super(firstName, lastName, nin, email, phone, pin,
              dateOfBirth, branch, openingDeposit);
        setAccountType("SAVINGS");
    }

    @Override
    public double getMinimumDeposit() { return 50_000; }

    @Override
    public int getMinimumAge() { return 18; }

    @Override
    public int getMaximumAge() { return 75; }

    @Override
    public String getAccountTypeName() { return "Savings Account"; }

    @Override
    public String getExtraValidationError() { return null; }
}

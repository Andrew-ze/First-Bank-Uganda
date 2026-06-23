package src.model;

import java.time.LocalDate;

/**
 * Fixed Deposit Account — minimum deposit UGX 1,000,000, ages 18–75.
 */
public class FixedDepositAccount extends Account {

    public FixedDepositAccount(String firstName, String lastName, String nin,
                               String email, String phone, String pin,
                               LocalDate dateOfBirth, String branch,
                               double openingDeposit) {
        super(firstName, lastName, nin, email, phone, pin,
              dateOfBirth, branch, openingDeposit);
        setAccountType("FIXED_DEPOSIT");
    }

    @Override
    public double getMinimumDeposit() { return 1_000_000; }

    @Override
    public int getMinimumAge() { return 18; }

    @Override
    public int getMaximumAge() { return 75; }

    @Override
    public String getAccountTypeName() { return "Fixed Deposit Account"; }

    @Override
    public String getExtraValidationError() { return null; }
}

package src.model;

import java.time.LocalDate;

/**
 * Current Account — minimum deposit UGX 200,000, ages 18–75.
 */
public class CurrentAccount extends Account {

    public CurrentAccount(String firstName, String lastName, String nin,
                          String email, String phone, String pin,
                          LocalDate dateOfBirth, String branch,
                          double openingDeposit) {
        super(firstName, lastName, nin, email, phone, pin,
              dateOfBirth, branch, openingDeposit);
        setAccountType("CURRENT");
    }

    @Override
    public double getMinimumDeposit() { return 200_000; }

    @Override
    public int getMinimumAge() { return 18; }

    @Override
    public int getMaximumAge() { return 75; }

    @Override
    public String getAccountTypeName() { return "Current Account"; }

    @Override
    public String getExtraValidationError() { return null; }
}

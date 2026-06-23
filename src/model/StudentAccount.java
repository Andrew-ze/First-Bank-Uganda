package src.model;

import java.time.LocalDate;

/**
 * Student Account — minimum deposit UGX 10,000, restricted to ages 18–25.
 */
public class StudentAccount extends Account {

    public StudentAccount(String firstName, String lastName, String nin,
                          String email, String phone, String pin,
                          LocalDate dateOfBirth, String branch,
                          double openingDeposit) {
        super(firstName, lastName, nin, email, phone, pin,
              dateOfBirth, branch, openingDeposit);
        setAccountType("STUDENT");
    }

    @Override
    public double getMinimumDeposit() { return 10_000; }

    @Override
    public int getMinimumAge() { return 18; }

    @Override
    public int getMaximumAge() { return 25; }

    @Override
    public String getAccountTypeName() { return "Student Account"; }

    @Override
    public String getExtraValidationError() {
        if (!isAgeEligible()) {
            return "Student accounts are only available for applicants aged 18 to 25.";
        }
        return null;
    }
}

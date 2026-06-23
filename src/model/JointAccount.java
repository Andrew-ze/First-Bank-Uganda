package src.model;

import java.time.LocalDate;

/**
 * Joint Account — minimum deposit UGX 100,000, ages 18–75.
 * Requires a second account holder's NIN.
 */
public class JointAccount extends Account {

    private String secondHolderNin;

    public JointAccount(String firstName, String lastName, String nin,
                        String email, String phone, String pin,
                        LocalDate dateOfBirth, String branch,
                        double openingDeposit, String secondHolderNin) {
        super(firstName, lastName, nin, email, phone, pin,
              dateOfBirth, branch, openingDeposit);
        this.secondHolderNin = secondHolderNin;
        setAccountType("JOINT");
    }

    @Override
    public double getMinimumDeposit() { return 100_000; }

    @Override
    public int getMinimumAge() { return 18; }

    @Override
    public int getMaximumAge() { return 75; }

    @Override
    public String getAccountTypeName() { return "Joint Account"; }

    @Override
    public String getExtraValidationError() {
        if (secondHolderNin == null || secondHolderNin.isBlank()) {
            return "Joint accounts require the NIN of the second account holder.";
        }
        if (secondHolderNin.trim().equalsIgnoreCase(getNin().trim())) {
            return "The second holder's NIN must be different from the primary holder's NIN.";
        }
        return null;
    }

    // ── Overrides getSummary to include 2nd holder NIN ────────────
    @Override
    public String getSummary() {
        return super.getSummary()
             + String.format("%n2nd Holder NIN : %s", secondHolderNin);
    }

    // ── Getter & setter ───────────────────────────────────────────
    public String getSecondHolderNin()          { return secondHolderNin; }
    public void   setSecondHolderNin(String v)  { this.secondHolderNin = v; }
}

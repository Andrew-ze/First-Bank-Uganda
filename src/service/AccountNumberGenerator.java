package src.service;

import java.time.LocalDate;

/**
 * Generates account numbers in the format: BRANCH-YEAR-XXXXXX
 * Example: KLA-2026-000142
 *
 * The sequential counter is provided by AccountRepository (fetched from
 * the database) to ensure uniqueness across sessions. This class is
 * responsible only for formatting the final string.
 *
 * Branch codes:
 *  Kampala        → KLA
 *  Entebbe        → ENT
 *  Jinja          → JIN
 *  Mbarara        → MBA
 *  Gulu           → GUL
 *  Mbale          → MBL
 *  Masaka         → MSK
 *  Fort Portal    → FTP
 */
public class AccountNumberGenerator {

    /** Maps full branch names to their 3-letter codes. */
    public static final String[][] BRANCH_MAP = {
        { "Kampala",     "KLA" },
        { "Jinja",       "JIN" },
        { "Mbarara",     "MBA" },
        { "Gulu",        "GUL" },
        { "Mbale",       "MBL" },
    };

    /**
     * Returns the 3-letter branch code for a given branch name.
     * Falls back to the first 3 letters uppercased if not found.
     *
     * @param branchName full branch name e.g. "Kampala"
     * @return branch code e.g. "KLA"
     */
    public static String getBranchCode(String branchName) {
        for (String[] entry : BRANCH_MAP) {
            if (entry[0].equalsIgnoreCase(branchName)) return entry[1];
        }
        // Fallback: first 3 letters uppercased
        return branchName.trim().toUpperCase().substring(0, Math.min(3, branchName.trim().length()));
    }

    /**
     * Returns an array of all branch names — use this to populate the branch combo box.
     */
    public static String[] getBranchNames() {
        String[] names = new String[BRANCH_MAP.length];
        for (int i = 0; i < BRANCH_MAP.length; i++) {
            names[i] = BRANCH_MAP[i][0];
        }
        return names;
    }

    /**
     * Formats an account number from its components.
     *
     * @param branchName     full branch name e.g. "Kampala"
     * @param sequenceNumber the next sequential number from the DB (e.g. 142)
     * @return formatted account number e.g. "KLA-2026-000142"
     */
    public static String generate(String branchName, int sequenceNumber) {
        String branchCode = getBranchCode(branchName);
        int year = LocalDate.now().getYear();
        return String.format("%s-%d-%06d", branchCode, year, sequenceNumber);
    }

    /**
     * Overload — uses an explicit year (useful for testing or back-dating).
     */
    public static String generate(String branchName, int year, int sequenceNumber) {
        String branchCode = getBranchCode(branchName);
        return String.format("%s-%d-%06d", branchCode, year, sequenceNumber);
    }
}
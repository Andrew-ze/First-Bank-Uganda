package src.db;

import src.model.Account;
import src.model.JointAccount;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for the Accounts table.
 * Provides operations:
 *  - insert(account)         — saves a new account record
 *  - getNextSequence(branch) — returns the next sequential number for account numbering
 *  - findAll()                — returns every saved account record, newest first
 */
public class AccountRepository {

    /**
     * Lightweight read-only snapshot of a saved account row.
     * Used to populate the "View Accounts" table without needing
     * to reconstruct a full polymorphic Account subclass.
     *
     * Getters follow JavaBean naming so JavaFX's PropertyValueFactory
     * can bind each TableColumn directly to a field by name.
     */
    public static class AccountRecord {
        public String accountNumber;
        public String accountType;
        public String firstName;
        public String lastName;
        public String nin;
        public String email;
        public String phone;
        public LocalDate dateOfBirth;
        public String branch;
        public double openingDeposit;
        public String secondHolderNin;
        public String createdAt;

        public String getAccountNumber()   { return accountNumber; }
        public String getAccountType()     { return accountType; }
        public String getFirstName()       { return firstName; }
        public String getLastName()        { return lastName; }
        public String getNin()             { return nin; }
        public String getEmail()           { return email; }
        public String getPhone()           { return phone; }
        public LocalDate getDateOfBirth()  { return dateOfBirth; }
        public String getBranch()          { return branch; }
        public double getOpeningDeposit()  { return openingDeposit; }
        public String getSecondHolderNin() { return secondHolderNin; }
        public String getCreatedAt()       { return createdAt; }
    }

    /**
     * Inserts a new account record into the Accounts table.
     *
     * @param account a fully populated Account with account number already set
     * @throws SQLException if the insert fails
     */
    public static void insert(Account account) throws SQLException {
        String sql =
            "INSERT INTO Accounts " +
            "(AccountNumber, AccountType, FirstName, LastName, NIN, " +
            " Email, Phone, DateOfBirth, Branch, OpeningDeposit, " +
            " SecondHolderNIN, CreatedAt) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseManager.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getAccountNumber());
            ps.setString(2, account.getAccountType());
            ps.setString(3, account.getFirstName());
            ps.setString(4, account.getLastName());
            ps.setString(5, account.getNin());
            ps.setString(6, account.getEmail());
            ps.setString(7, account.getPhone());
            ps.setDate  (8, Date.valueOf(account.getDateOfBirth()));
            ps.setString(9, account.getBranch());
            ps.setDouble(10, account.getOpeningDeposit());

            // Second holder NIN — only set for Joint accounts
            if (account instanceof JointAccount joint) {
                ps.setString(11, joint.getSecondHolderNin());
            } else {
                ps.setNull(11, Types.VARCHAR);
            }

            ps.setTimestamp(12, Timestamp.valueOf(
                java.time.LocalDateTime.now().withNano(0)));

            ps.executeUpdate();
        }
    }

    /**
     * Returns the next available sequence number for a given branch in the
     * current year. Used by AccountNumberGenerator to build the account number.
     *
     * Counts existing accounts for this branch/year combination and adds 1.
     * e.g. if KLA already has 141 accounts in 2026, this returns 142.
     *
     * @param branch full branch name e.g. "Kampala"
     * @return next integer sequence number (starts at 1)
     * @throws SQLException if the query fails
     */
    public static int getNextSequence(String branch) throws SQLException {
        int currentYear = LocalDate.now().getYear();
        String yearPrefix = "%-" + currentYear + "-%";

        String sql =
            "SELECT COUNT(*) FROM Accounts " +
            "WHERE Branch = ? AND AccountNumber LIKE ?";

        Connection conn = DatabaseManager.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, branch);
            ps.setString(2, yearPrefix);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        }
        return 1;
    }

    /**
     * Returns every saved account record, ordered newest first.
     * Used to populate the "View Accounts" window.
     *
     * @return list of AccountRecord snapshots; empty list if none saved yet
     * @throws SQLException if the query fails
     */
    public static List<AccountRecord> findAll() throws SQLException {
        List<AccountRecord> records = new ArrayList<>();

        String sql =
            "SELECT AccountNumber, AccountType, FirstName, LastName, NIN, " +
            "       Email, Phone, DateOfBirth, Branch, OpeningDeposit, " +
            "       SecondHolderNIN, CreatedAt " +
            "FROM Accounts " +
            "ORDER BY CreatedAt DESC";

        Connection conn = DatabaseManager.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AccountRecord r = new AccountRecord();
                r.accountNumber    = rs.getString("AccountNumber");
                r.accountType      = rs.getString("AccountType");
                r.firstName        = rs.getString("FirstName");
                r.lastName         = rs.getString("LastName");
                r.nin              = rs.getString("NIN");
                r.email            = rs.getString("Email");
                r.phone            = rs.getString("Phone");
                Date dob           = rs.getDate("DateOfBirth");
                r.dateOfBirth      = dob != null ? dob.toLocalDate() : null;
                r.branch           = rs.getString("Branch");
                r.openingDeposit   = rs.getDouble("OpeningDeposit");
                r.secondHolderNin  = rs.getString("SecondHolderNIN");
                Timestamp created  = rs.getTimestamp("CreatedAt");
                r.createdAt        = created != null ? created.toString() : "";
                records.add(r);
            }
        }
        return records;
    }
}
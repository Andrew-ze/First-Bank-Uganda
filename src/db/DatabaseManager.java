package src.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the JDBC connection to the MS Access database (firstbank.accdb)
 * using the UCanAccess driver.
 *
 * Setup requirements:
 *  Add these JARs to your project's lib/ folder (download from UCanAccess):
 *  - ucanaccess-5.0.1.jar
 *  - hsqldb-2.5.0.jar
 *  - jackcess-3.0.1.jar
 *  - commons-lang3-3.8.1.jar
 *  - commons-logging-1.2.jar
 *
 * The .accdb file lives at:  resources/firstbank.accdb
 * (relative to the project root)
 */
public class DatabaseManager {

    // Path to the Access database file — relative to project root
    private static final String DB_PATH = "resources/firstbank.accdb";

    private static final String JDBC_URL =
        "jdbc:ucanaccess://" + DB_PATH + ";newDatabaseVersion=V2010";

    private static Connection connection = null;

    /**
     * Returns a singleton Connection to the database.
     * Creates the connection (and the Accounts table if needed) on first call.
     *
     * @return active JDBC Connection
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            ensureResourcesFolderExists();
            connection = DriverManager.getConnection(JDBC_URL);
            createTableIfNotExists();
        }
        return connection;
    }

    /**
     * Creates the resources/ folder if it doesn't exist yet, so UCanAccess
     * has somewhere to write the new .accdb file on first run.
     */
    private static void ensureResourcesFolderExists() {
        File resourcesFolder = new File("resources");
        if (!resourcesFolder.exists()) {
            resourcesFolder.mkdirs();
        }
    }

    /**
     * Creates the Accounts table if it does not already exist.
     * This means you do not need to manually set up the .accdb schema —
     * the app creates it on first run.
     */
    private static void createTableIfNotExists() throws SQLException {
        // UCanAccess/MS Access does not support "CREATE TABLE IF NOT EXISTS"
        // so we check if the table exists first, then create it only if needed
        boolean tableExists = false;
        try (ResultSet rs = connection.getMetaData().getTables(
                null, null, "Accounts", new String[]{"TABLE"})) {
            tableExists = rs.next();
        }

        if (!tableExists) {
            String sql =
                "CREATE TABLE Accounts (" +
                "  AccountNumber  VARCHAR(20)  PRIMARY KEY, " +
                "  AccountType    VARCHAR(20)  NOT NULL, " +
                "  FirstName      VARCHAR(50)  NOT NULL, " +
                "  LastName       VARCHAR(50)  NOT NULL, " +
                "  NIN            VARCHAR(14)  NOT NULL, " +
                "  Email          VARCHAR(254) NOT NULL, " +
                "  Phone          VARCHAR(15)  NOT NULL, " +
                "  DateOfBirth    DATE         NOT NULL, " +
                "  Branch         VARCHAR(50)  NOT NULL, " +
                "  OpeningDeposit DOUBLE       NOT NULL, " +
                "  SecondHolderNIN VARCHAR(14), " +
                "  CreatedAt      DATETIME     NOT NULL  " +
                ")";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sql);
            }
        }
    }

    /**
     * Closes the database connection.
     * Call this when the application exits.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
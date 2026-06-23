package src.service;

import java.time.LocalDate;
import java.time.Period;

/**
 * Helper for Date of Birth operations.
 * Handles:
 *  - Determining how many days are in a given month (including leap years)
 *  - Building a LocalDate from day/month/year combo-box selections
 *  - Computing age in whole years
 *  - Checking overall age eligibility (18–75)
 */
public class DOBHelper {

    public static final int MIN_AGE = 18;
    public static final int MAX_AGE = 75;

    /** All month names in order — use these to populate the month combo box. */
    public static final String[] MONTHS = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    /**
     * Returns the number of days in a given month for a given year.
     * Correctly handles February in leap years.
     *
     * @param month 1-based month number (1 = January, 12 = December)
     * @param year  the full 4-digit year (e.g. 2005)
     * @return number of days in that month
     */
    public static int daysInMonth(int month, int year) {
        return switch (month) {
            case 1, 3, 5, 7, 8, 10, 12 -> 31;
            case 4, 6, 9, 11            -> 30;
            case 2                       -> isLeapYear(year) ? 29 : 28;
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }

    /**
     * Returns true if the given year is a leap year.
     * A year is a leap year if:
     *  - divisible by 4, AND
     *  - NOT divisible by 100, UNLESS also divisible by 400
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * Builds a LocalDate from day, month name, and year strings.
     * Returns null if any value is invalid or the combination is impossible.
     *
     * @param day   e.g. "15"
     * @param month e.g. "February"
     * @param year  e.g. "2000"
     * @return a LocalDate, or null if the inputs are invalid
     */
    public static LocalDate buildDate(String day, String month, String year) {
        try {
            int d = Integer.parseInt(day.trim());
            int m = monthNameToNumber(month.trim());
            int y = Integer.parseInt(year.trim());

            if (m == -1) return null;

            int maxDay = daysInMonth(m, y);
            if (d < 1 || d > maxDay) return null;

            return LocalDate.of(y, m, d);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a month name to its 1-based number.
     * @return 1–12, or -1 if not found
     */
    public static int monthNameToNumber(String monthName) {
        for (int i = 0; i < MONTHS.length; i++) {
            if (MONTHS[i].equalsIgnoreCase(monthName)) return i + 1;
        }
        return -1;
    }

    /**
     * Computes age in whole years from a date of birth to today.
     */
    public static int computeAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    /**
     * Validates the date of birth for general account eligibility (ages 18–75).
     *
     * @param day   day string from combo box
     * @param month month name string from combo box
     * @param year  year string from combo box
     * @return null if valid, or an error message string if invalid
     */
    public static String validate(String day, String month, String year) {
        if (day == null || day.isBlank() ||
            month == null || month.isBlank() ||
            year == null || year.isBlank()) {
            return "Date of birth is required.";
        }
        LocalDate dob = buildDate(day, month, year);
        if (dob == null) {
            return "Date of birth is invalid. Check the day is correct for the selected month and year.";
        }
        if (dob.isAfter(LocalDate.now())) {
            return "Date of birth cannot be in the future.";
        }
        int age = computeAge(dob);
        if (age < MIN_AGE) {
            return "Applicant must be at least " + MIN_AGE + " years old.";
        }
        if (age > MAX_AGE) {
            return "Applicant must be no older than " + MAX_AGE + " years.";
        }
        return null;
    }

    /**
     * Generates an array of year strings for the year combo box,
     * from (currentYear - MAX_AGE) to (currentYear - MIN_AGE), descending.
     * e.g. for 2026: 2008 down to 1951
     */
    public static String[] generateYearRange() {
        int currentYear = LocalDate.now().getYear();
        int startYear   = currentYear - MIN_AGE;   // youngest allowed
        int endYear     = currentYear - MAX_AGE;    // oldest allowed
        String[] years  = new String[startYear - endYear + 1];
        for (int i = 0; i <= startYear - endYear; i++) {
            years[i] = String.valueOf(startYear - i);
        }
        return years;
    }
}
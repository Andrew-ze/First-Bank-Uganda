package src.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
//import src.service.AccountNumberGenerator;
import src.service.DOBHelper;

/**
 * Panel containing all personal information fields:
 * First Name, Last Name, NIN, Email, Confirm Email,
 * Phone, PIN, Confirm PIN, and Date of Birth.
 */
public class PersonalInfoPanel extends GridPane {

    // ── Fields ────────────────────────────────────────────────────
    private final TextField     firstNameField    = new TextField();
    private final TextField     lastNameField     = new TextField();
    private final TextField     ninField          = new TextField();
    private final TextField     emailField        = new TextField();
    private final TextField     confirmEmailField = new TextField();
    private final TextField     phoneField        = new TextField();
    private final PasswordField pinField          = new PasswordField();
    private final PasswordField confirmPinField   = new PasswordField();

    // Date of birth combo boxes
    private final ComboBox<String> dayCombo   = new ComboBox<>();
    private final ComboBox<String> monthCombo = new ComboBox<>();
    private final ComboBox<String> yearCombo  = new ComboBox<>();

    public PersonalInfoPanel() {
        buildLayout();
        populateDOBCombos();
        setPromptTexts();
        monthCombo.setOnAction(e -> refreshDays());
        yearCombo.setOnAction(e  -> refreshDays());
    }

    // ── Layout ────────────────────────────────────────────────────
    private void buildLayout() {
        setHgap(12);
        setVgap(10);
        setPadding(new Insets(16));

        // Column constraints: label | field | label | field
        ColumnConstraints lbl  = new ColumnConstraints(130);
        ColumnConstraints fld  = new ColumnConstraints(200);
        fld.setHgrow(Priority.ALWAYS);
        getColumnConstraints().addAll(lbl, fld, lbl, fld);

        int row = 0;

        // Section heading
        Label heading = new Label("Personal Information");
        heading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        add(heading, 0, row++, 4, 1);

        // Row: First Name | Last Name
        addRow(row++,
            label("First Name *"), firstNameField,
            label("Last Name *"),  lastNameField);

        // Row: NIN | Phone
        addRow(row++,
            label("NIN *"),   ninField,
            label("Phone *"), phoneField);

        // Row: Email | Confirm Email
        addRow(row++,
            label("Email *"),         emailField,
            label("Confirm Email *"), confirmEmailField);

        // Row: PIN | Confirm PIN
        addRow(row++,
            label("PIN *"),         pinField,
            label("Confirm PIN *"), confirmPinField);

        // Row: Date of Birth (spans full width)
        Label dobLabel = label("Date of Birth *");
        HBox dobBox = new HBox(8, dayCombo, monthCombo, yearCombo);
        addRow(row, dobLabel, dobBox);
        GridPane.setColumnSpan(dobBox, 3);
    }

    private Label label(String text) {
        Label l = new Label(text);
        l.setWrapText(true);
        return l;
    }

    // ── DOB combo population ──────────────────────────────────────
    private void populateDOBCombos() {
        // Days 1–31 initially; refreshDays() narrows this based on month/year
        for (int d = 1; d <= 31; d++) dayCombo.getItems().add(String.valueOf(d));
        for (String m : DOBHelper.MONTHS)          monthCombo.getItems().add(m);
        for (String y : DOBHelper.generateYearRange()) yearCombo.getItems().add(y);

        dayCombo.setPromptText("Day");
        monthCombo.setPromptText("Month");
        yearCombo.setPromptText("Year");

        dayCombo.setPrefWidth(70);
        monthCombo.setPrefWidth(120);
        yearCombo.setPrefWidth(90);
    }

    /**
     * Refreshes the day combo box when the month or year changes,
     * so February never shows day 30 or 31, and leap years show day 29.
     */
    private void refreshDays() {
        String selectedDay   = dayCombo.getValue();
        String selectedMonth = monthCombo.getValue();
        String selectedYear  = yearCombo.getValue();

        if (selectedMonth == null || selectedYear == null) return;

        int month = DOBHelper.monthNameToNumber(selectedMonth);
        int year;
        try { year = Integer.parseInt(selectedYear); }
        catch (NumberFormatException e) { return; }

        int maxDay = DOBHelper.daysInMonth(month, year);

        dayCombo.getItems().clear();
        for (int d = 1; d <= maxDay; d++) dayCombo.getItems().add(String.valueOf(d));

        // Re-select the previously chosen day if it is still valid
        if (selectedDay != null) {
            try {
                int prevDay = Integer.parseInt(selectedDay);
                if (prevDay <= maxDay) dayCombo.setValue(selectedDay);
            } catch (NumberFormatException ignored) {}
        }
    }

    private void setPromptTexts() {
        firstNameField.setPromptText("e.g. Drew");
        lastNameField.setPromptText("e.g. Salim");
        ninField.setPromptText("e.g. CF123456789SZA");
        emailField.setPromptText("e.g. you@gmail.com");
        confirmEmailField.setPromptText("Re-enter email");
        phoneField.setPromptText("e.g. +256712345678");
        pinField.setPromptText("4-6 digit PIN");
        confirmPinField.setPromptText("Confirm PIN");
    }

    // ── Getters (used by MainFormWindow to build FormFields) ──────
    public String getFirstName()     { return firstNameField.getText(); }
    public String getLastName()      { return lastNameField.getText(); }
    public String getNin()           { return ninField.getText(); }
    public String getEmail()         { return emailField.getText(); }
    public String getConfirmEmail()  { return confirmEmailField.getText(); }
    public String getPhone()         { return phoneField.getText(); }
    public String getPin()           { return pinField.getText(); }
    public String getConfirmPin()    { return confirmPinField.getText(); }
    public String getDobDay()        { return dayCombo.getValue(); }
    public String getDobMonth()      { return monthCombo.getValue(); }
    public String getDobYear()       { return yearCombo.getValue(); }

    /** Clears all fields — called on form reset. */
    public void clear() {
        firstNameField.clear();
        lastNameField.clear();
        ninField.clear();
        emailField.clear();
        confirmEmailField.clear();
        phoneField.clear();
        pinField.clear();
        confirmPinField.clear();
        dayCombo.setValue(null);
        monthCombo.setValue(null);
        yearCombo.setValue(null);
    }
}
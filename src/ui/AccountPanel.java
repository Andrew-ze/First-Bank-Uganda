package src.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import src.service.AccountNumberGenerator;

/**
 * Panel containing account-specific fields:
 * Account Type, Branch, Opening Deposit, and
 * the conditional Second Holder NIN (Joint accounts only).
 */
public class AccountPanel extends GridPane {

    // ── Fields ────────────────────────────────────────────────────
    private final ComboBox<String> accountTypeCombo = new ComboBox<>();
    private final ComboBox<String> branchCombo      = new ComboBox<>();
    private final TextField        depositField      = new TextField();

    // Joint account — hidden unless "Joint" is selected
    private final Label     secondNinLabel = new Label("2nd Holder NIN *");
    private final TextField secondNinField = new TextField();

    public AccountPanel() {
        buildLayout();
        populateCombos();
        setPromptTexts();
        // Show/hide second NIN row based on account type selection
        accountTypeCombo.setOnAction(e -> toggleSecondNin());
        toggleSecondNin(); // initial state
    }

    // ── Layout ────────────────────────────────────────────────────
    private void buildLayout() {
        setHgap(12);
        setVgap(10);
        setPadding(new Insets(16));

        ColumnConstraints lbl = new ColumnConstraints(130);
        ColumnConstraints fld = new ColumnConstraints(200);
        fld.setHgrow(Priority.ALWAYS);
        getColumnConstraints().addAll(lbl, fld, lbl, fld);

        int row = 0;

        Label heading = new Label("Account Details");
        heading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        add(heading, 0, row++, 4, 1);

        // Row: Account Type | Branch
        addRow(row++,
            label("Account Type *"), accountTypeCombo,
            label("Branch *"),       branchCombo);

        // Row: Opening Deposit (spans two columns)
        addRow(row++,
            label("Opening Deposit (UGX) *"), depositField);

        // Row: Second Holder NIN — only visible for Joint accounts
        addRow(row, secondNinLabel, secondNinField);
    }

    private Label label(String text) {
        Label l = new Label(text);
        l.setWrapText(true);
        return l;
    }

    // ── Combo population ──────────────────────────────────────────
    private void populateCombos() {
        accountTypeCombo.getItems().addAll(
            "Savings", "Current", "Fixed Deposit", "Student", "Joint"
        );
        accountTypeCombo.setPromptText("Select type");

        for (String branch : AccountNumberGenerator.getBranchNames()) {
            branchCombo.getItems().add(branch);
        }
        branchCombo.setPromptText("Select branch");

        accountTypeCombo.setPrefWidth(180);
        branchCombo.setPrefWidth(180);
    }

    private void setPromptTexts() {
        depositField.setPromptText("e.g. 50000");
        secondNinField.setPromptText("e.g. CM123456789SZA");
    }

    // ── Toggle second NIN visibility ──────────────────────────────
    private void toggleSecondNin() {
        boolean isJoint = "Joint".equals(accountTypeCombo.getValue());
        secondNinLabel.setVisible(isJoint);
        secondNinLabel.setManaged(isJoint);
        secondNinField.setVisible(isJoint);
        secondNinField.setManaged(isJoint);
        if (!isJoint) secondNinField.clear();
    }

    // ── Getters ───────────────────────────────────────────────────
    public String getAccountType()    { return accountTypeCombo.getValue(); }
    public String getBranch()         { return branchCombo.getValue(); }
    public String getDeposit()        { return depositField.getText(); }
    public String getSecondHolderNin(){ return secondNinField.getText(); }

    /** Clears all fields — called on form reset. */
    public void clear() {
        accountTypeCombo.setValue(null);
        branchCombo.setValue(null);
        depositField.clear();
        secondNinField.clear();
        toggleSecondNin();
    }
}
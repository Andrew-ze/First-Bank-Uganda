package src.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import src.model.*;
import src.service.*;
import src.validation.DepositValidator;
import src.db.AccountRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Root application window for First Bank Uganda Account Opening.
 * Assembles all panels, handles the Submit and Reset buttons,
 * and orchestrates the full submit flow.
 */
public class MainFormWindow {

    private final PersonalInfoPanel  personalPanel  = new PersonalInfoPanel();
    private final AccountPanel       accountPanel   = new AccountPanel();
    private final ValidationErrorPanel errorPanel   = new ValidationErrorPanel();
    private final SummaryPanel       summaryPanel   = new SummaryPanel();

    private final Button submitBtn = new Button("Open Account");
    private final Button resetBtn  = new Button("Reset");
    

    private Stage primaryStage;

    /**
     * Builds and shows the primary stage.
     * Call this from Main.java inside Application.start().
     */
    public void show(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("First Bank Uganda — Account Opening");

        // ── Bank header ───────────────────────────────────────────
        Label bankName = new Label("FIRST BANK UGANDA");
        bankName.setStyle(
            "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #000000;"
        );
        Label tagline = new Label("Account Opening Form");
        tagline.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");
        VBox header = new VBox(4, bankName, tagline);
        header.setPadding(new Insets(16, 16, 8, 16));
        header.setStyle("-fx-background-color: #e8eaf6; -fx-border-color: #202438; -fx-border-width: 0 0 1 0;");

        // ── Button bar ────────────────────────────────────────────
        submitBtn.setStyle(
            "-fx-background-color: #16401f; -fx-text-fill: white;" +
            "-fx-font-size: 13px; -fx-padding: 8 24; -fx-cursor: hand;"
        );
        resetBtn.setStyle(
            "-fx-background-color: #e50a0a; -fx-text-fill: #000000;" +
            "-fx-font-size: 13px; -fx-padding: 8 24; -fx-cursor: hand;"
        );
        HBox buttonBar = new HBox(12, submitBtn, resetBtn);
        buttonBar.setPadding(new Insets(12, 16, 16, 16));

        // ── Separator between the two form panels ─────────────────
        Separator sep = new Separator();

        // ── Root layout ───────────────────────────────────────────
        VBox root = new VBox(
            header,
            personalPanel,
            sep,
            accountPanel,
            errorPanel,
            summaryPanel,
            buttonBar
        );
        root.setFillWidth(true);

        // ── Scroll pane (form can be taller than screen) ──────────
        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(scroll, 780, 680);
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(500);

        // ── Button actions ────────────────────────────────────────
        submitBtn.setOnAction(e -> handleSubmit());
        resetBtn.setOnAction(e  -> handleReset());

        stage.show();
    }

    // ── Submit handler ────────────────────────────────────────────
    private void handleSubmit() {
        errorPanel.clear();
        summaryPanel.clear();

        // 1. Collect raw fields from both panels
        FormValidator.FormFields fields = new FormValidator.FormFields();
        fields.firstName      = personalPanel.getFirstName();
        fields.lastName       = personalPanel.getLastName();
        fields.nin            = personalPanel.getNin();
        fields.email          = personalPanel.getEmail();
        fields.confirmEmail   = personalPanel.getConfirmEmail();
        fields.phone          = personalPanel.getPhone();
        fields.pin            = personalPanel.getPin();
        fields.confirmPin     = personalPanel.getConfirmPin();
        fields.dobDay         = personalPanel.getDobDay();
        fields.dobMonth       = personalPanel.getDobMonth();
        fields.dobYear        = personalPanel.getDobYear();
        fields.accountType    = accountPanel.getAccountType();
        fields.branch         = accountPanel.getBranch();
        fields.openingDeposit = accountPanel.getDeposit();
        fields.secondHolderNin = accountPanel.getSecondHolderNin();

        // 2. Build the Account subclass from the selected type
        Account account = buildAccount(fields);

        // 3. Run all validators
        List<String> errors = FormValidator.validate(fields, account);

        if (!errors.isEmpty()) {
            errorPanel.showErrors(errors);
            showErrorDialog(errors);
            return;
        }

        // 4. Generate account number and save to database
        String accountNumber = null;
        try {
            int seq = AccountRepository.getNextSequence(fields.branch);
            accountNumber = AccountNumberGenerator.generate(fields.branch, seq);
            account.setAccountNumber(accountNumber);
            AccountRepository.insert(account);
            System.out.println("Account saved to database: " + accountNumber);
        } catch (Exception ex) {
            ex.printStackTrace();
            String dbError = "Database error: " + ex.getMessage() +
                " — account details are shown below but were NOT saved.";
            errorPanel.showErrors(List.of(dbError));
            showErrorDialog(List.of(dbError));
        }

        // 5. Show summary + success popup
        summaryPanel.showSummary(account);
        if (accountNumber != null) {
            showSuccessDialog(accountNumber);
        }
    }

    /**
     * Shows a popup dialog listing every validation/database problem,
     * satisfying the brief's "summary dialog listing all problems" requirement.
     */
    private void showErrorDialog(List<String> problems) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Cannot Open Account");
        alert.setHeaderText("Please fix the following " + problems.size() +
            (problems.size() == 1 ? " problem:" : " problems:"));

        StringBuilder body = new StringBuilder();
        for (String problem : problems) {
            body.append("• ").append(problem).append("\n");
        }
        alert.setContentText(body.toString());
        alert.showAndWait();
    }

    /**
     * Shows a success popup confirming the account was created,
     * including the generated account number.
     */
    private void showSuccessDialog(String accountNumber) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Account created successfully!\nAccount Number: " + accountNumber);
        alert.showAndWait();
    }

    /**
     * Constructs the correct Account subclass based on the selected account type.
     * Returns null if the type is not yet selected or DOB is invalid.
     */
    private Account buildAccount(FormValidator.FormFields f) {
        if (f.accountType == null || f.accountType.isBlank()) return null;

        LocalDate dob = DOBHelper.buildDate(f.dobDay, f.dobMonth, f.dobYear);
        if (dob == null) return null;

        double deposit = 0;
        try { deposit = DepositValidator.parse(f.openingDeposit); }
        catch (Exception ignored) {}

        return switch (f.accountType) {
            case "Savings"       -> new SavingsAccount(
                f.firstName, f.lastName, f.nin, f.email, f.phone, f.pin, dob, f.branch, deposit);
            case "Current"       -> new CurrentAccount(
                f.firstName, f.lastName, f.nin, f.email, f.phone, f.pin, dob, f.branch, deposit);
            case "Fixed Deposit" -> new FixedDepositAccount(
                f.firstName, f.lastName, f.nin, f.email, f.phone, f.pin, dob, f.branch, deposit);
            case "Student"       -> new StudentAccount(
                f.firstName, f.lastName, f.nin, f.email, f.phone, f.pin, dob, f.branch, deposit);
            case "Joint"         -> new JointAccount(
                f.firstName, f.lastName, f.nin, f.email, f.phone, f.pin, dob, f.branch, deposit,
                f.secondHolderNin);
            default -> null;
        };
    }

    // ── Reset handler ─────────────────────────────────────────────
    private void handleReset() {
        personalPanel.clear();
        accountPanel.clear();
        errorPanel.clear();
        summaryPanel.clear();
    }
}
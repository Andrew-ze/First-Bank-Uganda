package src.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import src.model.Account;

/**
 * Displays the formatted account summary after a successful form submission.
 * Hidden until a submission succeeds.
 */
public class SummaryPanel extends VBox {

    private final Label    heading  = new Label("✔  Account Opened Successfully");
    private final TextArea summaryArea = new TextArea();

    public SummaryPanel() {
        setSpacing(10);
        setPadding(new Insets(16));
        setStyle(
            "-fx-background-color: #f1f8e9;" +
            "-fx-border-color: #81c784;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );

        heading.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        summaryArea.setEditable(false);
        summaryArea.setWrapText(true);
        summaryArea.setPrefRowCount(12);
        summaryArea.setStyle(
            "-fx-font-family: monospace;" +
            "-fx-font-size: 12px;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
        );

        getChildren().addAll(heading, summaryArea);
        setVisible(false);
        setManaged(false);
    }

    /**
     * Populates and shows the summary for the given account.
     * @param account a fully populated Account with account number assigned
     */
    public void showSummary(Account account) {
        summaryArea.setText(account.getSummary());
        setVisible(true);
        setManaged(true);
    }

    /** Hides the panel and clears the summary text. */
    public void clear() {
        summaryArea.clear();
        setVisible(false);
        setManaged(false);
    }
}
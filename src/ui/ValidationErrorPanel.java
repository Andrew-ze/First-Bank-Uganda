package src.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Displays a list of validation error messages inline below the form.
 * Hidden when there are no errors.
 */
public class ValidationErrorPanel extends VBox {

    private final Label heading = new Label("Please fix the following:");

    public ValidationErrorPanel() {
        setSpacing(4);
        setPadding(new Insets(12));
        setStyle(
            "-fx-background-color: #fff3f3;" +
            "-fx-border-color: #e57373;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );
        heading.setStyle("-fx-font-weight: bold; -fx-text-fill: #b71c1c;");
        getChildren().add(heading);
        setVisible(false);
        setManaged(false);
    }

    /**
     * Shows the panel and populates it with the given error messages.
     * @param errors non-empty list of error strings
     */
    public void showErrors(List<String> errors) {
        // Remove old error labels (keep the heading)
        getChildren().removeIf(n -> n != heading);

        for (String error : errors) {
            Label item = new Label("• " + error);
            item.setStyle("-fx-text-fill: #c62828; -fx-font-size: 12px;");
            item.setWrapText(true);
            getChildren().add(item);
        }
        setVisible(true);
        setManaged(true);
    }

    /** Hides the panel and clears all error labels. */
    public void clear() {
        getChildren().removeIf(n -> n != heading);
        setVisible(false);
        setManaged(false);
    }
}
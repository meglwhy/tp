package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;

/**
 * Clears all entries from the household book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "All entries have been cleared.";
    public static final String MESSAGE_CANCELLED = "Clear command cancelled.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        if (!showConfirmationDialog()) {
            return new CommandResult(MESSAGE_CANCELLED);
        }

        model.getHouseholdBook().resetData(new HouseholdBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Displays a confirmation dialog and returns true if the user confirms.
     */
    private boolean showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Clear");
        alert.setHeaderText("WARNING: This will remove ALL data.");
        alert.setContentText("Are you sure you want to proceed? Press 'OK' to proceed, or 'Cancel' to abort.");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}

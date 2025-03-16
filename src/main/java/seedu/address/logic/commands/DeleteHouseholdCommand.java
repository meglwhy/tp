package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;

/**
 * Deletes a household identified using its household ID from the household book.
 */
public class DeleteHouseholdCommand extends Command {

    public static final String COMMAND_WORD = "delete-household";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the household identified by the household ID.\n"
            + "Parameters: "
            + PREFIX_ID + "HOUSEHOLD_ID\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "1";

    public static final String MESSAGE_DELETE_HOUSEHOLD_SUCCESS = "Deleted Household: %1$s";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID: %1$s";
    public static final String MESSAGE_CANCELLED = "Deletion cancelled.";

    private final HouseholdId targetId;

    public DeleteHouseholdCommand(HouseholdId targetId) {
        requireNonNull(targetId);
        this.targetId = targetId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Household householdToDelete = model.getHouseholdBook().getHouseholdById(targetId)
                .orElseThrow(() -> new CommandException(
                        String.format(MESSAGE_HOUSEHOLD_NOT_FOUND, targetId)));

        // Show confirmation dialog before deleting
        if (!showConfirmationDialog(householdToDelete)) {
            return new CommandResult(MESSAGE_CANCELLED);
        }

        model.getHouseholdBook().removeHousehold(householdToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_HOUSEHOLD_SUCCESS, householdToDelete));
    }

    /**
     * Displays a confirmation dialog before deleting a household.
     * @return true if user confirms, false otherwise.
     * Public for testing.
     */
    @SuppressWarnings("checkstyle:JavadocTagContinuationIndentation")
    public boolean showConfirmationDialog(Household household) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("WARNING: You are about to delete a household.");
        alert.setContentText("Are you sure you want to delete " + household + "? This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeleteHouseholdCommand
                && targetId.equals(((DeleteHouseholdCommand) other).targetId));
    }
}

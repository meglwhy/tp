package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.ui.MainWindow;

/**
 * Clears all entries from the household book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "All entries have been cleared.";
    public static final String MESSAGE_CANCELLED = "Clear command cancelled.";
    /**
     * Executes the command to clear all data from the household book, prompting the user for confirmation.
     *
     * @param model The model whose household book data is to be cleared. Must not be null.
     * @return A {@code CommandResult} indicating the result of the clear operation.
     *         Returns a cancellation message if the user declines the confirmation.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        if (!MainWindow.showClearConfirmationDialog()) {
            return new CommandResult(MESSAGE_CANCELLED);
        }

        model.getHouseholdBook().resetData(new HouseholdBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

}

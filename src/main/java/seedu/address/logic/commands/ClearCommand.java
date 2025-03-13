package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
/**
 * Clears all entries from the household book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "All entries have been cleared";
    public static final String MESSAGE_CONFIRMATION = "Are you sure you want to clear all entries? This action cannot be undone. (Y/N)";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.getHouseholdBook().resetData(new HouseholdBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}

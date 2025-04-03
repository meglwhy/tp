package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Lists all households in the household book.
 */
public class ListHouseholdsCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all households in the household book.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all households.";
    /**
     * Executes the command to display the total number of households in the model.
     *
     * @param model The model containing the list of households. Must not be null.
     * @return A {@code CommandResult} containing a success message with the total count of households.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredHouseholdList(unused -> true); // No filter, show all

        int householdCount = model.getFilteredHouseholdList().size();

        return new CommandResult(
                String.format(MESSAGE_SUCCESS + "\nTotal households: %d", householdCount));
    }
}

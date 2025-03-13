package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;
import seedu.address.model.household.Household;

/**
 * Lists all households in the household book.
 */
public class ListHouseholdsCommand extends Command {

    public static final String COMMAND_WORD = "list-households";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all households in the household book.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all households.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        // Get the entire list of households
        model.updateFilteredHouseholdList(unused -> true);  // No filter, show all

        // Get the number of households in the entire list
        int householdCount = model.getFilteredHouseholdList().size();

        // Return the result showing all households
        return new CommandResult(
                String.format(MESSAGE_SUCCESS + "\nTotal households: %d", householdCount));
    }
}

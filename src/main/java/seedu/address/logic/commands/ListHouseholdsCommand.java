package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.HouseholdBook;

/**
 * Lists all households in the household book to the user.
 */
public class ListHouseholdsCommand extends Command {

    public static final String COMMAND_WORD = "list-households";

    public static final String MESSAGE_SUCCESS = "Listed all households";
    public static final String MESSAGE_NO_HOUSEHOLDS = "No households found";

    @Override
    public CommandResult execute(HouseholdBook householdBook) {
        requireNonNull(householdBook);
        
        if (householdBook.getHouseholdList().isEmpty()) {
            return new CommandResult(MESSAGE_NO_HOUSEHOLDS);
        }
        
        return new CommandResult(MESSAGE_SUCCESS);
    }
} 
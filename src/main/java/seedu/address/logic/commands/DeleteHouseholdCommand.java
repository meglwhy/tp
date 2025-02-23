package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.Model;

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
            + PREFIX_ID + "H000001";

    public static final String MESSAGE_DELETE_HOUSEHOLD_SUCCESS = "Deleted Household: %1$s";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID: %1$s";

    private final HouseholdId targetId;

    public DeleteHouseholdCommand(HouseholdId targetId) {
        requireNonNull(targetId);
        this.targetId = targetId;
    }

    @Override
    public CommandResult execute(Model householdBook) throws CommandException {
        requireNonNull(householdBook);

        Household householdToDelete = householdBook.getHouseholdBook().getHouseholdById(targetId)
                .orElseThrow(() -> new CommandException(
                        String.format(MESSAGE_HOUSEHOLD_NOT_FOUND, targetId)));

        householdBook.getHouseholdBook().removeHousehold(householdToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_HOUSEHOLD_SUCCESS, householdToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteHouseholdCommand // instanceof handles nulls
                && targetId.equals(((DeleteHouseholdCommand) other).targetId)); // state check
    }
} 
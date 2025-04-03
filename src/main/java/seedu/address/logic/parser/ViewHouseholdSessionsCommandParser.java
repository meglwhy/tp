package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ViewHouseholdSessionsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

/**
 * Parses input arguments and creates a new ViewHouseholdSessionsCommand object.
 */
public class ViewHouseholdSessionsCommandParser implements Parser<ViewHouseholdSessionsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewHouseholdSessionsCommand
     * and returns a ViewHouseholdSessionsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewHouseholdSessionsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (!trimmedArgs.startsWith("id/")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ViewHouseholdSessionsCommand.MESSAGE_USAGE));
        }

        String idValue = trimmedArgs.substring(3).trim();
        if (idValue.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ViewHouseholdSessionsCommand.MESSAGE_USAGE));
        }

        // Validate id using HouseholdId methods.
        if (!HouseholdId.isValidId(idValue)) {
            throw new ParseException(HouseholdId.MESSAGE_CONSTRAINTS);
        }
        HouseholdId householdId = HouseholdId.fromString(idValue);
        return new ViewHouseholdSessionsCommand(householdId);
    }
}

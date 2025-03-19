package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ListSessionsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

public class ListSessionsCommandParser implements Parser<ListSessionsCommand> {

    @Override
    public ListSessionsCommand parse(String userInput) throws ParseException {
        String trimmedInput = userInput.trim();

        // The expected format is "id/HOUSEHOLD_ID"
        if (!trimmedInput.startsWith("id/")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListSessionsCommand.MESSAGE_USAGE));
        }
        String householdIdStr = trimmedInput.substring(3).trim(); // remove the "id/" prefix
        if (householdIdStr.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListSessionsCommand.MESSAGE_USAGE));
        }
        if (!HouseholdId.isValidId(householdIdStr)) {
            throw new ParseException("Invalid household ID: " + householdIdStr);
        }
        HouseholdId householdId = HouseholdId.fromString(householdIdStr);
        return new ListSessionsCommand(householdId);
    }
}


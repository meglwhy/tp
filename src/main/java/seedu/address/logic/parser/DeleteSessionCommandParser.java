package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.DeleteSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

public class DeleteSessionCommandParser implements Parser<DeleteSessionCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid format! Usage: delete-session <HOUSEHOLD_ID>-<SESSION_INDEX>\n"
                    + "Example: delete-session H000002-2";

    @Override
    public DeleteSessionCommand parse(String userInput) throws ParseException {
        // Trim whitespace, e.g. " H000002-2 " -> "H000002-2"
        userInput = userInput.trim();

        // Must contain a dash
        if (!userInput.contains("-")) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        // Split into household ID and session index
        String[] parts = userInput.split("-", 2);
        if (parts.length < 2) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        String householdIdStr = parts[0].trim();  // "H000002"
        String sessionIndexStr = parts[1].trim(); // "2"

        // Validate the household ID
        if (!HouseholdId.isValidId(householdIdStr)) {
            throw new ParseException("Invalid household ID: " + householdIdStr);
        }
        HouseholdId householdId = HouseholdId.fromString(householdIdStr);

        // Parse the session index
        int sessionIndex;
        try {
            sessionIndex = Integer.parseInt(sessionIndexStr);
        } catch (NumberFormatException e) {
            throw new ParseException("Session index must be an integer: " + sessionIndexStr);
        }

        return new DeleteSessionCommand(householdId, sessionIndex);
    }
}


package seedu.address.logic.parser;

import seedu.address.logic.commands.ViewFullSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

/**
 * Parses input arguments and creates a new {@code ViewFullSessionCommand} object.
 *
 * <p>Expected input format: {@code id/HOUSEHOLD_ID-SESSION_INDEX}</p>
 *
 * <p>Example usage: {@code view-full-s id/H000001-2}</p>
 */
public class ViewFullSessionCommandParser implements Parser<ViewFullSessionCommand> {
    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid format! Usage: view-full-s id/HOUSEHOLD_ID-SESSION_INDEX";

    @Override
    public ViewFullSessionCommand parse(String userInput) throws ParseException {
        userInput = userInput.trim();
        if (!userInput.startsWith("id/")) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        String sessionIdentifier = userInput.substring(3).trim();
        String[] parts = sessionIdentifier.split("-", 2);

        if (parts.length < 2) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        String householdIdStr = parts[0].trim();
        String sessionIndexStr = parts[1].trim();

        if (!HouseholdId.isValidId(householdIdStr)) {
            throw new ParseException("Invalid household ID: " + householdIdStr);
        }

        HouseholdId householdId = HouseholdId.fromString(householdIdStr);

        int sessionIndex;
        try {
            sessionIndex = Integer.parseInt(sessionIndexStr);
        } catch (NumberFormatException e) {
            throw new ParseException("Session index must be an integer: " + sessionIndexStr);
        }

        return new ViewFullSessionCommand(householdId, sessionIndex);
    }
}

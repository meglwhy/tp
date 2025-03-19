package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.EditSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

public class EditSessionCommandParser implements Parser<EditSessionCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid format! Usage: edit-session <HOUSEHOLD_ID-SESSION_INDEX> d/DATE t/TIME\n"
                    + "Example: edit-session H000006-2 d/2025-03-16 t/15:00";

    @Override
    public EditSessionCommand parse(String userInput) throws ParseException {
        String trimmedInput = userInput.trim();

        // Find the positions of the date and time tokens.
        int dateIndex = trimmedInput.indexOf("d/");
        int timeIndex = trimmedInput.indexOf("t/");
        if (dateIndex == -1 || timeIndex == -1) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        // The session identifier is the part before "d/".
        String sessionIdentifier = trimmedInput.substring(0, dateIndex).trim();
        // Extract the date: from after "d/" up to "t/".
        String datePart = trimmedInput.substring(dateIndex + 2, timeIndex).trim();
        // Extract the time: from after "t/" to the end.
        String timePart = trimmedInput.substring(timeIndex + 2).trim();

        if (sessionIdentifier.isEmpty() || datePart.isEmpty() || timePart.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        // Expect the sessionIdentifier to be in the format "H000006-2".
        if (!sessionIdentifier.contains("-")) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }
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

        return new EditSessionCommand(householdId, sessionIndex, datePart, timePart);
    }
}




package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AddNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

public class AddNoteCommandParser implements Parser<AddNoteCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid format! Usage: add-note <HOUSEHOLD_ID-SESSION_INDEX> n/NOTE\n"
                    + "Example: add-note H000006-2 n/Follow-up on medical assistance application";

    @Override
    public AddNoteCommand parse(String userInput) throws ParseException {
        String trimmedInput = userInput.trim();

        // The input must contain "n/" for the note.
        int noteIndex = trimmedInput.indexOf("n/");
        if (noteIndex == -1) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }
        // Everything before "n/" should be the session identifier.
        String sessionIdentifier = trimmedInput.substring(0, noteIndex).trim();
        // Everything after "n/" is the note.
        String note = trimmedInput.substring(noteIndex + 2).trim();
        if (sessionIdentifier.isEmpty() || note.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        // Expect the sessionIdentifier to be in the form "H000006-2"
        if (!sessionIdentifier.contains("-")) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }
        String[] parts = sessionIdentifier.split("-", 2);
        if (parts.length < 2) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }
        String householdIdStr = parts[0].trim();
        String sessionIndexStr = parts[1].trim();

        // Validate the household ID.
        if (!HouseholdId.isValidId(householdIdStr)) {
            throw new ParseException("Invalid household ID: " + householdIdStr);
        }
        HouseholdId householdId = HouseholdId.fromString(householdIdStr);

        // Parse the session index.
        int sessionIndex;
        try {
            sessionIndex = Integer.parseInt(sessionIndexStr);
        } catch (NumberFormatException e) {
            throw new ParseException("Session index must be an integer: " + sessionIndexStr);
        }

        return new AddNoteCommand(householdId, sessionIndex, note);
    }
}


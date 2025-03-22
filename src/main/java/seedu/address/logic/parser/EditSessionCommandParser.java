package seedu.address.logic.parser;

import seedu.address.logic.commands.EditSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

import static seedu.address.logic.parser.CliSyntax.*;

/**
 * Parses input for the edit-session command.
 * Expected format: "edit-session <code>id/HOUSEHOLD_ID-SESSION_INDEX</code> d/DATE tm/TIME [n/NOTE]"
 * Example: "edit-session <code>id/H000007-2</code> d/2025-09-27 tm/19:00"
 * Example with note: "edit-session id/H000007-2 d/2025-09-27 tm/19:00 n/Follow-up"
 */
public class EditSessionCommandParser implements Parser<EditSessionCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid format! Usage: edit-session id/<HOUSEHOLD_ID-SESSION_INDEX> d/DATE tm/TIME [n/NOTE]\n"
                    + "Example: edit-session id/H000007-2 d/2025-09-27 tm/19:00\n"
                    + "Example with note: edit-session id/H000007-2 d/2025-09-27 tm/19:00 n/Follow-up";

    @Override
    public EditSessionCommand parse(String userInput) throws ParseException {
        // Tokenize the input while handling extra whitespace
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_ID,
                PREFIX_DATE, PREFIX_TIME, PREFIX_NOTE);

        if (!argMultimap.arePrefixesPresent(PREFIX_ID, PREFIX_DATE, PREFIX_TIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        // Extract the session identifier and trim extra whitespace.
        String sessionIdentifier = argMultimap.getValue(PREFIX_ID).get().trim();
        // If the identifier still starts with "id/", remove it.
        if (sessionIdentifier.startsWith("id/")) {
            sessionIdentifier = sessionIdentifier.substring(3).trim();
        }
        // Ensure the identifier is in the format "HOUSEHOLD_ID-SESSION_INDEX"
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

        // Extract and trim the date and time tokens.
        String datePart = argMultimap.getValue(PREFIX_DATE).get().trim();
        String timePart = argMultimap.getValue(PREFIX_TIME).get().trim();
        if (datePart.isEmpty() || timePart.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        // Check if an optional note is provided.
        if (argMultimap.getValue(PREFIX_NOTE).isPresent()) {
            String notePart = argMultimap.getValue(PREFIX_NOTE).get().trim();
            if (notePart.isEmpty()) {
                throw new ParseException("Note cannot be empty");
            }
            return new EditSessionCommand(householdId, sessionIndex, datePart, timePart, notePart);
        } else {
            return new EditSessionCommand(householdId, sessionIndex, datePart, timePart);
        }
    }
}







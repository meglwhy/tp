package seedu.address.logic.parser;

import seedu.address.logic.commands.AddNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

/**
 * Parses input for the add-note command.
 * Expected format: "id/H000006-2 n/NOTE"
 * Example: "add-note id/H000006-2 n/Follow-up on medical assistance application"
 */
public class AddNoteCommandParser implements Parser<AddNoteCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid format! Usage: add-note id/<HOUSEHOLD_ID-SESSION_INDEX> n/NOTE\n"
                    + "Example: add-note id/H000006-2 n/Follow-up on medical assistance application";

    private static final Prefix PREFIX_ID = new Prefix("id/");
    private static final Prefix PREFIX_NOTE = new Prefix("n/");

    @Override
    public AddNoteCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_ID, PREFIX_NOTE);

        if (!argMultimap.arePrefixesPresent(PREFIX_ID, PREFIX_NOTE) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        String sessionIdentifier = argMultimap.getValue(PREFIX_ID).get().trim();
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

        String note = argMultimap.getValue(PREFIX_NOTE).get().trim();
        if (note.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        return new AddNoteCommand(householdId, sessionIndex, note);
    }
}



package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import seedu.address.logic.commands.EditSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input for the edit-s command.
 */
public class EditSessionCommandParser implements Parser<EditSessionCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid format! Usage: edit-s id/<HOUSEHOLD_ID-SESSION_INDEX> [d/DATE] [tm/TIME] [n/NOTE]\n"
                    + "Example: edit-s id/H000007-2 d/2025-09-27 tm/19:00\n"
                    + "Example with note: edit-s id/H000007-2 n/Follow-up";

    public static final String MESSAGE_NO_FIELDS_PROVIDED =
            "At least one field to edit must be provided (date, time, or note).";

    @Override
    public EditSessionCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_ID, PREFIX_DATE,
                PREFIX_TIME, PREFIX_NOTE);

        if (!argMultimap.arePrefixesPresent(PREFIX_ID) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        if (argMultimap.getValue(PREFIX_DATE).isEmpty()
                && argMultimap.getValue(PREFIX_TIME).isEmpty()
                && argMultimap.getValue(PREFIX_NOTE).isEmpty()) {
            throw new ParseException(MESSAGE_NO_FIELDS_PROVIDED);
        }

        // Use the helper to parse the session identifier.
        SessionIdentifier sessionIdentifier = SessionParserUtil.parseSessionIdentifier(
                argMultimap.getValue(PREFIX_ID).orElseThrow(() -> new ParseException(MESSAGE_INVALID_FORMAT)).trim()
        );

        // Validate optional fields: trim and check if empty if provided.
        String datePart = getValidatedField(argMultimap.getValue(PREFIX_DATE), "Date provided is empty.");
        String timePart = getValidatedField(argMultimap.getValue(PREFIX_TIME), "Time provided is empty.");
        String notePart = getValidatedField(argMultimap.getValue(PREFIX_NOTE), "Note provided is empty.");

        return new EditSessionCommand(sessionIdentifier.getHouseholdId(), sessionIdentifier.getSessionIndex(),
                datePart, timePart, notePart);
    }

    private String getValidatedField(java.util.Optional<String> field, String errorMessage) throws ParseException {
        if (field.isPresent()) {
            String value = field.get().trim();
            if (value.isEmpty()) {
                throw new ParseException(errorMessage);
            }
            return value;
        }
        return null;
    }
}



package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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

    @Override
    public EditSessionCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_ID, PREFIX_DATE,
                PREFIX_TIME, PREFIX_NOTE);

        if (!argMultimap.arePrefixesPresent(PREFIX_ID) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditSessionCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_DATE).isEmpty()
                && argMultimap.getValue(PREFIX_TIME).isEmpty()
                && argMultimap.getValue(PREFIX_NOTE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditSessionCommand.MESSAGE_USAGE));
        }

        // Use the helper to parse the session identifier.
        SessionIdentifier sessionIdentifier = SessionParserUtil.parseSessionIdentifier(
                argMultimap.getValue(PREFIX_ID).orElseThrow(() -> {
                    return new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            EditSessionCommand.MESSAGE_USAGE));
                }).trim());

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



package seedu.address.logic.parser;

import java.util.Optional;

import seedu.address.logic.commands.DeleteSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input for the delete-s command.
 */
public class DeleteSessionCommandParser implements Parser<DeleteSessionCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid format! Usage: delete-s id/<HOUSEHOLD_ID-SESSION_INDEX>\n"
                    + "Example: delete-s id/H000002-2";

    private static final Prefix PREFIX_ID = new Prefix("id/");

    @Override
    public DeleteSessionCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_ID);

        if (!argMultimap.arePrefixesPresent(PREFIX_ID) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        Optional<String> idAndIndexOpt = argMultimap.getValue(PREFIX_ID);
        if (idAndIndexOpt.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        // Use the helper to parse the session identifier.
        SessionIdentifier sessionIdentifier = SessionParserUtil.parseSessionIdentifier(idAndIndexOpt.get().trim());

        return new DeleteSessionCommand(sessionIdentifier.getHouseholdId(), sessionIdentifier.getSessionIndex());
    }
}




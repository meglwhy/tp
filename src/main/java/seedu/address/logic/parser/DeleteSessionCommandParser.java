package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Optional;

import seedu.address.logic.commands.DeleteSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input for the delete-s command.
 */
public class DeleteSessionCommandParser implements Parser<DeleteSessionCommand> {

    private static final Prefix PREFIX_ID = new Prefix("id/");

    @Override
    public DeleteSessionCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_ID);

        if (!argMultimap.arePrefixesPresent(PREFIX_ID) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteSessionCommand.MESSAGE_USAGE));
        }

        Optional<String> idAndIndexOpt = argMultimap.getValue(PREFIX_ID);
        if (idAndIndexOpt.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteSessionCommand.MESSAGE_USAGE));
        }

        // Use the helper to parse the session identifier.
        SessionIdentifier sessionIdentifier = SessionParserUtil.parseSessionIdentifier(idAndIndexOpt.get().trim());

        return new DeleteSessionCommand(sessionIdentifier.getHouseholdId(), sessionIdentifier.getSessionIndex());
    }
}




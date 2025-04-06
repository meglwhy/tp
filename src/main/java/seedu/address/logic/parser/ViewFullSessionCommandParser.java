package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import seedu.address.logic.commands.ViewFullSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

/**
 * Parses input arguments and creates a new {@code ViewFullSessionCommand} object.
 */
public class ViewFullSessionCommandParser implements Parser<ViewFullSessionCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the ViewFullSessionCommand
     * and returns a ViewFullSessionCommand object for execution.
     *
     * @param args The input arguments provided by the user.
     * @return A ViewFullSessionCommand object with the parsed household ID and session index.
     * @throws ParseException if the user input does not conform to the expected format or
     *                        if the household ID or session index is invalid.
     */
    @Override
    public ViewFullSessionCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ID);

        if (argMultimap.getValue(PREFIX_ID).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ViewFullSessionCommand.MESSAGE_USAGE));
        }

        SessionIdentifier parsed = SessionParserUtil.parseSessionIdentifier(
                argMultimap.getValue(PREFIX_ID).get());
        return new ViewFullSessionCommand(parsed.getHouseholdId(), parsed.getSessionIndex());
    }
}

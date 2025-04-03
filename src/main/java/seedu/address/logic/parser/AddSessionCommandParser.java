package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import seedu.address.logic.commands.AddSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;

/**
 * Parses input arguments and creates a new AddSessionCommand object
 */
public class AddSessionCommandParser implements Parser<AddSessionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddSessionCommand
     * and returns an {@code AddSessionCommand} object for execution.
     *
     * @param args The string of arguments to parse. Must contain an ID, date, and time.
     * @return An {@code AddSessionCommand} object initialized with the parsed session data.
     * @throws ParseException If the user input does not conform to the expected format or is missing required prefixes.
     */
    public AddSessionCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_DATE, PREFIX_TIME);

        if (!argMultimap.arePrefixesPresent(PREFIX_ID, PREFIX_DATE, PREFIX_TIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddSessionCommand.MESSAGE_USAGE));
        }

        HouseholdId householdId = ParserUtil.parseHouseholdId(
                argMultimap.getValue(PREFIX_ID).orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSessionCommand.MESSAGE_USAGE))));

        SessionDate date = ParserUtil.parseDate(
                argMultimap.getValue(PREFIX_DATE).orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSessionCommand.MESSAGE_USAGE))));

        SessionTime time = ParserUtil.parseTime(
                argMultimap.getValue(PREFIX_TIME).orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSessionCommand.MESSAGE_USAGE))));

        return new AddSessionCommand(householdId, date, time);
    }
}

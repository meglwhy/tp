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
     * and returns an AddSessionCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddSessionCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_DATE, PREFIX_TIME);

        if (!argMultimap.arePrefixesPresent(PREFIX_ID, PREFIX_DATE, PREFIX_TIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddSessionCommand.MESSAGE_USAGE));
        }

        HouseholdId householdId = ParserUtil.parseHouseholdId(argMultimap.getValue(PREFIX_ID).get());
        SessionDate date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        SessionTime time = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME).get());

        return new AddSessionCommand(householdId, date, time);
    }
}

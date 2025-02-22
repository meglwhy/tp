package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import seedu.address.logic.commands.ViewHistoryCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

/**
 * Parses input arguments and creates a new ViewHistoryCommand object
 */
public class ViewHistoryCommandParser implements Parser<ViewHistoryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewHistoryCommand
     * and returns a ViewHistoryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewHistoryCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ID);

        if (!argMultimap.getValue(PREFIX_ID).isPresent() || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewHistoryCommand.MESSAGE_USAGE));
        }

        HouseholdId householdId = ParserUtil.parseHouseholdId(argMultimap.getValue(PREFIX_ID).get());

        return new ViewHistoryCommand(householdId);
    }
} 
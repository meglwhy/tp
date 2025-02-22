package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindHouseholdCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Parses input arguments and creates a new FindHouseholdCommand object
 */
public class FindHouseholdCommandParser implements Parser<FindHouseholdCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindHouseholdCommand
     * and returns a FindHouseholdCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindHouseholdCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindHouseholdCommand.MESSAGE_USAGE));
        }

        String[] keywords = trimmedArgs.split("\\s+");
        try {
            return new FindHouseholdCommand(String.join(" ", keywords));
        } catch (CommandException e) {
            throw new ParseException(e.getMessage());
        }
    }
} 
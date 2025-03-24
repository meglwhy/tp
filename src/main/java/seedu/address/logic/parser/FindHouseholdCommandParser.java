package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.FindHouseholdCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindHouseholdCommand object.
 */
public class FindHouseholdCommandParser implements Parser<FindHouseholdCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of FindHouseholdCommand
     * and returns a FindHouseholdCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public FindHouseholdCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindHouseholdCommand.MESSAGE_USAGE));
        }

        // Pass the full trimmed input directly to FindHouseholdCommand
        try {
            return new FindHouseholdCommand(trimmedArgs);
        } catch (CommandException e) {
            throw new ParseException(e.getMessage());
        }
    }
}

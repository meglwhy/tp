package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import seedu.address.logic.commands.ViewFullSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

/**
 * Parses input arguments and creates a new {@code ViewFullSessionCommand} object.
 *
 * <p>Expected input format: {@code id/HOUSEHOLD_ID-SESSION_INDEX}</p>
 *
 * <p>Example usage: {@code view-full-s id/H000001-2}</p>
 */
public class ViewFullSessionCommandParser implements Parser<ViewFullSessionCommand> {

    @Override
    public ViewFullSessionCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ID);

        if (!argMultimap.getValue(PREFIX_ID).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ViewFullSessionCommand.MESSAGE_USAGE));
        }

        String sessionIdentifier = argMultimap.getValue(PREFIX_ID).get().trim();
        String[] parts = sessionIdentifier.split("-", 2);

        if (parts.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ViewFullSessionCommand.MESSAGE_USAGE));
        }

        String householdIdStr = parts[0].trim();
        String sessionIndexStr = parts[1].trim();

        if (!HouseholdId.isValidId(householdIdStr)) {
            throw new ParseException("Invalid household ID: " + householdIdStr);
        }

        HouseholdId householdId = HouseholdId.fromString(householdIdStr);

        int sessionIndex;
        try {
            sessionIndex = Integer.parseInt(sessionIndexStr);
        } catch (NumberFormatException e) {
            throw new ParseException("Session index must be an integer: " + sessionIndexStr);
        }

        return new ViewFullSessionCommand(householdId, sessionIndex);
    }
}

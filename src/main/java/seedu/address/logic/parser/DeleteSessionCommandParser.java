package seedu.address.logic.parser;

import java.util.Optional;

import seedu.address.logic.commands.DeleteSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

/**
 * Parses input for the delete-session command.
 */
public class DeleteSessionCommandParser implements Parser<DeleteSessionCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid format! Usage: delete-session id/<HOUSEHOLD_ID-SESSION_INDEX>\n"
                    + "Example: delete-session id/H000002-2";

    private static final Prefix PREFIX_ID = new Prefix("id/");
    /**
     * Parses the given {@code String} of arguments in the context of the {@code DeleteSessionCommand}
     * and returns a {@code DeleteSessionCommand} object for execution.
     *
     * @param userInput The string representing the user input to be parsed.
     * @return A {@code DeleteSessionCommand} object containing the parsed {@code HouseholdId} and session index.
     * @throws ParseException If the user input does not conform to the expected format.
     */
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

        String idAndIndex = idAndIndexOpt.get().trim();

        if (!idAndIndex.contains("-")) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }
        String[] parts = idAndIndex.split("-", 2);
        if (parts.length < 2) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
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

        return new DeleteSessionCommand(householdId, sessionIndex);
    }
}



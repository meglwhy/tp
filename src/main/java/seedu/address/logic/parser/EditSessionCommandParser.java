package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.EditSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;

/**
 * Parses input for the edit-s command.
 */
public class EditSessionCommandParser implements Parser<EditSessionCommand> {
    private final Model model;

    public EditSessionCommandParser(Model model) {
        this.model = model;
    }

    @Override
    public EditSessionCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput,
                PREFIX_ID, PREFIX_DATE, PREFIX_TIME, PREFIX_NOTE);

        if (!argMultimap.arePrefixesPresent(PREFIX_ID) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditSessionCommand.MESSAGE_USAGE));
        }
        SessionIdentifier sessionIdentifier = SessionParserUtil.parseSessionIdentifier(
                argMultimap.getValue(PREFIX_ID).orElseThrow(() ->
                                new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                        EditSessionCommand.MESSAGE_USAGE)))
                        .trim());

        HouseholdId householdId = sessionIdentifier.getHouseholdId();
        int sessionIndex = sessionIdentifier.getSessionIndex();

        Household household = model.getHouseholdBook().getHouseholdById(householdId)
                .orElseThrow(() -> new ParseException(String.format(EditSessionCommand.MESSAGE_HOUSEHOLD_NOT_FOUND,
                        householdId)));

        ObservableList<Session> sessions = household.getSessions();
        if (sessionIndex < 1 || sessionIndex > sessions.size()) {
            throw new ParseException(String.format(EditSessionCommand.MESSAGE_INVALID_SESSION_INDEX, sessionIndex,
                    householdId));
        }

        if (argMultimap.getValue(PREFIX_DATE).isEmpty()
                && argMultimap.getValue(PREFIX_TIME).isEmpty()
                && argMultimap.getValue(PREFIX_NOTE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditSessionCommand.MESSAGE_USAGE));
        }


        // Validate optional fields
        String datePart = getValidatedField(argMultimap.getValue(PREFIX_DATE), "Date provided is empty.");
        String timePart = getValidatedField(argMultimap.getValue(PREFIX_TIME), "Time provided is empty.");
        String notePart = getValidatedField(argMultimap.getValue(PREFIX_NOTE), "Note provided is empty.");

        return new EditSessionCommand(householdId, sessionIndex, datePart, timePart, notePart);
    }

    private String getValidatedField(java.util.Optional<String> field, String errorMessage) throws ParseException {
        if (field.isPresent()) {
            String value = field.get().trim();
            if (value.isEmpty()) {
                throw new ParseException(errorMessage);
            }
            return value;
        }
        return null;
    }
}



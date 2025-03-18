package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.*;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input for the household management system.
 */
public class HouseholdBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        switch (commandWord) {

        case AddHouseholdCommand.COMMAND_WORD:
            return new AddHouseholdCommandParser().parse(arguments);

        case AddSessionCommand.COMMAND_WORD:
            return new AddSessionCommandParser().parse(arguments);

        case EditHouseholdCommand.COMMAND_WORD:
            return new EditHouseholdCommandParser().parse(arguments);

        case EditSessionCommand.COMMAND_WORD:
            return new EditSessionCommandParser().parse(arguments);

        case DeleteHouseholdCommand.COMMAND_WORD:
            return new DeleteHouseholdCommandParser().parse(arguments);

            case DeleteSessionCommand.COMMAND_WORD:
                return new DeleteSessionCommandParser().parse(arguments);

            case AddNoteCommand.COMMAND_WORD:
                return new AddNoteCommandParser().parse(arguments);

            case ListSessionsCommand.COMMAND_WORD:
                return new ListSessionsCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindHouseholdCommand.COMMAND_WORD:
            return new FindHouseholdCommandParser().parse(arguments);

        case ListHouseholdsCommand.COMMAND_WORD:
            return new ListHouseholdsCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}

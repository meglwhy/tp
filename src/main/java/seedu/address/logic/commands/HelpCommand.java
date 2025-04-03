package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {
    public static final String COMMAND_WORD = "help";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;
    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";
    /**
     * Executes the command to show the help message.
     *
     * @param householdBook The model representing the household data. Must not be null.
     * @return A {@code CommandResult} containing a message to display the help instructions,
     *         with flags indicating that the help view should be shown.
     */
    @Override
    public CommandResult execute(Model householdBook) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}

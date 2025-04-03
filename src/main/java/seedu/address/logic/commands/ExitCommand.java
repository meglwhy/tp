package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Address Book as requested ...";
    /**
     * Executes the command to exit the application, returning an exit acknowledgment.
     *
     * @param householdBook The model representing the household data. Must not be null.
     * @return A {@code CommandResult} indicating the result of the exit command, including
     *         a message acknowledging the exit, and flags for indicating whether the application
     *         should be closed.
     */
    @Override
    public CommandResult execute(Model householdBook) {
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
    }

}

package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;

/**
 * Represents a command to delete a specific session from a household.
 * The session is identified using a household ID and a session index.
 *
 * Usage example:
 * <pre>
 *     delete-session H000002-2
 * </pre>
 *
 * This command performs the following checks:
 * - Ensures the household with the given ID exists.
 * - Ensures the session index is valid for the specified household.
 * - Removes the session from both the household and the global session list.
 *
 * Upon successful execution, it returns a message confirming the deleted session.
 */
public class DeleteSessionCommand extends Command {

    public static final String COMMAND_WORD = "delete-session";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the session identified by "
            + "HOUSEHOLD_ID-SESSION_INDEX.\n"
            + "Example: " + COMMAND_WORD + " H000002-2";
    public static final String MESSAGE_DELETE_SESSION_SUCCESS = "Deleted session %d from household %s: %s";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID %s.";
    public static final String MESSAGE_INVALID_SESSION_INDEX = "Session index %d is invalid for household %s.";

    private final HouseholdId householdId;
    private final int sessionIndex; // 1-based

    /**
     * Creates a {@code DeleteSessionCommand} to delete a session from a household.
     *
     * @param householdId  The unique identifier of the household.
     * @param sessionIndex The 1-based index of the session to be deleted.
     * @throws NullPointerException if {@code householdId} is null.
     */
    public DeleteSessionCommand(HouseholdId householdId, int sessionIndex) {
        requireNonNull(householdId);
        this.householdId = householdId;
        this.sessionIndex = sessionIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Find the household
        Optional<Household> householdOpt = model.getHouseholdBook().getHouseholdById(householdId);
        if (householdOpt.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_HOUSEHOLD_NOT_FOUND, householdId));
        }
        Household household = householdOpt.get();

        // Get the session list for that household
        List<Session> sessions = household.getSessions();
        if (sessionIndex < 1 || sessionIndex > sessions.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_SESSION_INDEX, sessionIndex, householdId));
        }

        // Retrieve the session to delete
        Session sessionToDelete = sessions.get(sessionIndex - 1);

        // Use hidden ID to remove from the entire model (household + global session list)
        model.getHouseholdBook().removeSessionById(sessionToDelete.getSessionId());

        return new CommandResult(String.format(
                MESSAGE_DELETE_SESSION_SUCCESS, sessionIndex, householdId, sessionToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeleteSessionCommand
                && householdId.equals(((DeleteSessionCommand) other).householdId)
                && sessionIndex == ((DeleteSessionCommand) other).sessionIndex);
    }
}

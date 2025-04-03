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
 * Deletes a specific session from a household.
 */
public class DeleteSessionCommand extends Command {

    public static final String COMMAND_WORD = "delete-s";
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
    /**
     * Executes the command to delete a session from the specified household.
     *
     * @param model The model from which the session will be deleted. Must not be null.
     * @return A {@code CommandResult} indicating the result of the deletion operation.
     * @throws CommandException If the household does not exist or the session index is invalid.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Optional<Household> householdOpt = model.getHouseholdBook().getHouseholdById(householdId);
        if (householdOpt.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_HOUSEHOLD_NOT_FOUND, householdId));
        }
        Household household = householdOpt.get();

        List<Session> sessions = household.getSessions();
        if (sessionIndex < 1 || sessionIndex > sessions.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_SESSION_INDEX, sessionIndex, householdId));
        }

        Session sessionToDelete = sessions.get(sessionIndex - 1);

        // Use hidden ID to remove from the entire model (household + global session list)
        model.getHouseholdBook().removeSessionById(sessionToDelete.getSessionId());

        return new CommandResult(String.format(
                MESSAGE_DELETE_SESSION_SUCCESS, sessionIndex, householdId, sessionToDelete));
    }
    /**
     * Checks if this {@code DeleteSessionCommand} is equal to another object.
     *
     * @param other The other object to compare against.
     * @return {@code true} if the other object is the same instance or an equivalent
     *         {@code DeleteSessionCommand} with the same household ID and session index,
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeleteSessionCommand
                && householdId.equals(((DeleteSessionCommand) other).householdId)
                && sessionIndex == ((DeleteSessionCommand) other).sessionIndex);
    }
}

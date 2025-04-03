package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;

/**
 * Command to display full session details in a pop-up window.
 */
public class ViewFullSessionCommand extends Command {
    public static final String COMMAND_WORD = "view-full-s";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows full session details.\n"
            + "Parameters: id/HOUSEHOLD_ID-SESSION_INDEX\n"
            + "Example: " + COMMAND_WORD + " id/H000001-2";

    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID %s.";
    public static final String MESSAGE_INVALID_SESSION_INDEX = "Invalid session index %d for household %s.";
    public static final String MESSAGE_SUCCESS = "Viewing session %s of %s in full.";

    private final HouseholdId householdId;
    private final int sessionIndex;

    /**
     * Constructs a {@code ViewFullSessionCommand} with the specified household ID and session index.
     *
     * @param householdId The ID of the household containing the session to be viewed.
     * @param sessionIndex The 1-based index of the session to display details for.
     * @throws NullPointerException if {@code householdId} is null.
     */
    public ViewFullSessionCommand(HouseholdId householdId, int sessionIndex) {
        requireNonNull(householdId);
        this.householdId = householdId;
        this.sessionIndex = sessionIndex;
    }
    /**
     * Executes the command to display the details of a session within a specified household.
     *
     * @param model The model containing the list of households. Must not be null.
     * @return A {@code CommandResult} containing the details of the specified session.
     * @throws CommandException If the household cannot be found or if the session index is invalid.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Optional<Household> householdOpt = model.getHouseholdBook().getHouseholdById(householdId);
        if (householdOpt.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_HOUSEHOLD_NOT_FOUND, householdId));
        }

        Household household = householdOpt.get();
        if (sessionIndex < 1 || sessionIndex > household.getSessions().size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_SESSION_INDEX, sessionIndex, householdId));
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, sessionIndex, householdId));
    }
}

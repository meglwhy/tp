package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;

/**
 * Views all sessions for the household identified by the given household id.
 */
public class ViewHouseholdSessionsCommand extends Command {
    public static final String COMMAND_WORD = "view-household-sessions";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views all sessions for the household identified by the household id.\n"
            + "Parameters: id/HOUSEHOLD_ID\n"
            + "Example: " + COMMAND_WORD + " id/H000007";

    public static final String MESSAGE_VIEW_SUCCESS = "Viewing sessions for household: %s";

    private final HouseholdId targetHouseholdId;

    /**
     * Constructs a new {@code ViewHouseholdSessionsCommand} for viewing the sessions
     * associated with the specified household.
     *
     * @param targetHouseholdId the unique identifier of the household whose sessions are to be viewed;
     *                          must not be null.
     * @throws NullPointerException if {@code targetHouseholdId} is null.
     */
    public ViewHouseholdSessionsCommand(HouseholdId targetHouseholdId) {
        requireNonNull(targetHouseholdId);
        this.targetHouseholdId = targetHouseholdId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Find the household with the given id.
        Household targetHousehold = model.getHouseholdBook().getHouseholdList().stream()
                .filter(h -> h.getId().equals(targetHouseholdId))
                .findFirst()
                .orElse(null);
        if (targetHousehold == null) {
            throw new CommandException("Household not found: " + targetHouseholdId.toString());
        }

        // Update the filtered session list to show only sessions for the specified household.
        Predicate<Session> predicate = session -> session.getHouseholdId().equals(targetHouseholdId);
        model.updateFilteredSessionList(predicate);

        return new CommandResult(String.format(MESSAGE_VIEW_SUCCESS, targetHouseholdId.toString()));
    }
}


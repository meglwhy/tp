package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;

public class ListSessionsCommand extends Command {

    public static final String COMMAND_WORD = "list-sessions";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " id/<HOUSEHOLD_ID>\n"
            + "Example: " + COMMAND_WORD + " id/H000003";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID %s.";
    public static final String MESSAGE_NO_SESSIONS = "No sessions to list for household %s.";
    public static final String MESSAGE_LIST_SESSIONS_SUCCESS = "Listed all sessions for household %s:%n%s";

    private final HouseholdId householdId;

    public ListSessionsCommand(HouseholdId householdId) {
        requireNonNull(householdId);
        this.householdId = householdId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Optional<Household> householdOpt = model.getHouseholdBook().getHouseholdById(householdId);
        if (householdOpt.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_HOUSEHOLD_NOT_FOUND, householdId));
        }
        Household household = householdOpt.get();

        List<Session> sessions = household.getSessions();

        // If no sessions exist for the household, return a message indicating so.
        if (sessions.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_SESSIONS, householdId));
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sessions.size(); i++) {
            Session session = sessions.get(i);
            sb.append(String.format("%d. Date: %s, Time: %s",
                    i + 1, session.getDate().toString(), session.getTime().toString()));
            if (i < sessions.size() - 1) {
                sb.append("\n");
            }
        }

        String resultMessage = String.format(MESSAGE_LIST_SESSIONS_SUCCESS, householdId, sb.toString());
        return new CommandResult(resultMessage);
    }
}



package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;

/**
 * Shows the session history of a specific household.
 */
public class ViewHistoryCommand extends Command {

    public static final String COMMAND_WORD = "view-history";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the session history of the specified household.\n"
            + "Parameters: "
            + PREFIX_ID + "HOUSEHOLD_ID\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "H000001";

    public static final String MESSAGE_SUCCESS = "Session history for %1$s:\n%2$s";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID: %1$s";
    public static final String MESSAGE_NO_SESSIONS = "No session history found for %1$s";

    private final HouseholdId targetId;

    public ViewHistoryCommand(HouseholdId targetId) {
        requireNonNull(targetId);
        this.targetId = targetId;
    }

    @Override
    public CommandResult execute(HouseholdBook householdBook) throws CommandException {
        requireNonNull(householdBook);

        Optional<Household> optionalHousehold = householdBook.getHouseholdById(targetId);
        if (optionalHousehold.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_HOUSEHOLD_NOT_FOUND, targetId));
        }

        Household household = optionalHousehold.get();
        List<Session> sessions = household.getSessions();

        if (sessions.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_SESSIONS, household.getName()));
        }

        // Sort sessions by date and time
        sessions.sort((s1, s2) -> {
            int dateComparison = s1.getDate().compareTo(s2.getDate());
            if (dateComparison != 0) {
                return dateComparison;
            }
            return s1.getTime().compareTo(s2.getTime());
        });

        StringBuilder history = new StringBuilder();
        for (Session session : sessions) {
            history.append(formatSessionEntry(session)).append("\n");
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, 
                household.getName(), history.toString()));
    }

    /**
     * Formats a session entry for display.
     */
    private String formatSessionEntry(Session session) {
        StringBuilder entry = new StringBuilder();
        entry.append("Date: ").append(session.getDate())
             .append(", Time: ").append(session.getTime());
        
        if (session.hasNote()) {
            entry.append("\n  Note: ").append(session.getNote());
        }
        
        return entry.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewHistoryCommand // instanceof handles nulls
                && targetId.equals(((ViewHistoryCommand) other).targetId)); // state check
    }
} 
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;

public class EditSessionCommand extends Command {

    public static final String COMMAND_WORD = "edit-session";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the session identified by the household ID and session index by updating date and time.\n"
            + "Parameters: <HOUSEHOLD_ID-SESSION_INDEX> d/DATE t/TIME\n"
            + "Example: " + COMMAND_WORD + " H000006-2 d/2025-03-16 t/15:00";
    public static final String MESSAGE_EDIT_SESSION_SUCCESS = "Edited session:%nDate: %s%nTime: %s";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID %s.";
    public static final String MESSAGE_INVALID_SESSION_INDEX = "Session index %d is invalid for household %s.";

    private final HouseholdId householdId;
    private final int sessionIndex; // 1-based
    private final String newDate;
    private final String newTime;

    public EditSessionCommand(HouseholdId householdId, int sessionIndex, String newDate, String newTime) {
        requireNonNull(householdId);
        requireNonNull(newDate);
        requireNonNull(newTime);
        this.householdId = householdId;
        this.sessionIndex = sessionIndex;
        this.newDate = newDate;
        this.newTime = newTime;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        // Find household using householdId.
        Optional<Household> householdOpt = model.getHouseholdBook().getHouseholdById(householdId);
        if (householdOpt.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_HOUSEHOLD_NOT_FOUND, householdId));
        }
        Household household = householdOpt.get();

        // Retrieve sessions list for that household.
        List<Session> sessions = household.getSessions();
        if (sessionIndex < 1 || sessionIndex > sessions.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_SESSION_INDEX, sessionIndex, householdId));
        }
        Session oldSession = sessions.get(sessionIndex - 1);

        // Delete the old session.
        model.getHouseholdBook().removeSessionById(oldSession.getSessionId());

        // Create a new session with new date and time.
        // If there is an existing note, preserve it.
        Session newSession;
        if (oldSession.hasNote()) {
            newSession = new Session(oldSession.getHouseholdId(),
                    new SessionDate(newDate),
                    new SessionTime(newTime),
                    oldSession.getNote());
        } else {
            newSession = new Session(oldSession.getHouseholdId(),
                    new SessionDate(newDate),
                    new SessionTime(newTime));
        }

        // Add the new session back to the household.
        model.getHouseholdBook().addSessionToHousehold(householdId, newSession);

        return new CommandResult(String.format(MESSAGE_EDIT_SESSION_SUCCESS, newDate, newTime));
    }
}




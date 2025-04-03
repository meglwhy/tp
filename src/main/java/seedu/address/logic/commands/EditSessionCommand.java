package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionNote;
import seedu.address.model.session.SessionTime;


/**
 * Edits a specific session in a household.
 */
public class EditSessionCommand extends Command {

    public static final String COMMAND_WORD = "edit-s";
    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Edits the session identified by the household ID and session index "
                    + "by updating any of the following: date, time, and/or note.\n"
                    + "Parameters: <HOUSEHOLD_ID-SESSION_INDEX> [d/DATE] [tm/TIME] [n/NOTE]\n"
                    + "Example: " + COMMAND_WORD + " H000006-2 d/2025-03-16 tm/15:00\n"
                    + "Example with note: " + COMMAND_WORD + " H000006-2 d/2025-03-16 tm/15:00 "
                    + "n/Follow-up on medical assistance application";

    public static final String MESSAGE_EDIT_SESSION_SUCCESS = "Edited session:\nDate: %s\nTime: %s";
    public static final String MESSAGE_EDIT_SESSION_WITH_NOTE_SUCCESS = "Edited session:\nDate: %s\nTime: %s\nNote: %s";
    public static final String MESSAGE_DUPLICATE_SESSION =
            "This time slot is already booked.\nExisting session: %1$s";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID %s.";
    public static final String MESSAGE_INVALID_SESSION_INDEX = "Session index %d is invalid for household %s.";

    private final HouseholdId householdId;
    private final int sessionIndex; // 1-based index
    // Optional fields-retains existing values if null
    private final String newDate;
    private final String newTime;
    private final String newNote;

    /**
     * Constructs an EditSessionCommand.
     *
     * @param householdId  The unique identifier of the household.
     * @param sessionIndex The 1-based index of the session to be edited.
     * @param newDate      The new date to set, or null to leave unchanged.
     * @param newTime      The new time to set, or null to leave unchanged.
     * @param newNote      The new note to set, or null to leave unchanged.
     */
    public EditSessionCommand(HouseholdId householdId, int sessionIndex,
                              String newDate, String newTime, String newNote) {
        requireNonNull(householdId);
        this.householdId = householdId;
        this.sessionIndex = sessionIndex;
        this.newDate = newDate;
        this.newTime = newTime;
        this.newNote = newNote;
    }
    /**
     * Executes the command to edit a session in the model.
     *
     * @param model The model containing the session to be edited. Must not be null.
     * @return A {@code CommandResult} indicating the result of the edit operation.
     * @throws CommandException If the session index is invalid, a conflicting session is detected,
     *     or the session cannot be edited.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ObservableList<Session> householdSessions = model.getFilteredSessionList()
                .filtered(s -> s.getHouseholdId().equals(householdId));

        // Sort sessions by date (descending) then time (descending), same as in SessionListPanel.
        Session oldSession = getSession(householdSessions);

        // Determine candidate values: use new values if provided, otherwise retain the current ones.
        SessionDate candidateDate = (newDate != null) ? new SessionDate(newDate) : oldSession.getDate();
        SessionTime candidateTime = (newTime != null) ? new SessionTime(newTime) : oldSession.getTime();
        boolean noteUpdated = (newNote != null);

        Session candidateSession;
        if (noteUpdated) {
            candidateSession = new Session(oldSession.getSessionId(),
                    oldSession.getHouseholdId(),
                    candidateDate,
                    candidateTime,
                    new SessionNote(newNote));
        } else if (oldSession.hasNote()) {
            candidateSession = new Session(oldSession.getSessionId(),
                    oldSession.getHouseholdId(),
                    candidateDate,
                    candidateTime,
                    oldSession.getNote());
        } else {
            candidateSession = new Session(oldSession.getSessionId(),
                    oldSession.getHouseholdId(),
                    candidateDate,
                    candidateTime);
        }

        Optional<Session> conflict = model.getHouseholdBook().getConflictingSession(candidateSession, oldSession);
        if (conflict.isPresent()) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_SESSION, conflict.get()));
        }

        model.getHouseholdBook().removeSessionById(oldSession.getSessionId());
        model.getHouseholdBook().addSessionToHousehold(oldSession.getHouseholdId(), candidateSession);

        String successMessage;
        if (noteUpdated) {
            successMessage = String.format(MESSAGE_EDIT_SESSION_WITH_NOTE_SUCCESS,
                    candidateDate, candidateTime, newNote);
        } else {
            successMessage = String.format(MESSAGE_EDIT_SESSION_SUCCESS,
                    candidateDate, candidateTime);
        }
        return new CommandResult(successMessage);
    }

    private Session getSession(ObservableList<Session> householdSessions) throws CommandException {
        SortedList<Session> sortedSessions = new SortedList<>(householdSessions, (s1, s2) -> {
            int dateCompare = s2.getDate().compareTo(s1.getDate());
            if (dateCompare != 0) {
                return dateCompare;
            }
            return s2.getTime().compareTo(s1.getTime());
        });

        if (sessionIndex < 1 || sessionIndex > sortedSessions.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_SESSION_INDEX, sessionIndex, householdId));
        }
        return sortedSessions.get(sessionIndex - 1);
    }

}





package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

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
 * Represents a command to edit a specific session in a household.
 * The session is identified using a household ID and a session index.
 * Each field (date, time, note) is optional; if not provided, the existing value is retained.
 *
 * Usage examples:
 * <pre>
 *     edit-s H000006-2 d/2025-03-16         // Only update date
 *     edit-s H000006-2 tm/15:00             // Only update time
 *     edit-s H000006-2 n/Follow-up note     // Only update note
 *     edit-s H000006-2 d/2025-03-16 tm/15:00 n/Follow-up note
 * </pre>
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
    // The following fields are optional; if null, the existing value is retained.
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
        this.newDate = newDate; // may be null
        this.newTime = newTime; // may be null
        this.newNote = newNote; // may be null
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Filter sessions for the given household.
        ObservableList<Session> householdSessions = model.getFilteredSessionList()
                .filtered(s -> s.getHouseholdId().equals(householdId));

        // Sort sessions by date (descending) then time (descending), same as SessionListPanel.
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
        Session oldSession = sortedSessions.get(sessionIndex - 1);

        // Determine candidate values: use new values if provided, otherwise use the current session's values.
        SessionDate candidateDate = (newDate != null) ? new SessionDate(newDate) : oldSession.getDate();
        SessionTime candidateTime = (newTime != null) ? new SessionTime(newTime) : oldSession.getTime();

        // For note, update only if a new note is provided.
        boolean noteUpdated = (newNote != null);

        // Create the candidate session with the updated fields.
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

        // Create a copy of the session list and remove the session being edited to avoid self-conflict.
        List<Session> otherSessions = new ArrayList<>(householdSessions);
        otherSessions.removeIf(s -> s.getSessionId().equals(oldSession.getSessionId()));

        // Check for any conflict: another session with the same date and time.
        for (Session s : otherSessions) {
            if (s.getDate().equals(candidateSession.getDate())
                    && s.getTime().equals(candidateSession.getTime())) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_SESSION, s));
            }
        }

        // No conflict: update the session.
        model.getHouseholdBook().removeSessionById(oldSession.getSessionId());
        model.getHouseholdBook().addSessionToHousehold(oldSession.getHouseholdId(), candidateSession);

        // Build the success message based on the fields updated.
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

}





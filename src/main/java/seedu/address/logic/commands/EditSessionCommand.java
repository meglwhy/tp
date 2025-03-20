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
import seedu.address.model.session.SessionNote;
import seedu.address.model.session.SessionTime;

/**
 * Represents a command to edit a specific session in a household.
 * The session is identified using a household ID and a session index.
 * The date, time, and optionally the note of the session can be updated.
 *
 * Usage example:
 * <pre>
 *     edit-session H000006-2 d/2025-03-16 t/15:00
 *     edit-session H000006-2 d/2025-03-16 t/15:00 n/Follow-up on medical assistance application
 * </pre>
 *
 * This command performs the following checks:
 * - Ensures the household with the given ID exists.
 * - Ensures the session index is valid for the specified household.
 * - Removes the old session and replaces it with a new one containing updated information.
 * - If a note is provided, it either replaces or appends to any existing note.
 *
 * Upon successful execution, it returns a message confirming the session update.
 */
public class EditSessionCommand extends Command {

    public static final String COMMAND_WORD = "edit-session";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the session identified by the household ID and session index by updating date, time,"
            + "and optionally a note.\n"
            + "Parameters: <HOUSEHOLD_ID-SESSION_INDEX> d/DATE t/TIME [n/NOTE]\n"
            + "Example: " + COMMAND_WORD + " H000006-2 d/2025-03-16 t/15:00\n"
            + "Example with note: " + COMMAND_WORD + " H000006-2 d/2025-03-16 t/15:00 n/Follow-up on"
            + "medical assistance application";
    public static final String MESSAGE_EDIT_SESSION_SUCCESS = "Edited session:%nDate: %s%nTime: %s";
    public static final String MESSAGE_EDIT_SESSION_WITH_NOTE_SUCCESS = "Edited session:%nDate: %s%nTime: %s%nNote: %s";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID %s.";
    public static final String MESSAGE_INVALID_SESSION_INDEX = "Session index %d is invalid for household %s.";

    private final HouseholdId householdId;
    private final int sessionIndex; // 1-based
    private final String newDate;
    private final String newTime;
    private final String note;
    private final boolean hasNote;

    /**
     * Creates an {@code EditSessionCommand} to update the date and time of a session in a household.
     *
     * @param householdId  The unique identifier of the household.
     * @param sessionIndex The 1-based index of the session to be edited.
     * @param newDate      The new date for the session.
     * @param newTime      The new time for the session.
     * @throws NullPointerException if {@code householdId}, {@code newDate}, or {@code newTime} is null.
     */
    public EditSessionCommand(HouseholdId householdId, int sessionIndex, String newDate, String newTime) {
        requireNonNull(householdId);
        requireNonNull(newDate);
        requireNonNull(newTime);
        this.householdId = householdId;
        this.sessionIndex = sessionIndex;
        this.newDate = newDate;
        this.newTime = newTime;
        this.note = null;
        this.hasNote = false;
    }
    /**
     * Creates an {@code EditSessionCommand} to update the date, time, and note of a session in a household.
     *
     * @param householdId  The unique identifier of the household.
     * @param sessionIndex The 1-based index of the session to be edited.
     * @param newDate      The new date for the session.
     * @param newTime      The new time for the session.
     * @param note         The note to be added or updated for the session.
     * @throws NullPointerException if any parameter is null.
     */
    public EditSessionCommand(HouseholdId householdId, int sessionIndex, String newDate, String newTime, String note) {
        requireNonNull(householdId);
        requireNonNull(newDate);
        requireNonNull(newTime);
        requireNonNull(note);
        this.householdId = householdId;
        this.sessionIndex = sessionIndex;
        this.newDate = newDate;
        this.newTime = newTime;
        this.note = note;
        this.hasNote = true;
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
        Session newSession;
        if (hasNote) {
            // If we're explicitly setting a new note
            newSession = new Session(oldSession.getHouseholdId(),
                    new SessionDate(newDate),
                    new SessionTime(newTime),
                    new SessionNote(note));
        } else if (oldSession.hasNote()) {
            // If no new note but there's an existing note, preserve it
            newSession = new Session(oldSession.getHouseholdId(),
                    new SessionDate(newDate),
                    new SessionTime(newTime),
                    oldSession.getNote());
        } else {
            // No new note and no existing note
            newSession = new Session(oldSession.getHouseholdId(),
                    new SessionDate(newDate),
                    new SessionTime(newTime));
        }

        // Add the new session back to the household.
        model.getHouseholdBook().addSessionToHousehold(householdId, newSession);

        // Return appropriate success message based on whether a note was included
        if (hasNote) {
            return new CommandResult(String.format(MESSAGE_EDIT_SESSION_WITH_NOTE_SUCCESS,
                newDate, newTime, note));
        } else {
            return new CommandResult(String.format(MESSAGE_EDIT_SESSION_SUCCESS, newDate, newTime));
        }
    }
}




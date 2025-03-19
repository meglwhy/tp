package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionNote;

public class AddNoteCommand extends Command {

    public static final String COMMAND_WORD = "add-note";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a note to the specified session.\n"
            + "Parameters: HOUSEHOLD_ID-SESSION_INDEX n/NOTE\n"
            + "Example: " + COMMAND_WORD + " H000006-2 n/Follow-up on medical assistance application";
    public static final String MESSAGE_ADD_NOTE_SUCCESS = "Added note to session:%nDate: %s%nTime: %s%nNote: %s";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID %s.";
    public static final String MESSAGE_INVALID_SESSION_INDEX = "Session index %d is invalid for household %s.";

    private final HouseholdId householdId;
    private final int sessionIndex; // 1-based
    private final String note;

    public AddNoteCommand(HouseholdId householdId, int sessionIndex, String note) {
        requireNonNull(householdId);
        requireNonNull(note);
        this.householdId = householdId;
        this.sessionIndex = sessionIndex;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Find the household by its ID.
        Optional<Household> householdOpt = model.getHouseholdBook().getHouseholdById(householdId);
        if (householdOpt.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_HOUSEHOLD_NOT_FOUND, householdId));
        }
        Household household = householdOpt.get();

        // Retrieve the sessions list for that household.
        List<Session> sessions = household.getSessions();
        if (sessionIndex < 1 || sessionIndex > sessions.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_SESSION_INDEX, sessionIndex, householdId));
        }
        Session targetSession = sessions.get(sessionIndex - 1);

        // Append the new note if one exists, else set the note.
        String combinedNote;
        if (targetSession.hasNote()) {
            combinedNote = targetSession.getNote().toString() + " " + note;
        } else {
            combinedNote = note;
        }
        targetSession.setNote(new SessionNote(combinedNote));

        // Prepare the success message.
        String resultMessage = String.format(MESSAGE_ADD_NOTE_SUCCESS,
                targetSession.getDate().toString(),
                targetSession.getTime().toString(),
                targetSession.getNote().toString());
        return new CommandResult(resultMessage);
    }
}


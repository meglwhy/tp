package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.*;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;
import seedu.address.model.session.SessionNote;
import seedu.address.model.Model;

/**
 * Edits the details of an existing session.
 */
public class EditSessionCommand extends Command {

    public static final String COMMAND_WORD = "edit-session";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the session identified "
            + "by the index number. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TIME + "TIME] "
            + "[" + PREFIX_NOTE + "NOTE]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2025-03-16 "
            + PREFIX_TIME + "15:00";

    public static final String MESSAGE_EDIT_SESSION_SUCCESS = "Edited Session: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_SESSION = 
            "This time slot is already booked.\n"
            + "Existing session: %1$s";
    public static final String MESSAGE_SESSION_NOT_FOUND = "No session found with this index.";

    private final Index index;
    private final EditSessionDescriptor editSessionDescriptor;

    /**
     * @param index of the session in the filtered session list to edit
     * @param editSessionDescriptor details to edit the session with
     */
    public EditSessionCommand(Index index, EditSessionDescriptor editSessionDescriptor) {
        requireAllNonNull(index, editSessionDescriptor);

        this.index = index;
        this.editSessionDescriptor = new EditSessionDescriptor(editSessionDescriptor);
    }

    @Override
    public CommandResult execute(Model householdBook) throws CommandException {
        requireNonNull(householdBook);

        Session sessionToEdit = householdBook.getHouseholdBook().getSession(index)
                .orElseThrow(() -> new CommandException(MESSAGE_SESSION_NOT_FOUND));
        Session editedSession = createEditedSession(sessionToEdit, editSessionDescriptor);

        if (!sessionToEdit.equals(editedSession)) {
            Optional<Session> conflict = householdBook.getHouseholdBook().getConflictingSession(editedSession, sessionToEdit);
            if (conflict.isPresent()) {
                throw new CommandException(
                        String.format(MESSAGE_DUPLICATE_SESSION, conflict.get()));
            }
        }

        householdBook.getHouseholdBook().updateSession(sessionToEdit, editedSession);
        return new CommandResult(String.format(MESSAGE_EDIT_SESSION_SUCCESS, editedSession));
    }

    /**
     * Creates and returns a {@code Session} with the details of {@code sessionToEdit}
     * edited with {@code editSessionDescriptor}.
     */
    private static Session createEditedSession(Session sessionToEdit, EditSessionDescriptor editSessionDescriptor) {
        assert sessionToEdit != null;

        SessionDate updatedDate = editSessionDescriptor.getDate().orElse(sessionToEdit.getDate());
        SessionTime updatedTime = editSessionDescriptor.getTime().orElse(sessionToEdit.getTime());
        SessionNote updatedNote = editSessionDescriptor.getNote().orElse(sessionToEdit.getNote());

        Session newSession = new Session(sessionToEdit.getHouseholdId(), updatedDate, updatedTime);
        if (updatedNote != null) {
            newSession.setNote(updatedNote);
        }
        return newSession;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EditSessionCommand // instanceof handles nulls
                && index.equals(((EditSessionCommand) other).index)
                && editSessionDescriptor.equals(((EditSessionCommand) other).editSessionDescriptor));
    }

    /**
     * Stores the details to edit the session with. Each non-empty field value will replace the
     * corresponding field value of the session.
     */
    public static class EditSessionDescriptor {
        private SessionDate date;
        private SessionTime time;
        private SessionNote note;

        public EditSessionDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditSessionDescriptor(EditSessionDescriptor toCopy) {
            setDate(toCopy.date);
            setTime(toCopy.time);
            setNote(toCopy.note);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return date != null || time != null || note != null;
        }

        public Optional<SessionDate> getDate() {
            return Optional.ofNullable(date);
        }

        public void setDate(SessionDate date) {
            this.date = date;
        }

        public Optional<SessionTime> getTime() {
            return Optional.ofNullable(time);
        }

        public void setTime(SessionTime time) {
            this.time = time;
        }

        public Optional<SessionNote> getNote() {
            return Optional.ofNullable(note);
        }

        public void setNote(SessionNote note) {
            this.note = note;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditSessionDescriptor)) {
                return false;
            }

            EditSessionDescriptor e = (EditSessionDescriptor) other;

            return getDate().equals(e.getDate())
                    && getTime().equals(e.getTime())
                    && getNote().equals(e.getNote());
        }
    }
} 
package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javafx.collections.FXCollections;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;

@ExtendWith(MockitoExtension.class)
class EditSessionCommandTest {

    private Model model;
    private HouseholdBook householdBook;
    private Household household;
    private HouseholdId householdId;
    private Session oldSession;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);
        household = mock(Household.class);
        householdId = new HouseholdId("H000001");
        // Create a sample session with a future date ("2030-01-01") and time ("12:00").
        oldSession = new Session("S000001", householdId,
                new SessionDate("2030-01-01"), new SessionTime("12:00"));

        // Stub the household to return a list with our sample session.
        when(household.getSessions()).thenReturn(
                FXCollections.observableArrayList(oldSession));
        // Stub model to return our mocked householdBook.
        lenient().when(model.getHouseholdBook()).thenReturn(householdBook);
        // Stub householdBook to return our household when searched by householdId.
        when(householdBook.getHouseholdById(eq(householdId)))
                .thenReturn(Optional.of(household));
        // By default, no conflicting session exists.
        lenient().when(householdBook.getConflictingSession(any(Session.class), eq(oldSession)))
                .thenReturn(Optional.empty());
    }

    @Test
    void executeValidEditNoNoteSuccess() throws CommandException {
        // Edit the session by changing its date and time; no note update.
        String newDate = "2030-02-02";
        String newTime = "14:00";
        String newNote = null;
        EditSessionCommand command = new EditSessionCommand(
                householdId, 1, newDate, newTime, newNote);

        CommandResult result = command.execute(model);

        SessionDate candidateDate = new SessionDate(newDate);
        SessionTime candidateTime = new SessionTime(newTime);
        String expectedMessage = String.format(
                EditSessionCommand.MESSAGE_EDIT_SESSION_SUCCESS,
                candidateDate, candidateTime);

        // Verify that the command removed the old session and added the updated session.
        verify(householdBook).removeSessionById(oldSession.getSessionId());
        verify(householdBook).addSessionToHousehold(eq(householdId), any(Session.class));

        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    void executeValidEditWithNoteSuccess() throws CommandException {
        // Edit the session by changing its date, time, and updating the note.
        String newDate = "2030-02-02";
        String newTime = "14:00";
        String newNote = "Follow-up on application";
        EditSessionCommand command = new EditSessionCommand(
                householdId, 1, newDate, newTime, newNote);

        CommandResult result = command.execute(model);

        SessionDate candidateDate = new SessionDate(newDate);
        SessionTime candidateTime = new SessionTime(newTime);
        String expectedMessage = String.format(
                EditSessionCommand.MESSAGE_EDIT_SESSION_WITH_NOTE_SUCCESS,
                candidateDate, candidateTime, newNote);
        // If the line exceeds 120 characters, you can break it as follows:
        /*
        String expectedMessage = String.format(
            EditSessionCommand.MESSAGE_EDIT_SESSION_WITH_NOTE_SUCCESS,
            candidateDate, candidateTime, newNote);
        */

        verify(householdBook).removeSessionById(oldSession.getSessionId());
        verify(householdBook).addSessionToHousehold(eq(householdId), any(Session.class));

        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    void executeEditPastDateTimeThrowsException() {
        // Attempt to edit the session to a past date/time (e.g. "2000-01-01" should be in the past).
        String pastDate = "2000-01-01";
        String newTime = "12:00";
        EditSessionCommand command = new EditSessionCommand(
                householdId, 1, pastDate, newTime, null);

        CommandException exception =
                assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals("Cannot edit session to a past date or time.", exception.getMessage());
    }

    @Test
    void executeEditConflictingSessionThrowsException() {
        // Simulate a conflicting session by stubbing a conflict.
        Session conflictSession = new Session("S000002", householdId,
                new SessionDate("2030-02-02"), new SessionTime("14:00"));
        when(householdBook.getConflictingSession(any(Session.class), eq(oldSession)))
                .thenReturn(Optional.of(conflictSession));

        String newDate = "2030-02-02";
        String newTime = "14:00";
        EditSessionCommand command = new EditSessionCommand(
                householdId, 1, newDate, newTime, null);

        CommandException exception =
                assertThrows(CommandException.class, () -> command.execute(model));
        String expectedMessage = String.format(
                EditSessionCommand.MESSAGE_DUPLICATE_SESSION, conflictSession);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void executeInvalidSessionIndexThrowsException() {
        // Using an invalid session index (e.g., index 2 when only one session exists) should throw an exception.
        EditSessionCommand command = new EditSessionCommand(
                householdId, 2, "2030-02-02", "14:00", null);
        assertThrows(IndexOutOfBoundsException.class, () -> command.execute(model));
    }
}



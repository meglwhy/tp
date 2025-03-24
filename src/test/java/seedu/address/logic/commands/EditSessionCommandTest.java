package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionNote;

class EditSessionCommandTest {

    private Model model;
    private HouseholdBook householdBook;
    private HouseholdId householdId;
    private Household household;
    private Session oldSession;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);
        when(model.getHouseholdBook()).thenReturn(householdBook);

        householdId = new HouseholdId("H000006");

        // Create a dummy session using a mock.
        oldSession = mock(Session.class);
        when(oldSession.getSessionId()).thenReturn("S0002");
        // *** Fix: Stub getHouseholdId() so it's not null ***
        when(oldSession.getHouseholdId()).thenReturn(householdId);
        when(oldSession.hasNote()).thenReturn(false);

        ObservableList<Session> sessions = FXCollections.observableArrayList(oldSession);
        household = mock(Household.class);
        when(household.getSessions()).thenReturn(sessions);

        when(householdBook.getHouseholdById(householdId)).thenReturn(Optional.of(household));
    }

    @Test
    void execute_editWithoutNote_updatesSession() throws CommandException {
        String newDate = "2025-03-16";
        String newTime = "15:00";

        EditSessionCommand command = new EditSessionCommand(householdId, 1, newDate, newTime);
        CommandResult result = command.execute(model);

        verify(householdBook).removeSessionById("S0002");
        verify(householdBook).addSessionToHousehold(eq(householdId), any(Session.class));
        assertTrue(result.getFeedbackToUser().contains(newDate));
        assertTrue(result.getFeedbackToUser().contains(newTime));
        // Verify the message does not include note information.
        assertFalse(result.getFeedbackToUser().contains("Note:"));
    }

    @Test
    void execute_editWithNote_updatesSessionWithNote() throws CommandException {
        // Simulate that the old session already has a note.
        when(oldSession.hasNote()).thenReturn(true);
        when(oldSession.getNote()).thenReturn(new SessionNote("Old note"));

        String newDate = "2025-03-16";
        String newTime = "15:00";
        String newNote = "Follow-up on application";

        EditSessionCommand command = new EditSessionCommand(householdId, 1, newDate, newTime, newNote);
        CommandResult result = command.execute(model);

        verify(householdBook).removeSessionById("S0002");
        verify(householdBook).addSessionToHousehold(eq(householdId), any(Session.class));
        assertTrue(result.getFeedbackToUser().contains(newDate));
        assertTrue(result.getFeedbackToUser().contains(newTime));
        assertTrue(result.getFeedbackToUser().contains(newNote));
    }

    @Test
    void execute_householdNotFound_throwsCommandException() {
        when(householdBook.getHouseholdById(householdId)).thenReturn(Optional.empty());

        EditSessionCommand command = new EditSessionCommand(householdId, 1, "2025-03-16", "15:00");
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(EditSessionCommand.MESSAGE_HOUSEHOLD_NOT_FOUND, householdId),
                exception.getMessage());
    }

    @Test
    void execute_invalidSessionIndex_throwsCommandException() {
        int invalidIndex = 2; // Only one session exists.
        EditSessionCommand command = new EditSessionCommand(householdId, invalidIndex, "2025-03-16", "15:00");
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(EditSessionCommand.MESSAGE_INVALID_SESSION_INDEX, invalidIndex, householdId),
                exception.getMessage());
    }
}


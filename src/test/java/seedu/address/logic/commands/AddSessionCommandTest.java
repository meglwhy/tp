package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;

class AddSessionCommandTest {

    private Model model;
    private HouseholdBook householdBook;
    private HouseholdId householdId;
    private SessionDate futureDate;
    private SessionTime sessionTime;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);
        when(model.getHouseholdBook()).thenReturn(householdBook);

        householdId = new HouseholdId("H000001");
        // Use a future date (today plus 1 day)
        futureDate = new SessionDate(LocalDate.now().plusDays(1).toString());
        sessionTime = new SessionTime("14:30");
    }

    @Test
    void execute_validSession_addsSessionToHousehold() throws CommandException {
        when(householdBook.hasHouseholdId(householdId)).thenReturn(true);
        when(householdBook.getConflictingSession(any(Session.class))).thenReturn(Optional.empty());

        AddSessionCommand command = new AddSessionCommand(householdId, futureDate, sessionTime);
        CommandResult result = command.execute(model);

        verify(householdBook).addSessionToHousehold(eq(householdId), any(Session.class));
        assertTrue(result.getFeedbackToUser().contains("New session added to household"));
    }

    @Test
    void execute_householdNotFound_throwsCommandException() {
        when(householdBook.hasHouseholdId(householdId)).thenReturn(false);

        AddSessionCommand command = new AddSessionCommand(householdId, futureDate, sessionTime);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(AddSessionCommand.MESSAGE_HOUSEHOLD_NOT_FOUND, exception.getMessage());
    }

    @Test
    void execute_sessionDateInPast_throwsCommandException() {
        SessionDate pastDate = new SessionDate(LocalDate.now().minusDays(1).toString());
        when(householdBook.hasHouseholdId(householdId)).thenReturn(true);

        AddSessionCommand command = new AddSessionCommand(householdId, pastDate, sessionTime);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(AddSessionCommand.MESSAGE_PAST_DATE, exception.getMessage());
    }

    @Test
    void execute_duplicateSession_throwsCommandException() {
        when(householdBook.hasHouseholdId(householdId)).thenReturn(true);
        Session conflictSession = new Session(householdId, futureDate, sessionTime);
        when(householdBook.getConflictingSession(any(Session.class))).thenReturn(Optional.of(conflictSession));

        AddSessionCommand command = new AddSessionCommand(householdId, futureDate, sessionTime);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertTrue(exception.getMessage().contains("This time slot is already booked"));
    }

    @Test
    void equals_sameObject_returnsTrue() {
        AddSessionCommand command = new AddSessionCommand(householdId, futureDate, sessionTime);
        assertEquals(command, command);
    }

    @Test
    void equals_differentHouseholdId_returnsFalse() {
        HouseholdId differentHouseholdId = new HouseholdId("H000002");
        AddSessionCommand command1 = new AddSessionCommand(householdId, futureDate, sessionTime);
        AddSessionCommand command2 = new AddSessionCommand(differentHouseholdId, futureDate, sessionTime);
        assertNotEquals(command1, command2);
    }

    @Test
    void equals_differentSessionParameters_returnsFalse() {
        SessionDate differentDate = new SessionDate("2025-06-01");
        SessionTime differentTime = new SessionTime("15:00");
        AddSessionCommand command1 = new AddSessionCommand(householdId, futureDate, sessionTime);
        AddSessionCommand command2 = new AddSessionCommand(householdId, differentDate, differentTime);
        assertNotEquals(command1, command2);
    }

    @Test
    void equals_null_returnsFalse() {
        AddSessionCommand command = new AddSessionCommand(householdId, futureDate, sessionTime);
        assertNotEquals(command, null);
    }

    @Test
    void equals_differentType_returnsFalse() {
        AddSessionCommand command = new AddSessionCommand(householdId, futureDate, sessionTime);
        assertNotEquals(command, "some string");
    }
}


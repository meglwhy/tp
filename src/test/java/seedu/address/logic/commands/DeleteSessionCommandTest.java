package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

class DeleteSessionCommandTest {

    private Model model;
    private HouseholdBook householdBook;
    private HouseholdId householdId;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);
        when(model.getHouseholdBook()).thenReturn(householdBook);

        householdId = new HouseholdId("H000002");

        Session session = mock(Session.class);
        when(session.getSessionId()).thenReturn("S0001");

        ObservableList<Session> sessions = FXCollections.observableArrayList(session);

        Household household = mock(Household.class);
        when(household.getSessions()).thenReturn(sessions);

        when(householdBook.getHouseholdById(householdId)).thenReturn(Optional.of(household));
    }

    @Test
    void execute_validDeletion_deletesSession() throws CommandException {
        int sessionIndex = 1;
        DeleteSessionCommand command = new DeleteSessionCommand(householdId, sessionIndex);
        CommandResult result = command.execute(model);

        verify(householdBook).removeSessionById("S0001");
        assertTrue(result.getFeedbackToUser().contains("Deleted session"));
    }

    @Test
    void execute_householdNotFound_throwsCommandException() {
        when(householdBook.getHouseholdById(householdId)).thenReturn(Optional.empty());

        DeleteSessionCommand command = new DeleteSessionCommand(householdId, 1);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(DeleteSessionCommand.MESSAGE_HOUSEHOLD_NOT_FOUND, householdId),
                exception.getMessage());
    }

    @Test
    void execute_invalidSessionIndex_throwsCommandException() {
        // Only one session exists; index 2 is invalid.
        int invalidIndex = 2;
        DeleteSessionCommand command = new DeleteSessionCommand(householdId, invalidIndex);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(DeleteSessionCommand.MESSAGE_INVALID_SESSION_INDEX, invalidIndex, householdId),
                exception.getMessage());
    }

    @Test
    void equals_sameValues_returnsTrue() {
        DeleteSessionCommand command1 = new DeleteSessionCommand(householdId, 1);
        DeleteSessionCommand command2 = new DeleteSessionCommand(householdId, 1);
        assertEquals(command1, command2);
    }

    @Test
    void equals_sameObject_returnsTrue() {
        DeleteSessionCommand command = new DeleteSessionCommand(householdId, 1);
        assertEquals(command, command);
    }

    @Test
    void equals_differentHouseholdId_returnsFalse() {
        HouseholdId differentHouseholdId = new HouseholdId("H000003");
        DeleteSessionCommand command1 = new DeleteSessionCommand(householdId, 1);
        DeleteSessionCommand command2 = new DeleteSessionCommand(differentHouseholdId, 1);
        assertNotEquals(command1, command2);
    }

    @Test
    void equals_differentSessionIndex_returnsFalse() {
        DeleteSessionCommand command1 = new DeleteSessionCommand(householdId, 1);
        DeleteSessionCommand command2 = new DeleteSessionCommand(householdId, 2);
        assertNotEquals(command1, command2);
    }

    @Test
    void equals_null_returnsFalse() {
        DeleteSessionCommand command = new DeleteSessionCommand(householdId, 1);
        assertNotEquals(command, null);
    }

    @Test
    void equals_differentType_returnsFalse() {
        DeleteSessionCommand command = new DeleteSessionCommand(householdId, 1);
        assertNotEquals(command, "some string");
    }
}

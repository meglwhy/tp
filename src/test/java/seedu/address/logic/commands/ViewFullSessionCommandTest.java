package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;

@ExtendWith(MockitoExtension.class)
class ViewFullSessionCommandTest {

    private Model model;
    private HouseholdBook householdBook;
    private Household household;
    private HouseholdId householdId;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);
        household = mock(Household.class);
        householdId = new HouseholdId("H000001");

        // Lenient stubbing for common dependencies.
        lenient().when(model.getHouseholdBook()).thenReturn(householdBook);
    }

    @Test
    void execute_householdNotFound_throwsCommandException() {
        when(householdBook.getHouseholdById(eq(householdId))).thenReturn(Optional.empty());

        ViewFullSessionCommand command = new ViewFullSessionCommand(householdId, 1);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));

        String expectedMessage = String.format(ViewFullSessionCommand.MESSAGE_HOUSEHOLD_NOT_FOUND, householdId);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void execute_invalidSessionIndexLow_throwsCommandException() {
        // Return a household so that the code proceeds to check the session index.
        when(householdBook.getHouseholdById(eq(householdId))).thenReturn(Optional.of(household));
        // No stubbing for household.getSessions() is necessary here since sessionIndex is less than 1.
        ViewFullSessionCommand command = new ViewFullSessionCommand(householdId, 0);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));

        String expectedMessage = String.format(ViewFullSessionCommand.MESSAGE_INVALID_SESSION_INDEX, 0, householdId);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void execute_invalidSessionIndexHigh_throwsCommandException() {
        when(householdBook.getHouseholdById(eq(householdId))).thenReturn(Optional.of(household));
        // Create a household with one session.
        Session dummySession = mock(Session.class);
        ObservableList<Session> sessions = FXCollections.observableArrayList(dummySession);
        when(household.getSessions()).thenReturn(sessions);

        // A session index greater than the session count (i.e. 2 when size is 1) is invalid.
        ViewFullSessionCommand command = new ViewFullSessionCommand(householdId, 2);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));

        String expectedMessage = String.format(ViewFullSessionCommand.MESSAGE_INVALID_SESSION_INDEX, 2, householdId);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void execute_validView_success() throws CommandException {
        when(householdBook.getHouseholdById(eq(householdId))).thenReturn(Optional.of(household));
        // Create a household with one session so that sessionIndex 1 is valid.
        Session dummySession = mock(Session.class);
        ObservableList<Session> sessions = FXCollections.observableArrayList(dummySession);
        when(household.getSessions()).thenReturn(sessions);

        int sessionIndex = 1;
        ViewFullSessionCommand command = new ViewFullSessionCommand(householdId, sessionIndex);
        CommandResult result = command.execute(model);

        String expectedMessage = String.format(ViewFullSessionCommand.MESSAGE_SUCCESS, sessionIndex, householdId);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }
}

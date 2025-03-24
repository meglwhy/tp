package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
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
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;

class ListSessionsCommandTest {

    private Model model;
    private HouseholdBook householdBook;
    private HouseholdId householdId;
    private Household household;
    private Session session;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);
        when(model.getHouseholdBook()).thenReturn(householdBook);

        householdId = new HouseholdId("H000003");
        session = mock(Session.class);
        when(session.getDate()).thenReturn(new SessionDate("2025-03-15"));
        when(session.getTime()).thenReturn(new SessionTime("14:30"));

        ObservableList<Session> sessions = FXCollections.observableArrayList(session);

        household = mock(Household.class);
        when(household.getSessions()).thenReturn(sessions);
        when(householdBook.getHouseholdById(householdId)).thenReturn(Optional.of(household));
    }

    @Test
    void execute_listSessions_success() throws CommandException {
        ListSessionsCommand command = new ListSessionsCommand(householdId);
        CommandResult result = command.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Listed all sessions for household"));
    }

    @Test
    void execute_noSessions_returnsNoSessionsMessage() throws CommandException {
        ObservableList<Session> emptySessions = FXCollections.observableArrayList();
        when(household.getSessions()).thenReturn(emptySessions);
        ListSessionsCommand command = new ListSessionsCommand(householdId);
        CommandResult result = command.execute(model);
        assertTrue(result.getFeedbackToUser().contains("No sessions to list for household"));
    }

    @Test
    void execute_householdNotFound_throwsCommandException() {
        when(householdBook.getHouseholdById(householdId)).thenReturn(Optional.empty());
        ListSessionsCommand command = new ListSessionsCommand(householdId);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(ListSessionsCommand.MESSAGE_HOUSEHOLD_NOT_FOUND, householdId),
                exception.getMessage());
    }
}

package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;

class ViewHouseholdSessionsCommandTest {

    private Model model;
    private HouseholdBook householdBook;
    private HouseholdId householdId;
    private Household household;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);
        when(model.getHouseholdBook()).thenReturn(householdBook);

        householdId = new HouseholdId("H000007");
        household = mock(Household.class);
        when(household.getId()).thenReturn(householdId);
        // Return an ObservableList for the household list.
        ObservableList<Household> households = FXCollections.observableArrayList(household);
        when(householdBook.getHouseholdList()).thenReturn(households);
    }

    @Test
    void execute_viewSessions_success() throws CommandException {
        ViewHouseholdSessionsCommand command = new ViewHouseholdSessionsCommand(householdId);
        CommandResult result = command.execute(model);
        verify(model).updateFilteredSessionList(any());
        assertTrue(result.getFeedbackToUser().contains("Viewing sessions for household"));
    }

    @Test
    void execute_householdNotFound_throwsCommandException() {
        // Simulate no household found by returning an empty ObservableList.
        ObservableList<Household> emptyHouseholds = FXCollections.observableArrayList();
        when(householdBook.getHouseholdList()).thenReturn(emptyHouseholds);
        ViewHouseholdSessionsCommand command = new ViewHouseholdSessionsCommand(householdId);
        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertTrue(exception.getMessage().contains("Household not found"));
    }
}

package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;

public class DeleteHouseholdCommandTest {

    @Mock
    private Model model;

    @Mock
    private HouseholdBook householdBook;

    @Mock
    private Household householdToDelete;

    private HouseholdId targetId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        targetId = new HouseholdId("H000001");

        // Mock the behavior of the model and household book
        when(model.getHouseholdBook()).thenReturn(householdBook);
    }

    @Test
    public void execute_validHouseholdId_success() throws CommandException {
        when(householdBook.getHouseholdById(targetId)).thenReturn(Optional.of(householdToDelete));

        // Create a spy of DeleteHouseholdCommand to mock showConfirmationDialog
        DeleteHouseholdCommand deleteCommand = Mockito.spy(new DeleteHouseholdCommand(targetId));

        // Mock the showConfirmationDialog to return true (confirm deletion)
        doReturn(true).when(deleteCommand).confirmDeletion(householdToDelete);

        CommandResult result = deleteCommand.execute(model);
        assertEquals(String.format(DeleteHouseholdCommand.MESSAGE_DELETE_HOUSEHOLD_SUCCESS, householdToDelete),
                result.getFeedbackToUser());

        verify(householdBook).removeHousehold(householdToDelete);
    }

    @Test
    public void execute_householdNotFound_throwsCommandException() {
        HouseholdId nonExistentId = new HouseholdId("H000002");
        when(householdBook.getHouseholdById(nonExistentId)).thenReturn(Optional.empty());
        DeleteHouseholdCommand deleteCommand = new DeleteHouseholdCommand(nonExistentId);
        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    @Test
    public void execute_cancellation_success() throws CommandException {
        when(householdBook.getHouseholdById(targetId)).thenReturn(Optional.of(householdToDelete));

        // Create a spy of DeleteHouseholdCommand to mock showConfirmationDialog
        DeleteHouseholdCommand deleteCommand = Mockito.spy(new DeleteHouseholdCommand(targetId));

        // Mock the showConfirmationDialog to return false (cancel deletion)
        doReturn(false).when(deleteCommand).confirmDeletion(householdToDelete);

        CommandResult result = deleteCommand.execute(model);

        assertEquals(DeleteHouseholdCommand.MESSAGE_CANCELLED, result.getFeedbackToUser());
        verify(householdBook, times(0)).removeHousehold(householdToDelete);
    }

    @Test
    public void execute_invalidHouseholdId_throwsCommandException() {
        HouseholdId invalidId = new HouseholdId("H000003");

        when(householdBook.getHouseholdById(invalidId)).thenReturn(Optional.empty());

        DeleteHouseholdCommand deleteCommand = new DeleteHouseholdCommand(invalidId);

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    @Test
    public void testEquals() {
        HouseholdId anotherId = new HouseholdId("H000001");
        HouseholdId differentId = new HouseholdId("H000002");

        DeleteHouseholdCommand command1 = new DeleteHouseholdCommand(anotherId);
        DeleteHouseholdCommand command2 = new DeleteHouseholdCommand(anotherId);
        DeleteHouseholdCommand command3 = new DeleteHouseholdCommand(differentId);
        assertTrue(command1.equals(command1));
        assertTrue(command1.equals(command2));
        // Different target ID, should not be equal
        assertFalse(command1.equals(command3));
    }
}

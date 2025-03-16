import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import seedu.address.logic.commands.DeleteHouseholdCommand;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.HouseholdBook;
import seedu.address.model.household.HouseholdId;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

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
        // Mock a valid household found by ID
        when(householdBook.getHouseholdById(targetId)).thenReturn(Optional.of(householdToDelete));

        // Mock the showConfirmationDialog to return true (confirm deletion)
        DeleteHouseholdCommand deleteCommand = new DeleteHouseholdCommand(targetId);
        doReturn(true).when(deleteCommand).showConfirmationDialog(householdToDelete);

        // Execute the command
        CommandResult result = deleteCommand.execute(model);

        // Verify the result and that the household was removed
        assertEquals(String.format(DeleteHouseholdCommand.MESSAGE_DELETE_HOUSEHOLD_SUCCESS, householdToDelete),
                result.getFeedbackToUser());

        // Verify that the correct methods were called
        verify(householdBook).removeHousehold(householdToDelete);
    }

    @Test
    public void execute_householdNotFound_throwsCommandException() {
        HouseholdId nonExistentId = new HouseholdId("H000002");

        // Mock that the household is not found
        when(householdBook.getHouseholdById(nonExistentId)).thenReturn(Optional.empty());

        // Create the delete command
        DeleteHouseholdCommand deleteCommand = new DeleteHouseholdCommand(nonExistentId);

        // Verify that CommandException is thrown
        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    @Test
    public void execute_cancellation_confirmed_cancelled() throws CommandException {
        // Mock that the household is found by ID
        when(householdBook.getHouseholdById(targetId)).thenReturn(Optional.of(householdToDelete));

        // Mock the showConfirmationDialog to return false (cancel deletion)
        DeleteHouseholdCommand deleteCommand = new DeleteHouseholdCommand(targetId);
        doReturn(false).when(deleteCommand).showConfirmationDialog(householdToDelete);

        // Execute the command
        CommandResult result = deleteCommand.execute(model);

        // Verify the result (deletion cancelled)
        assertEquals(DeleteHouseholdCommand.MESSAGE_CANCELLED, result.getFeedbackToUser());

        // Verify that removeHousehold was not called
        verify(householdBook, times(0)).removeHousehold(householdToDelete);
    }

    @Test
    public void execute_invalidHouseholdId_throwsCommandException() {
        HouseholdId invalidId = new HouseholdId("H000003");

        // Mock that the household is not found
        when(householdBook.getHouseholdById(invalidId)).thenReturn(Optional.empty());

        DeleteHouseholdCommand deleteCommand = new DeleteHouseholdCommand(invalidId);

        // Check that an exception is thrown for invalid ID
        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    @Test
    public void testEquals() {
        HouseholdId anotherId = new HouseholdId("H000001");
        HouseholdId differentId = new HouseholdId("H000002");

        DeleteHouseholdCommand command1 = new DeleteHouseholdCommand(anotherId);
        DeleteHouseholdCommand command2 = new DeleteHouseholdCommand(anotherId);
        DeleteHouseholdCommand command3 = new DeleteHouseholdCommand(differentId);

        // Same object, should be equal
        assertTrue(command1.equals(command1));

        // Same target ID, should be equal
        assertTrue(command1.equals(command2));

        // Different target ID, should not be equal
        assertFalse(command1.equals(command3));
    }
}

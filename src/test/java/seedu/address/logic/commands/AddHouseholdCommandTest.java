package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.HouseholdBook;

class AddHouseholdCommandTest {

    private Model model;
    private HouseholdBook householdBook;
    private Household validHousehold;
    private Household duplicateHousehold;

    @BeforeEach
    void setUp() {
        // Create mocks for Model and HouseholdBook
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);

        // Set up the mocked model to return the mocked household book
        when(model.getHouseholdBook()).thenReturn(householdBook);

        // Create sample Household objects for testing
        validHousehold = new Household(new seedu.address.model.household.Name("Smith Family"),
                new seedu.address.model.household.Address("123 Main St"),
                new seedu.address.model.household.Contact("98765432"));
        duplicateHousehold = new Household(new seedu.address.model.household.Name("Smith Family"),
                new seedu.address.model.household.Address("123 Main St"),
                new seedu.address.model.household.Contact("98765432"));
    }

    @Test
    void execute_validHousehold_addsHouseholdToModel() throws CommandException {
        // Set up mock behavior for no duplicate household
        when(householdBook.hasHousehold(validHousehold)).thenReturn(false);

        // Create the AddHouseholdCommand
        AddHouseholdCommand addHouseholdCommand = new AddHouseholdCommand(validHousehold);

        // Execute the command
        CommandResult result = addHouseholdCommand.execute(model);

        // Verify that addHousehold is called once and with correct parameter
        verify(householdBook).addHousehold(validHousehold);

        // Verify the result
        assertEquals(String.format(AddHouseholdCommand.MESSAGE_SUCCESS, validHousehold), result.getFeedbackToUser());
    }

    @Test
    void execute_duplicateHousehold_throwsCommandException() {
        // Set up mock behavior for duplicate household
        when(householdBook.hasHousehold(duplicateHousehold)).thenReturn(true);

        // Create the AddHouseholdCommand
        AddHouseholdCommand addHouseholdCommand = new AddHouseholdCommand(duplicateHousehold);

        // Execute the command and assert that a CommandException is thrown
        CommandException exception = assertThrows(CommandException.class, () -> addHouseholdCommand.execute(model));

        // Verify the exception message
        assertEquals(AddHouseholdCommand.MESSAGE_DUPLICATE_HOUSEHOLD, exception.getMessage());
    }

    @Test
    void equals_sameObject_returnsTrue() {
        AddHouseholdCommand addHouseholdCommand1 = new AddHouseholdCommand(validHousehold);
        AddHouseholdCommand addHouseholdCommand2 = new AddHouseholdCommand(validHousehold);

        // Test equality between the same object
        assertTrue(addHouseholdCommand1.equals(addHouseholdCommand2));
    }

    @Test
    void equals_differentObject_returnsFalse() {
        AddHouseholdCommand addHouseholdCommand1 = new AddHouseholdCommand(validHousehold);
        AddHouseholdCommand addHouseholdCommand2 = new AddHouseholdCommand(duplicateHousehold);

        // Test inequality between different objects
        assertFalse(addHouseholdCommand1.equals(addHouseholdCommand2));
    }

    @Test
    void equals_nullObject_returnsFalse() {
        AddHouseholdCommand addHouseholdCommand = new AddHouseholdCommand(validHousehold);

        // Test inequality with null
        assertFalse(addHouseholdCommand.equals(null));
    }

    @Test
    void equals_differentClass_returnsFalse() {
        AddHouseholdCommand addHouseholdCommand = new AddHouseholdCommand(validHousehold);

        // Test inequality with different class type
        assertFalse(addHouseholdCommand.equals(new Object()));
    }
}

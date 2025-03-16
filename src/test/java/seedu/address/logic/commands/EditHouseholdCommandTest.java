package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;

class EditHouseholdCommandTest {

    private Model model;
    private HouseholdBook householdBook;
    private HouseholdId householdId;
    private Household existingHousehold;
    private Household editedHousehold;
    private EditHouseholdCommand.EditHouseholdDescriptor editHouseholdDescriptor;

    @BeforeEach
    void setUp() {
        // Create mocks for Model and HouseholdBook
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);

        // Set up the mocked model to return the mocked household book
        when(model.getHouseholdBook()).thenReturn(householdBook);

        // Set up household objects for testing
        householdId = new HouseholdId("H000001");
        existingHousehold = new Household(
                new Name("Smith Family"),
                new Address("123 Main St"),
                new Contact("98765432"),
                householdId,
                new HashSet<>()
        );

        // Create EditHouseholdDescriptor with a name change
        editHouseholdDescriptor = new EditHouseholdCommand.EditHouseholdDescriptor();
        editHouseholdDescriptor.setName(new Name("Smith Family Updated"));
    }

    @Test
    void execute_validHousehold_editSuccess() throws CommandException {
        // Set up mock behavior for existing household and the editing
        when(householdBook.getHouseholdList().stream().filter(h -> h.getId().equals(householdId))
                .findFirst()).thenReturn(Optional.of(existingHousehold));

        // Create the edited household
        editedHousehold = new Household(
                new Name("Smith Family Updated"),
                new Address("123 Main St"),
                new Contact("98765432"),
                householdId,
                new HashSet<>()
        );

        // Set up the model to update household
        EditHouseholdCommand editHouseholdCommand = new EditHouseholdCommand(householdId, editHouseholdDescriptor);

        // Execute the command
        CommandResult result = editHouseholdCommand.execute(model);

        // Verify the update
        verify(householdBook).updateHousehold(existingHousehold, editedHousehold);

        // Verify the result message
        assertEquals(String.format(EditHouseholdCommand.MESSAGE_EDIT_HOUSEHOLD_SUCCESS, editedHousehold),
                result.getFeedbackToUser());
    }

    @Test
    void execute_householdNotFound_throwsCommandException() {
        // Set up mock behavior to return an empty result when searching for the household
        when(householdBook.getHouseholdList().stream().filter(h -> h.getId().equals(householdId))
                .findFirst()).thenReturn(Optional.empty());

        // Create the EditHouseholdCommand
        EditHouseholdCommand editHouseholdCommand = new EditHouseholdCommand(householdId, editHouseholdDescriptor);

        // Execute and assert exception is thrown
        CommandException exception = assertThrows(CommandException.class, () -> editHouseholdCommand.execute(model));

        // Verify exception message
        assertEquals(String.format(EditHouseholdCommand.MESSAGE_HOUSEHOLD_NOT_FOUND,
                householdId), exception.getMessage());
    }

    @Test
    void execute_duplicateHousehold_throwsCommandException() {
        // Set up mock behavior for existing household and the edited household causing a duplicate
        Household editedDuplicateHousehold = new Household(
                new Name("Smith Family Updated"),
                new Address("123 Main St"),
                new Contact("98765432"),
                householdId,
                new HashSet<>()
        );
        when(householdBook.getHouseholdList().stream().filter(h -> h.getId().equals(householdId))
                .findFirst()).thenReturn(Optional.of(existingHousehold));
        when(householdBook.getHouseholdList().stream()
                .anyMatch(h -> h.equals(editedDuplicateHousehold))).thenReturn(true);

        // Create the EditHouseholdCommand
        EditHouseholdCommand editHouseholdCommand = new EditHouseholdCommand(householdId, editHouseholdDescriptor);

        // Execute and assert exception is thrown
        CommandException exception = assertThrows(CommandException.class, () -> editHouseholdCommand.execute(model));

        // Verify exception message
        assertEquals(EditHouseholdCommand.MESSAGE_DUPLICATE_HOUSEHOLD, exception.getMessage());
    }

    @Test
    void equals_sameObject_returnsTrue() {
        // Create two EditHouseholdCommand objects with the same values
        EditHouseholdCommand editHouseholdCommand1 = new EditHouseholdCommand(householdId, editHouseholdDescriptor);
        EditHouseholdCommand editHouseholdCommand2 = new EditHouseholdCommand(householdId, editHouseholdDescriptor);

        // Test equality between the same object
        assertTrue(editHouseholdCommand1.equals(editHouseholdCommand2));
    }

    @Test
    void equals_differentObject_returnsFalse() {
        // Create an EditHouseholdCommand with a different householdId
        EditHouseholdCommand editHouseholdCommand1 = new EditHouseholdCommand(householdId, editHouseholdDescriptor);
        EditHouseholdCommand editHouseholdCommand2 = new EditHouseholdCommand(new HouseholdId("H000002"),
                editHouseholdDescriptor);

        // Test inequality with different object
        assertFalse(editHouseholdCommand1.equals(editHouseholdCommand2));
    }

    @Test
    void equals_nullObject_returnsFalse() {
        EditHouseholdCommand editHouseholdCommand = new EditHouseholdCommand(householdId, editHouseholdDescriptor);

        // Test inequality with null
        assertFalse(editHouseholdCommand.equals(null));
    }

    @Test
    void equals_differentClass_returnsFalse() {
        EditHouseholdCommand editHouseholdCommand = new EditHouseholdCommand(householdId, editHouseholdDescriptor);

        // Test inequality with different class type
        assertFalse(editHouseholdCommand.equals(new Object()));
    }
}
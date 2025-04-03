package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.Name;

class AddHouseholdCommandTest {

    private Model model;
    private HouseholdBook householdBook;
    private Household validHousehold;
    private Household duplicateHousehold;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);

        when(model.getHouseholdBook()).thenReturn(householdBook);

        validHousehold = new Household(new Name("Smith Family"),
                new Address("123 Main St"),
                new Contact("98765432"));
        duplicateHousehold = new Household(new Name("Smith Family"),
                new Address("123 Main St"),
                new Contact("98765432"));
    }

    @Test
    void execute_validHousehold_addsHouseholdToModel() throws CommandException {
        when(householdBook.hasHousehold(validHousehold)).thenReturn(false);
        AddHouseholdCommand addHouseholdCommand = new AddHouseholdCommand(validHousehold);
        CommandResult result = addHouseholdCommand.execute(model);
        verify(householdBook).addHousehold(validHousehold);

        assertEquals(String.format(AddHouseholdCommand.MESSAGE_SUCCESS, validHousehold), result.getFeedbackToUser());
    }

    @Test
    void execute_duplicateHousehold_throwsCommandException() {
        when(householdBook.hasHousehold(duplicateHousehold)).thenReturn(true);
        AddHouseholdCommand addHouseholdCommand = new AddHouseholdCommand(duplicateHousehold);
        CommandException exception = assertThrows(CommandException.class, () -> addHouseholdCommand.execute(model));

        assertEquals(AddHouseholdCommand.MESSAGE_DUPLICATE_HOUSEHOLD, exception.getMessage());
    }

    @Test
    void equals_sameObject_returnsTrue() {
        AddHouseholdCommand addHouseholdCommand1 = new AddHouseholdCommand(validHousehold);
        AddHouseholdCommand addHouseholdCommand2 = new AddHouseholdCommand(validHousehold);

        assertEquals(addHouseholdCommand1, addHouseholdCommand2);
    }

    @Test
    void equals_differentObject_returnsFalse() {
        Household validHousehold = new Household(new Name("Smith Family"), new Address("123 Main St"),
                new Contact("98765432"));
        Household completelyDifferentHousehold = new Household(new Name("Johnson Family"), new Address("456 Elm St"),
                new Contact("82345678"));

        assertNotEquals(validHousehold, completelyDifferentHousehold);
    }

    @Test
    void equals_nullObject_returnsFalse() {
        AddHouseholdCommand addHouseholdCommand = new AddHouseholdCommand(validHousehold);

        assertNotEquals(null, addHouseholdCommand);
    }

    @Test
    void equals_differentClass_returnsFalse() {
        AddHouseholdCommand addHouseholdCommand = new AddHouseholdCommand(validHousehold);

        // Test inequality with different class type
        assertNotEquals(new Object(), addHouseholdCommand);
    }
}

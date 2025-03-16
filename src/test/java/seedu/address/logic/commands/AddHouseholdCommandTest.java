import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import seedu.address.logic.commands.AddHouseholdCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdBook;

class AddHouseholdCommandTest {

    private Model model;
    private HouseholdBook householdBook;
    private Household validHousehold;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);
        validHousehold = new Household("Smith Family", "123 Main St", "98765432");

        when(model.getHouseholdBook()).thenReturn(householdBook);
    }

    @Test
    void execute_newHousehold_success() throws Exception {
        when(householdBook.hasHousehold(validHousehold)).thenReturn(false);

        AddHouseholdCommand command = new AddHouseholdCommand(validHousehold);
        CommandResult result = command.execute(model);

        verify(householdBook).addHousehold(validHousehold);
        assertEquals(String.format(AddHouseholdCommand.MESSAGE_SUCCESS, validHousehold), result.getFeedbackToUser());
    }

    @Test
    void execute_duplicateHousehold_throwsCommandException() {
        when(householdBook.hasHousehold(validHousehold)).thenReturn(true);

        AddHouseholdCommand command = new AddHouseholdCommand(validHousehold);
        Executable executable = () -> command.execute(model);

        CommandException thrown = assertThrows(CommandException.class, executable);
        assertEquals(AddHouseholdCommand.MESSAGE_DUPLICATE_HOUSEHOLD, thrown.getMessage());
    }

    @Test
    void equals_sameObject_returnsTrue() {
        AddHouseholdCommand command = new AddHouseholdCommand(validHousehold);
        assertTrue(command.equals(command));
    }

    @Test
    void equals_differentType_returnsFalse() {
        AddHouseholdCommand command = new AddHouseholdCommand(validHousehold);
        assertFalse(command.equals(new Object()));
    }

    @Test
    void equals_differentHousehold_returnsFalse() {
        AddHouseholdCommand command1 = new AddHouseholdCommand(validHousehold);
        Household anotherHousehold = new Household("Johnson Family", "456 Elm St", "91234567");
        AddHouseholdCommand command2 = new AddHouseholdCommand(anotherHousehold);

        assertFalse(command1.equals(command2));
    }
}

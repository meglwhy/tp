package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditHouseholdCommand.EditHouseholdDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.tag.Tag;

public class EditHouseholdCommandTest {

    private Model model;
    private HouseholdId householdId;
    private Household household;
    private EditHouseholdCommand.EditHouseholdDescriptor descriptor;
    private Set<Tag> tags;

    @BeforeEach
    public void setUp() {
        model = mock(Model.class);

        // Mock the HouseholdBook
        HouseholdBook householdBook = mock(HouseholdBook.class);
        when(model.getHouseholdBook()).thenReturn(householdBook);

        // Set up mock household data
        householdId = new HouseholdId("H000001");
        Name name = new Name("Sample Name");
        Address address = new Address("123 Sample St");
        Contact contact = new Contact("98765432");
        tags = new HashSet<>();
        tags.add(new Tag("priority"));

        household = new Household(name, address, contact, householdId, tags);

        // Mock the behavior of the household book
        when(householdBook.getHouseholdById(householdId)).thenReturn(Optional.of(household));
    }

    @Test
    public void execute_editHousehold_success() throws CommandException {
        // Prepare the descriptor
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setContact(new Contact("91234567")); // Updated contact

        // Create the command
        EditHouseholdCommand command = new EditHouseholdCommand(householdId, descriptor);

        // Mock the behavior of household book
        when(model.getHouseholdBook().hasHousehold(any(Household.class))).thenReturn(false);

        CommandResult result = command.execute(model);

        // The expected message should reflect the updated contact
        assertEquals(String.format(EditHouseholdCommand.MESSAGE_EDIT_HOUSEHOLD_SUCCESS,
                        new Household(new Name("Sample Name"), new Address("123 Sample St"),
                                new Contact("91234567"), new HouseholdId("H000001"), tags)),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_duplicateHousehold_throwsCommandException() throws CommandException {
        // Prepare the descriptor that would create a duplicate household
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setContact(new Contact("98765432"));

        // Create the command
        EditHouseholdCommand command = new EditHouseholdCommand(householdId, descriptor);

        // Mock the behavior of household book to simulate a duplicate
        when(model.getHouseholdBook().hasHousehold(any(Household.class))).thenReturn(true);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_householdNotFound_throwsCommandException() throws CommandException {
        // Prepare the descriptor
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setContact(new Contact("91234567"));

        // Create the command with a non-existent ID
        EditHouseholdCommand command = new EditHouseholdCommand(new HouseholdId("H000002"), descriptor);

        // Mock the behavior of household book to simulate a not found household
        when(model.getHouseholdBook().getHouseholdById(any(HouseholdId.class))).thenReturn(Optional.empty());

        assertThrows(CommandException.class, () -> command.execute(model));
    }
}

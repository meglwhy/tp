package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    private Set<Tag> tags;

    private Name originalName;
    private Address originalAddress;
    private Contact originalContact;

    @BeforeEach
    public void setUp() {
        model = mock(Model.class);
        HouseholdBook householdBook = mock(HouseholdBook.class);
        when(model.getHouseholdBook()).thenReturn(householdBook);

        householdId = new HouseholdId("H000001");
        originalName = new Name("Sample Name");
        originalAddress = new Address("123 Sample St");
        originalContact = new Contact("98765432");
        tags = new HashSet<>();
        tags.add(new Tag("priority"));

        Household household = new Household(
                originalName, originalAddress, originalContact, householdId, tags);
        when(householdBook.getHouseholdById(householdId))
                .thenReturn(Optional.of(household));
    }

    @Test
    public void executeEditHouseholdSuccess() throws CommandException {
        // Create descriptor that updates the contact.
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setContact(new Contact("91234567")); // Updated contact

        EditHouseholdCommand command = new EditHouseholdCommand(householdId, descriptor);
        // In a successful edit, the householdBook must not consider the updated household as duplicate.
        when(model.getHouseholdBook().hasHousehold(any(Household.class))).thenReturn(false);

        CommandResult result = command.execute(model);

        Household expectedHousehold = new Household(
                originalName,
                originalAddress,
                new Contact("91234567"),
                new HouseholdId("H000001"),
                tags);
        // The expected feedback message includes the string representation of the edited household.
        assertEquals(String.format(EditHouseholdCommand.MESSAGE_EDIT_HOUSEHOLD_SUCCESS,
                expectedHousehold), result.getFeedbackToUser());
    }

    @Test
    public void executeDuplicateHouseholdThrowsCommandException() {
        // Create descriptor without any actual change, so edited household equals original.
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setContact(new Contact("98765432"));

        EditHouseholdCommand command = new EditHouseholdCommand(householdId, descriptor);
        when(model.getHouseholdBook().hasHousehold(any(Household.class))).thenReturn(true);

        CommandException exception =
                assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(EditHouseholdCommand.MESSAGE_DUPLICATE_HOUSEHOLD, exception.getMessage());
    }

    @Test
    public void equalsSameValuesReturnsTrue() {
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setContact(new Contact("91234567"));

        EditHouseholdCommand command1 = new EditHouseholdCommand(householdId, descriptor);
        EditHouseholdCommand command2 = new EditHouseholdCommand(householdId, descriptor);
        assertEquals(command1, command2);
    }

    @Test
    public void equalsSameObjectReturnsTrue() {
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setAddress(new Address("123 Sample St"));

        EditHouseholdCommand command = new EditHouseholdCommand(householdId, descriptor);
        assertEquals(command, command);
    }

    @Test
    public void equalsDifferentHouseholdIdReturnsFalse() {
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setName(new Name("New Name"));

        HouseholdId differentHouseholdId = new HouseholdId("H000002");
        EditHouseholdCommand command1 = new EditHouseholdCommand(householdId, descriptor);
        EditHouseholdCommand command2 = new EditHouseholdCommand(differentHouseholdId, descriptor);
        assertNotEquals(command1, command2);
    }

    @Test
    public void equalsDifferentDescriptorReturnsFalse() {
        EditHouseholdDescriptor descriptor1 = new EditHouseholdDescriptor();
        descriptor1.setContact(new Contact("91234567"));

        EditHouseholdDescriptor descriptor2 = new EditHouseholdDescriptor();
        descriptor2.setAddress(new Address("456 Other St"));

        EditHouseholdCommand command1 = new EditHouseholdCommand(householdId, descriptor1);
        EditHouseholdCommand command2 = new EditHouseholdCommand(householdId, descriptor2);
        assertNotEquals(command1, command2);
    }

    @Test
    public void equalsNullReturnsFalse() {
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setName(new Name("Test"));
        EditHouseholdCommand command = new EditHouseholdCommand(householdId, descriptor);
        assertNotEquals(command, null);
    }

    @Test
    public void equalsDifferentTypeReturnsFalse() {
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setContact(new Contact("91234567"));
        EditHouseholdCommand command = new EditHouseholdCommand(householdId, descriptor);
        assertNotEquals(command, "some string");
    }

    @Test
    public void descriptorEqualsDifferentValuesReturnsFalse() {
        EditHouseholdDescriptor descriptor1 = new EditHouseholdDescriptor();
        descriptor1.setName(new Name("Alice"));

        EditHouseholdDescriptor descriptor2 = new EditHouseholdDescriptor();
        descriptor2.setName(new Name("Bob"));

        assertNotEquals(descriptor1, descriptor2);
    }

    @Test
    public void descriptorEqualsNullReturnsFalse() {
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setContact(new Contact("91234567"));
        assertNotEquals(descriptor, null);
    }

    @Test
    public void descriptorEqualsDifferentTypeReturnsFalse() {
        EditHouseholdDescriptor descriptor = new EditHouseholdDescriptor();
        descriptor.setAddress(new Address("123 Sample St"));
        assertNotEquals(descriptor, "not a descriptor");
    }
}

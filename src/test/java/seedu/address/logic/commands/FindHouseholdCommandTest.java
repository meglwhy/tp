package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.tag.Tag;

public class FindHouseholdCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = mock(Model.class);
    }

    @Test
    public void constructor_nullKeywords_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FindHouseholdCommand(null));
    }

    @Test
    public void constructor_emptyKeywords_throwsCommandException() {
        assertThrows(CommandException.class, () -> new FindHouseholdCommand("   "));
    }

    @Test
    public void execute_singleKeywordMatch_success() throws CommandException {
        Household household = createHousehold("John Household", "123 Main St", "91234567",
                "H000001", "priority");
        ObservableList<Household> households = FXCollections.observableArrayList(household);

        when(model.getFilteredHouseholdList()).thenReturn(households);

        FindHouseholdCommand command = new FindHouseholdCommand("John");
        CommandResult result = command.execute(model);

        verify(model).updateFilteredHouseholdList(command.predicate);
        assertEquals("Found 1 household(s) matching: John", result.getFeedbackToUser());
    }

    @Test
    public void execute_multipleKeywordsMatch_success() throws CommandException {
        Household household1 = createHousehold("John Household", "123 Main St", "91234567",
                "H000001", "priority");
        Household household2 = createHousehold("Jane Household", "456 Side St", "98765432",
                "H000002", "standard");
        ObservableList<Household> households = FXCollections.observableArrayList(household1, household2);

        when(model.getFilteredHouseholdList()).thenReturn(households);

        FindHouseholdCommand command = new FindHouseholdCommand("John Jane");
        CommandResult result = command.execute(model);

        verify(model).updateFilteredHouseholdList(command.predicate);
        assertEquals("Found 2 household(s) matching: John Jane", result.getFeedbackToUser());
    }

    @Test
    public void execute_quotedPhraseMatch_success() throws CommandException {
        Household household = createHousehold("John Smith", "123 Main St", "91234567",
                "H000001", "priority");
        ObservableList<Household> households = FXCollections.observableArrayList(household);

        when(model.getFilteredHouseholdList()).thenReturn(households);

        FindHouseholdCommand command = new FindHouseholdCommand("\"John Smith\"");
        CommandResult result = command.execute(model);

        verify(model).updateFilteredHouseholdList(command.predicate);
        assertEquals("Found 1 household(s) matching: \"John Smith\"", result.getFeedbackToUser());
    }

    @Test
    public void execute_numericSearch_success() throws CommandException {
        Household household = createHousehold("Jane Doe", "789 Side St", "98765432",
                "H000003", "standard");
        ObservableList<Household> households = FXCollections.observableArrayList(household);

        when(model.getFilteredHouseholdList()).thenReturn(households);

        FindHouseholdCommand command = new FindHouseholdCommand("98765432");
        CommandResult result = command.execute(model);

        verify(model).updateFilteredHouseholdList(command.predicate);
        assertEquals("Found 1 household(s) matching: 98765432", result.getFeedbackToUser());
    }

    @Test
    public void execute_noMatchingHouseholds_returnsNoMatchMessage() throws CommandException {
        ObservableList<Household> households = FXCollections.observableArrayList();
        when(model.getFilteredHouseholdList()).thenReturn(households);

        FindHouseholdCommand command = new FindHouseholdCommand("NonExistentName");
        CommandResult result = command.execute(model);

        verify(model).updateFilteredHouseholdList(command.predicate);
        assertEquals("No households found matching: NonExistentName", result.getFeedbackToUser());
    }

    // Helper method to create test Household objects
    private Household createHousehold(String name, String address, String contact, String id, String tag) {
        return new Household(new Name(name), new Address(address), new Contact(contact),
                new HouseholdId(id), new HashSet<>(Collections.singletonList(new Tag(tag))));
    }
}

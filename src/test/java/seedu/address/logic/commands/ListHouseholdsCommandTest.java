package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.Model;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.tag.Tag;

public class ListHouseholdsCommandTest {

    private Model model;
    private ListHouseholdsCommand listHouseholdsCommand;

    @BeforeEach
    public void setUp() {
        // Mocking the Model interface
        model = mock(Model.class);
        listHouseholdsCommand = new ListHouseholdsCommand();
    }

    @Test
    public void execute_nonEmptyHouseholdList_success() {
        HouseholdId householdId1 = new HouseholdId("H000001");
        Household household1 = new Household(
                new Name("John Household"), new Address("123 Main St"),
                new Contact("91234567"), householdId1, new HashSet<>(List.of(new Tag("priority"))));

        HouseholdId householdId2 = new HouseholdId("H000002");
        Household household2 = new Household(
                new Name("Jane Household"), new Address("456 Side St"),
                new Contact("98765432"), householdId2, new HashSet<>(List.of(new Tag("standard"))));

        ObservableList<Household> households = FXCollections.observableArrayList(household1, household2);
        when(model.getFilteredHouseholdList()).thenReturn(households);

        CommandResult result = listHouseholdsCommand.execute(model);

        String expectedMessage = "Listed all households.\nTotal households: 2";
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_emptyHouseholdList_success() {
        ObservableList<Household> households = FXCollections.observableArrayList();
        when(model.getFilteredHouseholdList()).thenReturn(households);

        CommandResult result = listHouseholdsCommand.execute(model);

        String expectedMessage = "Listed all households.\nTotal households: 0";
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_singleHousehold_success() {
        HouseholdId householdId = new HouseholdId("H000001");
        Household household = new Household(
                new Name("Single Household"), new Address("123 Unique St"),
                new Contact("98765432"), householdId, new HashSet<>(List.of(new Tag("priority"))));

        ObservableList<Household> households = FXCollections.observableArrayList(household);
        when(model.getFilteredHouseholdList()).thenReturn(households);

        CommandResult result = listHouseholdsCommand.execute(model);

        String expectedMessage = "Listed all households.\nTotal households: 1";
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }
}

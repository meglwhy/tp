package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.file.Paths;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionNote;
import seedu.address.model.session.SessionTime;

class ModelManagerTest {
    private ModelManager modelManager;
    private HouseholdBook householdBook;
    private UserPrefs userPrefs;

    @BeforeEach
    public void setUp() {
        householdBook = new HouseholdBook();

        // Create a dummy household with a valid contact number.
        Household household = new Household(
                new Name("Test Family"),
                new Address("Test Address"),
                new Contact("91234567"), // valid (starts with 9)
                HouseholdId.fromString("H000001"),
                Set.of());
        householdBook.addHousehold(household);

        // Add a dummy session.
        Session session = new Session(HouseholdId.fromString("H000001"),
                new SessionDate("2024-01-01"),
                new SessionTime("09:00"),
                new SessionNote("Test Session"));
        householdBook.addSessionToHousehold(household.getId(), session);

        userPrefs = new UserPrefs();
        userPrefs.setHouseholdBookFilePath(Paths.get("dummy/path"));
        userPrefs.setGuiSettings(new GuiSettings(800, 600, 0, 0));

        modelManager = new ModelManager(householdBook, userPrefs);
    }

    @Test
    public void testGetHouseholdBook() {
        assertEquals(householdBook, modelManager.getHouseholdBook());
    }

    @Test
    public void testGetHouseholdBookFilePath() {
        assertEquals(userPrefs.getHouseholdBookFilePath(), modelManager.getHouseholdBookFilePath());
    }

    @Test
    public void testUpdateFilteredHouseholdList() {
        ObservableList<Household> filtered = modelManager.getFilteredHouseholdList();
        assertEquals(1, filtered.size());
        Predicate<Household> predicate = h -> false;
        modelManager.updateFilteredHouseholdList(predicate);
        filtered = modelManager.getFilteredHouseholdList();
        assertEquals(0, filtered.size());
    }

    @Test
    public void testUpdateFilteredSessionList() {
        ObservableList<Session> filtered = modelManager.getFilteredSessionList();
        assertEquals(1, filtered.size());
        Predicate<Session> predicate = s -> false;
        modelManager.updateFilteredSessionList(predicate);
        filtered = modelManager.getFilteredSessionList();
        assertEquals(0, filtered.size());
    }

    @Test
    public void testEquals() {
        ModelManager copy = new ModelManager(householdBook, userPrefs);
        assertEquals(modelManager, copy);

        // Modify householdBook.
        HouseholdBook differentHb = new HouseholdBook();
        Household diffHousehold = new Household(
                new Name("Different Family"),
                new Address("Different Address"),
                new Contact("81234567"), // valid (starts with 8)
                HouseholdId.fromString("H000002"),
                Set.of());
        differentHb.addHousehold(diffHousehold);
        ModelManager differentModel = new ModelManager(differentHb, userPrefs);
        assertNotEquals(modelManager, differentModel);

        // Modify userPrefs.
        UserPrefs differentPrefs = new UserPrefs();
        differentPrefs.setHouseholdBookFilePath(Paths.get("different/path"));
        differentModel = new ModelManager(householdBook, differentPrefs);
        assertNotEquals(modelManager, differentModel);
    }
}


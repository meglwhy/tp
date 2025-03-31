package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;

/**
 * Test class for {@code EditSessionCommand}.
 *
 * This class uses a ModelStub implementing the Model interface, backed by a real HouseholdBook
 * containing a Household (constructed with dummy Name, Address, and Contact stubs via Mockito)
 * and Sessions. It tests several scenarios:
 * - Successful edit (with and without a note)
 * - Household not found
 * - Invalid session index
 * - Duplicate session conflict
 */
public class EditSessionCommandTest {

    // Original and new session details.
    private static final String ORIGINAL_DATE = "2025-03-31";
    private static final String ORIGINAL_TIME = "14:30";
    private static final String NEW_DATE = "2025-04-01";
    private static final String NEW_TIME = "15:00";
    private static final String NEW_NOTE = "Follow-up on medical assistance application";

    // A known household id.
    private final HouseholdId householdId = new HouseholdId("H000001");

    // Dummy stubs for household creation.
    // Replace these with actual implementations if available.
    private final seedu.address.model.household.Name dummyName = mock(seedu.address.model.household.Name.class);
    private final seedu.address.model.household.Address dummyAddress =
            mock(seedu.address.model.household.Address.class);
    private final seedu.address.model.household.Contact dummyContact =
            mock(seedu.address.model.household.Contact.class);

    // The household and household book used for testing.
    private Household household;
    private HouseholdBook householdBook;
    private Model model;

    /**
     * A simple ModelStub that implements the Model interface.
     * Only getHouseholdBook() is used by EditSessionCommand;
     * the remaining methods are stubbed out.
     */
    private static class ModelStub implements Model {
        private final HouseholdBook householdBook;

        ModelStub(HouseholdBook householdBook) {
            this.householdBook = householdBook;
        }

        @Override
        public HouseholdBook getHouseholdBook() {
            return householdBook;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            // no-op for testing
        }

        @Override
        public seedu.address.model.ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }

        @Override
        public Path getHouseholdBookFilePath() {
            return null;
        }

        @Override
        public void setHouseholdBookFilePath(Path householdBookFilePath) {
            // no-op for testing
        }

        @Override
        public ObservableList<Household> getFilteredHouseholdList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public ObservableList<Session> getFilteredSessionList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public void updateFilteredHouseholdList(Predicate<Household> predicate) {
            // no-op for testing
        }

        @Override
        public void updateFilteredSessionList(Predicate<Session> predicate) {
            // no-op for testing
        }
    }

    @BeforeEach
    public void setUp() {
        // Set up dummy values for the household's Name, Address, and Contact.
        when(dummyName.toString()).thenReturn("Dummy Name");
        when(dummyAddress.toString()).thenReturn("Dummy Address");
        when(dummyContact.toString()).thenReturn("Dummy Contact");

        // Create a household with a known householdId using the full constructor.
        // (An empty set is passed for tags.)
        household = new Household(dummyName, dummyAddress, dummyContact, householdId, new HashSet<>());

        // Create an original session with a fixed session id ("session1").
        Session originalSession = new Session("session1", householdId,
                new SessionDate(ORIGINAL_DATE), new SessionTime(ORIGINAL_TIME));
        // Add the original session manually.
        household.addSession(originalSession);
        // Also add the original session via HouseholdBook (this will add it again).
        householdBook = new HouseholdBook();
        householdBook.addHousehold(household);
        householdBook.addSessionToHousehold(householdId, originalSession);

        // Create a ModelStub backed by our HouseholdBook.
        model = new ModelStub(householdBook);
    }

    @Test
    public void execute_validEditWithoutNote_success() throws Exception {
        EditSessionCommand command = new EditSessionCommand(householdId, 1, NEW_DATE, NEW_TIME);
        CommandResult result = command.execute(model);

        Session updatedSession = household.getSessions().get(0);
        assertEquals("session1", updatedSession.getSessionId());
        assertEquals(NEW_DATE, updatedSession.getDate().toString());
        assertEquals(NEW_TIME, updatedSession.getTime().toString());
        assertFalse(updatedSession.hasNote());

        String expectedMessage = String.format(EditSessionCommand.MESSAGE_EDIT_SESSION_SUCCESS, NEW_DATE, NEW_TIME);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_validEditWithNote_success() throws Exception {
        EditSessionCommand command = new EditSessionCommand(householdId, 1, NEW_DATE, NEW_TIME, NEW_NOTE);
        CommandResult result = command.execute(model);

        Session updatedSession = household.getSessions().get(0);
        assertEquals("session1", updatedSession.getSessionId());
        assertEquals(NEW_DATE, updatedSession.getDate().toString());
        assertEquals(NEW_TIME, updatedSession.getTime().toString());
        assertTrue(updatedSession.hasNote());
        assertEquals(NEW_NOTE, updatedSession.getNote().toString());

        String expectedMessage = String.format(EditSessionCommand.MESSAGE_EDIT_SESSION_WITH_NOTE_SUCCESS,
                NEW_DATE, NEW_TIME, NEW_NOTE);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_householdNotFound_throwsCommandException() {
        HouseholdBook emptyBook = new HouseholdBook();
        Model modelWithNoHousehold = new ModelStub(emptyBook);

        EditSessionCommand command = new EditSessionCommand(householdId, 1, NEW_DATE, NEW_TIME);
        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(modelWithNoHousehold));
        String expectedMessage = String.format(EditSessionCommand.MESSAGE_HOUSEHOLD_NOT_FOUND, householdId);
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    public void execute_invalidSessionIndex_throwsCommandException() {
        // Since the session list size is 2 (due to duplicate addition), choose an index of 3.
        int invalidIndex = household.getSessions().size() + 1;
        EditSessionCommand command = new EditSessionCommand(householdId, invalidIndex, NEW_DATE, NEW_TIME);
        CommandException thrown = assertThrows(CommandException.class, (
                ) -> command.execute(model));
        String expectedMessage = String.format(EditSessionCommand.MESSAGE_INVALID_SESSION_INDEX,
                invalidIndex, householdId);
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    public void execute_duplicateSession_throwsCommandException() {
        // Add a conflicting session with the same NEW_DATE and NEW_TIME.
        Session conflictSession = new Session("session2", householdId,
                new SessionDate(NEW_DATE), new SessionTime(NEW_TIME));
        household.addSession(conflictSession);
        householdBook.addSessionToHousehold(householdId, conflictSession);

        EditSessionCommand command = new EditSessionCommand(householdId, 1, NEW_DATE, NEW_TIME);
        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        String expectedMessage = String.format(EditSessionCommand.MESSAGE_DUPLICATE_SESSION, conflictSession);
        assertEquals(expectedMessage, thrown.getMessage());
    }
}





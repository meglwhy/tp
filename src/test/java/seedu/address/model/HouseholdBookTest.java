package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionNote;
import seedu.address.model.session.SessionTime;
import seedu.address.model.tag.Tag;

class HouseholdBookTest {

    private HouseholdBook hb;
    private Household household1;
    private Household household2;
    private Session session1;
    private Session session2;

    @BeforeEach
    public void setUp() {
        hb = new HouseholdBook();

        // Create two dummy households with valid contact numbers.
        household1 = new Household(
                new Name("Family A"),
                new Address("123 A St"),
                new Contact("91234567"), // valid (starts with 9)
                HouseholdId.fromString("H000001"),
                Set.of(new Tag("tag1")));
        household2 = new Household(
                new Name("Family B"),
                new Address("456 B St"),
                new Contact("81234567"), // valid (starts with 8)
                HouseholdId.fromString("H000002"),
                Set.of(new Tag("tag2")));

        hb.addHousehold(household1);
        hb.addHousehold(household2);

        // Create two dummy sessions.
        session1 = new Session(HouseholdId.fromString("H000001"),
                new SessionDate("2024-01-01"),
                new SessionTime("10:00"),
                new SessionNote("Session one"));
        session2 = new Session(HouseholdId.fromString("H000002"),
                new SessionDate("2024-01-02"),
                new SessionTime("11:00"));
    }

    @Test
    public void testAddHousehold() {
        ObservableList<Household> list = hb.getHouseholdList();
        assertEquals(2, list.size());
        assertTrue(list.contains(household1));
        assertTrue(list.contains(household2));
    }

    @Test
    public void testHasHousehold_and_hasHouseholdId() {
        // Create a household with same details as household1 but with a different id.
        Household potentialDup = new Household(
                new Name("Family A"),
                new Address("123 A St"),
                new Contact("91234567"), // valid
                HouseholdId.fromString("H000003"),
                Set.of(new Tag("tag1")));
        // The check excludes same ID so household1 is considered duplicate.
        assertTrue(hb.hasHousehold(potentialDup));
        assertTrue(hb.hasHouseholdId(HouseholdId.fromString("H000001")));
        assertFalse(hb.hasHouseholdId(HouseholdId.fromString("NON_EXISTENT")));
    }

    @Test
    public void testResetData() {
        // Create a new HouseholdBook containing only household2 and session2.
        HouseholdBook newHb = new HouseholdBook();
        newHb.addHousehold(household2);
        newHb.addSessionToHousehold(household2.getId(), session2);

        hb.resetData(newHb);

        ObservableList<Household> list = hb.getHouseholdList();
        assertEquals(1, list.size());
        assertTrue(list.contains(household2));

        ObservableList<Session> sessions = hb.getSessionList();
        assertEquals(1, sessions.size());
        assertTrue(sessions.contains(session2));
    }

    @Test
    public void testAddSessionToHousehold() {
        // Add session1 to household1.
        hb.addSessionToHousehold(household1.getId(), session1);
        List<Session> sessions = hb.getSessions();
        assertTrue(sessions.contains(session1));
    }

    @Test
    public void testGetConflictingSession() {
        hb.addSessionToHousehold(household1.getId(), session1);

        // Create a conflicting session with the same date and time as session1.
        Session conflicting = new Session(HouseholdId.fromString("H000001"),
                new SessionDate("2024-01-01"),
                new SessionTime("10:00"),
                new SessionNote("Conflict"));
        // Exclude session1 from conflict check: no conflict should be detected.
        Optional<Session> conflictOpt = hb.getConflictingSession(conflicting, session1);
        assertTrue(conflictOpt.isEmpty());

        // Without exclusion, conflictOpt should return session1.
        conflictOpt = hb.getConflictingSession(conflicting);
        assertTrue(conflictOpt.isPresent());
        assertEquals(session1, conflictOpt.get());
    }

    @Test
    public void testRemoveSessionById() {
        hb.addSessionToHousehold(household1.getId(), session1);
        hb.addSessionToHousehold(household2.getId(), session2);
        hb.removeSessionById(session1.getSessionId());
        List<Session> sessions = hb.getSessions();
        assertFalse(sessions.contains(session1));
        assertTrue(sessions.contains(session2));
    }

    @Test
    public void testGetHouseholdById() {
        Optional<Household> opt = hb.getHouseholdById(HouseholdId.fromString("H000001"));
        assertTrue(opt.isPresent());
        assertEquals(household1, opt.get());
        opt = hb.getHouseholdById(HouseholdId.fromString("NON_EXISTENT"));
        assertTrue(opt.isEmpty());
    }

    @Test
    public void testGetHouseholdListUnmodifiable() {
        ObservableList<Household> list = hb.getHouseholdList();
        assertThrows(UnsupportedOperationException.class, () -> list.add(household1));
    }

    @Test
    public void testGetSessionListUnmodifiable() {
        ObservableList<Session> list = hb.getSessionList();
        assertThrows(UnsupportedOperationException.class, () -> list.add(session1));
    }

    @Test
    public void testEqualsAndHashCode() {
        HouseholdBook copy = new HouseholdBook();
        copy.addHousehold(household1);
        copy.addHousehold(household2);
        copy.addSessionToHousehold(household1.getId(), session1);

        // Ensure hb has the same households and then add session1 to hb as well.
        hb.addSessionToHousehold(household1.getId(), session1);
        assertEquals(hb, copy);
        assertEquals(hb.hashCode(), copy.hashCode());
    }
}



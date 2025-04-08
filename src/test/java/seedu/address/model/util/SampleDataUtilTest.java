package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyHouseholdBook;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionNote;
import seedu.address.model.session.SessionTime;
import seedu.address.model.tag.Tag;

public class SampleDataUtilTest {

    @Test
    public void testGetSampleHouseholds() {
        Household[] households = SampleDataUtil.getSampleHouseholds();
        // Expect three sample households.
        assertEquals(3, households.length);

        // Verify details of the first household (Tan Family).
        Household tanFamily = households[0];
        assertEquals(new Name("Tan Family"), tanFamily.getName());
        assertEquals("Blk 30 Geylang Street 29, #06-40", tanFamily.getAddress().toString());
        // Verify that the household ID is "H000001".
        assertEquals(HouseholdId.fromString("H000001"), tanFamily.getId());
        // Verify that its tag set contains "elderly" and "priority".
        Set<Tag> tanTags = tanFamily.getTags();
        assertEquals(2, tanTags.size());
        assertTrue(tanTags.contains(new Tag("elderly")));
        assertTrue(tanTags.contains(new Tag("priority")));

        // Optionally, you can check other households in a similar manner.
    }

    @Test
    public void testGetSampleSessions() {
        Session[] sessions = SampleDataUtil.getSampleSessions();
        // Expect three sample sessions.
        assertEquals(3, sessions.length);

        // Verify details of the first session.
        Session session1 = sessions[0];
        assertEquals(HouseholdId.fromString("H000001"), session1.getHouseholdId());
        // Verify date and time; assuming SessionDate.toString() and SessionTime.toString() return the original input.
        assertEquals(new SessionDate("2024-03-15").toString(), session1.getDate().toString());
        assertEquals(new SessionTime("14:00").toString(), session1.getTime().toString());
        // Check the session note.
        assertTrue(session1.hasNote());
        assertEquals(new SessionNote("First visit completed").toString(), session1.getNote().toString());

        // Verify the third session (no note provided).
        Session session3 = sessions[2];
        assertEquals(HouseholdId.fromString("H000003"), session3.getHouseholdId());
        assertEquals(new SessionDate("2024-03-17").toString(), session3.getDate().toString());
        assertEquals(new SessionTime("15:30").toString(), session3.getTime().toString());
        // When no note is provided, hasNote() should be false.
        assertTrue(!session3.hasNote());
    }

    @Test
    public void testGetSampleHouseholdBook() {
        ReadOnlyHouseholdBook sampleHb = SampleDataUtil.getSampleHouseholdBook();
        assertNotNull(sampleHb);
        // Expect 3 households.
        assertEquals(3, sampleHb.getHouseholdList().size());
        // Expect 3 sessions total across households.
        assertEquals(3, sampleHb.getSessionList().size());

        // Verify that the household with ID "H000001" has the session added.
        Household tanFamily = sampleHb.getHouseholdList().stream()
                .filter(h -> h.getId().equals(HouseholdId.fromString("H000001")))
                .findFirst()
                .orElse(null);
        assertNotNull(tanFamily);
        // In the sample sessions, H000001 appears once.
        assertEquals(1, tanFamily.getSessions().size());
    }

    @Test
    public void testGetTagSet() {
        // When duplicate tag strings are passed, the resulting set should contain only unique tags.
        Set<Tag> tags = SampleDataUtil.getTagSet("a", "b", "c", "a");
        assertEquals(3, tags.size());
        // Verify that the set contains the expected tags.
        assertTrue(tags.contains(new Tag("a")));
        assertTrue(tags.contains(new Tag("b")));
        assertTrue(tags.contains(new Tag("c")));
    }
}


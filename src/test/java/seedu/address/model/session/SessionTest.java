package seedu.address.model.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import seedu.address.model.household.HouseholdId;

@ExtendWith(MockitoExtension.class)
class SessionTest {

    private final HouseholdId validHouseholdId = new HouseholdId("H000001");
    private final SessionDate validDate = new SessionDate("2025-03-31");
    private final SessionTime validTime = new SessionTime("14:30");
    private final SessionNote validNote = new SessionNote("Test note");

    @Test
    void constructor_withoutNote_validSession() {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, validHouseholdId, validDate, validTime);
        assertEquals(sessionId, session.getSessionId());
        assertEquals(validHouseholdId, session.getHouseholdId());
        assertEquals(validDate, session.getDate());
        assertEquals(validTime, session.getTime());
        // Session created without note should have null note and hasNote() returns false.
        assertEquals(null, session.getNote());
        assertTrue(!session.hasNote());
    }

    @Test
    void constructor_withNote_validSession() {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, validHouseholdId, validDate, validTime, validNote);
        assertEquals(sessionId, session.getSessionId());
        assertEquals(validHouseholdId, session.getHouseholdId());
        assertEquals(validDate, session.getDate());
        assertEquals(validTime, session.getTime());
        assertEquals(validNote, session.getNote());
        assertTrue(session.hasNote());
    }

    @Test
    void constructor_nullArguments_throwsNullPointerException() {
        String sessionId = UUID.randomUUID().toString();
        // Null sessionId
        assertThrows(NullPointerException.class, (
        ) -> new Session((String) null, validHouseholdId, validDate, validTime));
        // Null householdId
        assertThrows(NullPointerException.class, () -> new Session(sessionId, null, validDate, validTime));
        // Null date
        assertThrows(NullPointerException.class, () -> new Session(sessionId, validHouseholdId, null, validTime));
        // Null time
        assertThrows(NullPointerException.class, () -> new Session(sessionId, validHouseholdId, validDate, null));
    }

    @Test
    void setNote_updatesNote() {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, validHouseholdId, validDate, validTime);
        // Initially, no note.
        assertEquals(null, session.getNote());
        session.setNote(validNote);
        assertEquals(validNote, session.getNote());
    }

    @Test
    void withNote_returnsNewSessionWithNote() {
        String sessionId = UUID.randomUUID().toString();
        Session originalSession = new Session(sessionId, validHouseholdId, validDate, validTime);
        Session sessionWithNote = originalSession.withNote("New note");

        // The new session should have the same sessionId and other details.
        assertEquals(originalSession.getSessionId(), sessionWithNote.getSessionId());
        assertEquals(validHouseholdId, sessionWithNote.getHouseholdId());
        assertEquals(validDate, sessionWithNote.getDate());
        assertEquals(validTime, sessionWithNote.getTime());
        assertTrue(sessionWithNote.hasNote());
        // Verify that the note's text is as provided.
        assertEquals("New note", sessionWithNote.getNote().toString());
    }

    @Test
    void equals_sameSessionId_returnsTrue() {
        String sessionId = UUID.randomUUID().toString();
        // Two sessions created with the same sessionId should be considered equal
        Session session1 = new Session(sessionId, validHouseholdId, validDate, validTime);
        Session session2 = new Session(sessionId, validHouseholdId, validDate, validTime, validNote);
        assertEquals(session1, session2);
    }

    @Test
    void equals_differentSessionId_returnsFalse() {
        // Two sessions created with different sessionIds are not equal.
        Session session1 = new Session(validHouseholdId, validDate, validTime);
        Session session2 = new Session(validHouseholdId, validDate, validTime);
        assertNotEquals(session1, session2);
    }

    @Test
    void toString_correctFormat() {
        // Create a session without a note.
        String sessionId = "test-session-id";
        Session session = new Session(sessionId, validHouseholdId, validDate, validTime);
        String expected = String.format("Session for %s on %s at %s",
                validHouseholdId.toString(), validDate.toString(), validTime.toString());
        assertEquals(expected, session.toString());

        // Now create a session with a note.
        Session sessionWithNote = new Session(sessionId, validHouseholdId, validDate, validTime, validNote);
        String expectedWithNote = String.format("Session for %s on %s at %s: %s",
                validHouseholdId.toString(), validDate.toString(), validTime.toString(), validNote.toString());
        assertEquals(expectedWithNote, sessionWithNote.toString());
    }
}


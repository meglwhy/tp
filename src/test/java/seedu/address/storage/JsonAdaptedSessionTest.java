package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedSession.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalSessions.SESSION_ONE;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;

public class JsonAdaptedSessionTest {
    private static final String INVALID_SESSION_ID = "not-a-uuid";
    private static final String INVALID_HOUSEHOLD_ID = "H12345"; // Missing leading zeros
    private static final String INVALID_DATE = "2025/05/15"; // Wrong format
    private static final String INVALID_TIME = "25:30"; // Invalid hour

    private static final String VALID_SESSION_ID = SESSION_ONE.getSessionId();
    private static final String VALID_HOUSEHOLD_ID = SESSION_ONE.getHouseholdId().toString();
    private static final String VALID_DATE = SESSION_ONE.getDate().toString();
    private static final String VALID_TIME = SESSION_ONE.getTime().toString();
    private static final String VALID_NOTE = SESSION_ONE.getNote().toString();

    @Test
    public void toModelType_validSessionDetails_returnsSession() throws Exception {
        JsonAdaptedSession session = new JsonAdaptedSession(SESSION_ONE);
        assertEquals(SESSION_ONE, session.toModelType());
    }

    @Test
    public void toModelType_invalidSessionId_throwsIllegalValueException() {
        JsonAdaptedSession session =
                new JsonAdaptedSession(INVALID_SESSION_ID, VALID_HOUSEHOLD_ID, VALID_DATE, VALID_TIME, VALID_NOTE);
        String expectedMessage = Session.MESSAGE_CONSTRAINTS_SESSION_ID;
        assertThrows(IllegalValueException.class, expectedMessage, session::toModelType);
    }

    @Test
    public void toModelType_nullSessionId_throwsIllegalValueException() {
        JsonAdaptedSession session =
                new JsonAdaptedSession(null, VALID_HOUSEHOLD_ID, VALID_DATE, VALID_TIME, VALID_NOTE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "SessionId");
        assertThrows(IllegalValueException.class, expectedMessage, session::toModelType);
    }

    @Test
    public void toModelType_invalidHouseholdId_throwsIllegalValueException() {
        JsonAdaptedSession session =
                new JsonAdaptedSession(VALID_SESSION_ID, INVALID_HOUSEHOLD_ID, VALID_DATE, VALID_TIME, VALID_NOTE);
        String expectedMessage = HouseholdId.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, session::toModelType);
    }

    @Test
    public void toModelType_nullHouseholdId_throwsIllegalValueException() {
        JsonAdaptedSession session =
                new JsonAdaptedSession(VALID_SESSION_ID, null, VALID_DATE, VALID_TIME, VALID_NOTE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, HouseholdId.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, session::toModelType);
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedSession session =
                new JsonAdaptedSession(VALID_SESSION_ID, VALID_HOUSEHOLD_ID, INVALID_DATE, VALID_TIME, VALID_NOTE);
        String expectedMessage = SessionDate.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, session::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedSession session =
                new JsonAdaptedSession(VALID_SESSION_ID, VALID_HOUSEHOLD_ID, null, VALID_TIME, VALID_NOTE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, SessionDate.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, session::toModelType);
    }

    @Test
    public void toModelType_invalidTime_throwsIllegalValueException() {
        JsonAdaptedSession session =
                new JsonAdaptedSession(VALID_SESSION_ID, VALID_HOUSEHOLD_ID, VALID_DATE, INVALID_TIME, VALID_NOTE);
        String expectedMessage = SessionTime.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, session::toModelType);
    }

    @Test
    public void toModelType_nullTime_throwsIllegalValueException() {
        JsonAdaptedSession session =
                new JsonAdaptedSession(VALID_SESSION_ID, VALID_HOUSEHOLD_ID, VALID_DATE, null, VALID_NOTE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, SessionTime.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, session::toModelType);
    }

    @Test
    public void toModelType_nullNote_success() throws Exception {
        JsonAdaptedSession session =
                new JsonAdaptedSession(VALID_SESSION_ID, VALID_HOUSEHOLD_ID, VALID_DATE, VALID_TIME, null);

        // Create a session with null note for comparison
        Session expectedSession = new Session(
                UUID.fromString(VALID_SESSION_ID),
                new HouseholdId(VALID_HOUSEHOLD_ID),
                new SessionDate(VALID_DATE),
                new SessionTime(VALID_TIME));

        assertEquals(expectedSession, session.toModelType());
    }
}

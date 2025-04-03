package seedu.address.storage;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionNote;
import seedu.address.model.session.SessionTime;

/**
 * Jackson-friendly version of {@link Session}.
 */
public class JsonAdaptedSession {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Session's %s field is missing!";

    private final String sessionId;
    private final String householdId;
    private final String date;
    private final String time;
    private final String note;

    /**
     * Constructs a {@code JsonAdaptedSession} with the given session details.
     */
    @JsonCreator
    public JsonAdaptedSession(@JsonProperty("sessionId") String sessionId,
                             @JsonProperty("householdId") String householdId,
                             @JsonProperty("date") String date,
                             @JsonProperty("time") String time,
                             @JsonProperty("note") String note) {
        this.sessionId = sessionId;
        this.householdId = householdId;
        this.date = date;
        this.time = time;
        this.note = note;
    }

    /**
     * Converts a given {@code Session} into this class for Jackson use.
     */
    public JsonAdaptedSession(Session source) {
        sessionId = source.getSessionId();
        householdId = source.getHouseholdId().toString();
        date = source.getDate().toString();
        time = source.getTime().toString();
        note = source.getNote() != null ? source.getNote().toString() : null;
    }

    /**
     * Converts this Jackson-friendly adapted session object into the model's {@code Session} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted session.
     */
    public Session toModelType() throws IllegalValueException {
        if (sessionId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "SessionId"));
        }
        try {
            UUID.fromString(sessionId);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(Session.MESSAGE_CONSTRAINTS_SESSION_ID);
        }

        if (householdId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                HouseholdId.class.getSimpleName()));
        }
        if (!HouseholdId.isValidId(householdId)) {
            throw new IllegalValueException(HouseholdId.MESSAGE_CONSTRAINTS);
        }
        final HouseholdId modelHouseholdId = new HouseholdId(householdId);

        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                SessionDate.class.getSimpleName()));
        }
        if (!SessionDate.isValidDate(date)) {
            throw new IllegalValueException(SessionDate.MESSAGE_CONSTRAINTS);
        }
        final SessionDate modelDate = new SessionDate(date);

        if (time == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                SessionTime.class.getSimpleName()));
        }
        if (!SessionTime.isValidTime(time)) {
            throw new IllegalValueException(SessionTime.MESSAGE_CONSTRAINTS);
        }
        final SessionTime modelTime = new SessionTime(time);

        final SessionNote modelNote = note != null ? new SessionNote(note) : null;

        UUID uuid = UUID.fromString(sessionId);
        if (modelNote == null) {
            return new Session(uuid, modelHouseholdId, modelDate, modelTime);
        } else {
            return new Session(uuid, modelHouseholdId, modelDate, modelTime, modelNote);
        }
    }
}

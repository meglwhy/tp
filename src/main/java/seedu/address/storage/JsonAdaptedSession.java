package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.*;

/**
 * Jackson-friendly version of {@link Session}.
 */
class JsonAdaptedSession {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Session's %s field is missing!";

    private final String householdId;
    private final String date;
    private final String time;
    private final String note;

    /**
     * Constructs a {@code JsonAdaptedSession} with the given session details.
     */
    @JsonCreator
    public JsonAdaptedSession(@JsonProperty("householdId") String householdId,
            @JsonProperty("date") String date,
            @JsonProperty("time") String time,
            @JsonProperty("note") String note) {
        this.householdId = householdId;
        this.date = date;
        this.time = time;
        this.note = note;
    }

    /**
     * Converts a given {@code Session} into this class for Jackson use.
     */
    public JsonAdaptedSession(Session source) {
        householdId = source.getHouseholdId().toString();
        date = source.getDate().toString();
        time = source.getTime().toString();
        note = source.hasNote() ? source.getNote().toString() : null;
    }

    /**
     * Converts this Jackson-friendly adapted session object into the model's {@code Session} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted session.
     */
    public Session toModelType() throws IllegalValueException {
        if (householdId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Household ID"));
        }
        if (!HouseholdId.isValidId(householdId)) {
            throw new IllegalValueException(HouseholdId.MESSAGE_CONSTRAINTS);
        }
        final HouseholdId modelHouseholdId = HouseholdId.fromString(householdId);

        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, SessionDate.class.getSimpleName()));
        }
        if (!SessionDate.isValidDate(date)) {
            throw new IllegalValueException(SessionDate.MESSAGE_CONSTRAINTS);
        }
        final SessionDate modelDate = new SessionDate(date);

        if (time == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, SessionTime.class.getSimpleName()));
        }
        if (!SessionTime.isValidTime(time)) {
            throw new IllegalValueException(SessionTime.MESSAGE_CONSTRAINTS);
        }
        final SessionTime modelTime = new SessionTime(time);

        final SessionNote modelNote;
        if (note == null) {
            modelNote = null;
        } else {
            if (!SessionNote.isValidNote(note)) {
                throw new IllegalValueException(SessionNote.MESSAGE_CONSTRAINTS);
            }
            modelNote = new SessionNote(note);
        }

        return new Session(modelHouseholdId, modelDate, modelTime, modelNote);
    }
} 
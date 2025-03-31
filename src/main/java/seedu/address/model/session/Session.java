package seedu.address.model.session;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import seedu.address.model.household.HouseholdId;

/**
 * Represents a Session in the household book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Session {
    // For validation of session ID
    public static final String MESSAGE_CONSTRAINTS_SESSION_ID =
            "Session ID should be a valid UUID format";

    private final String sessionId;
    private final HouseholdId householdId;
    private final SessionDate date;
    private final SessionTime time;
    private SessionNote note;

    /**
     * Creates a Session object without a note.
     */
    public Session(String sessionId, HouseholdId householdId, SessionDate date, SessionTime time) {
        requireNonNull(sessionId);
        requireNonNull(householdId);
        requireNonNull(date);
        requireNonNull(time);
        this.sessionId = sessionId;
        this.householdId = householdId;
        this.date = date;
        this.time = time;
        this.note = null;
    }

    /**
     * Creates a Session object with a note.
     */
    public Session(String sessionId, HouseholdId householdId, SessionDate date, SessionTime time, SessionNote note) {
        this(sessionId, householdId, date, time);
        this.note = note;
    }

    /**
     * Creates a new Session with a generated sessionId without a note.
     */
    public Session(HouseholdId householdId, SessionDate date, SessionTime time) {
        this(UUID.randomUUID().toString(), householdId, date, time);
    }

    /**
     * Creates a new Session with a generated sessionId with a note.
     */
    public Session(HouseholdId householdId, SessionDate date, SessionTime time, SessionNote note) {
        this(UUID.randomUUID().toString(), householdId, date, time, note);
    }

    /**
     * Creates a Session with a specific UUID string for testing purposes.
     */
    public Session(UUID uuid, HouseholdId householdId, SessionDate date, SessionTime time) {
        this(uuid.toString(), householdId, date, time);
    }

    /**
     * Creates a Session with a specific UUID string and note for testing purposes.
     */
    public Session(UUID uuid, HouseholdId householdId, SessionDate date, SessionTime time, SessionNote note) {
        this(uuid.toString(), householdId, date, time, note);
    }

    public String getSessionId() {
        return sessionId; // Not exposed to users, only used internally
    }

    public HouseholdId getHouseholdId() {
        return householdId;
    }

    public SessionDate getDate() {
        return date;
    }

    public SessionTime getTime() {
        return time;
    }

    public SessionNote getNote() {
        return note;
    }

    public void setNote(SessionNote note) {
        this.note = note;
    }

    public boolean hasNote() {
        return note != null;
    }

    /**
     * Returns a copy of this session with the given note.
     */
    public Session withNote(String noteText) {
        return new Session(
                this.sessionId,
                this.householdId,
                this.date,
                this.time,
                new SessionNote(noteText));
    }

    @Override
    public String toString() {
        // Hide sessionId from user output
        return String.format("Session for %s on %s at %s%s",
                householdId.toString(),
                date.toString(),
                time.toString(),
                hasNote() ? ": " + note.toString() : "");
    }

    /**
     * Sessions are uniquely identified by their sessionId.
     * Using date/time for equals can cause unexpected removal or indexing issues
     * if multiple sessions share the same date/time. So we rely on sessionId alone.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Session)) {
            return false;
        }

        Session otherSession = (Session) other;
        return sessionId.equals(otherSession.sessionId);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }
}


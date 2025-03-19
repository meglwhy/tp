package seedu.address.model.session;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import seedu.address.model.household.HouseholdId;

/**
 * Represents a Session in the household book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Session {
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


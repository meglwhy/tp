package seedu.address.model.session;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import seedu.address.model.household.HouseholdId;

/**
 * Represents a session associated with a specific household.
 * Each session is uniquely identified by a {@code sessionId}.
 * A session can optionally have a {@code SessionNote}.
 */
public class Session {
    public static final String MESSAGE_CONSTRAINTS_SESSION_ID =
            "Session ID should be a valid UUID format";

    private final String sessionId;
    private final HouseholdId householdId;
    private final SessionDate date;
    private final SessionTime time;
    private SessionNote note;

    /**
     * Creates a {@code Session} without a note using a specific session ID.
     *
     * @param sessionId Unique identifier for this session.
     * @param householdId The household associated with this session.
     * @param date The date of the session.
     * @param time The time of the session.
     * @throws NullPointerException If any argument is null.
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
     * Creates a {@code Session} with a note using a specific session ID.
     *
     * @param sessionId Unique identifier for this session.
     * @param householdId The household associated with this session.
     * @param date The date of the session.
     * @param time The time of the session.
     * @param note An optional note for this session.
     * @throws NullPointerException If any required argument is null.
     */
    public Session(String sessionId, HouseholdId householdId, SessionDate date, SessionTime time, SessionNote note) {
        this(sessionId, householdId, date, time);
        this.note = note;
    }

    /**
     * Creates a {@code Session} without a note and auto-generates a session ID.
     *
     * @param householdId The household associated with this session.
     * @param date The date of the session.
     * @param time The time of the session.
     * @throws NullPointerException If any argument is null.
     */
    public Session(HouseholdId householdId, SessionDate date, SessionTime time) {
        this(UUID.randomUUID().toString(), householdId, date, time);
    }

    /**
     * Creates a {@code Session} with a note and auto-generates a session ID.
     *
     * @param householdId The household associated with this session.
     * @param date The date of the session.
     * @param time The time of the session.
     * @param note An optional note for this session.
     * @throws NullPointerException If any required argument is null.
     */
    public Session(HouseholdId householdId, SessionDate date, SessionTime time, SessionNote note) {
        this(UUID.randomUUID().toString(), householdId, date, time, note);
    }

    /**
     * Creates a {@code Session} without a note using a specific UUID for testing purposes.
     *
     * @param uuid The UUID used to create the session ID.
     * @param householdId The household associated with this session.
     * @param date The date of the session.
     * @param time The time of the session.
     * @throws NullPointerException If any argument is null.
     */
    public Session(UUID uuid, HouseholdId householdId, SessionDate date, SessionTime time) {
        this(uuid.toString(), householdId, date, time);
    }

    /**
     * Creates a {@code Session} with a note using a specific UUID for testing purposes.
     *
     * @param uuid The UUID used to create the session ID.
     * @param householdId The household associated with this session.
     * @param date The date of the session.
     * @param time The time of the session.
     * @param note An optional note for this session.
     * @throws NullPointerException If any required argument is null.
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
     * Creates a copy of this session with a new note.
     *
     * @param noteText The text of the new note.
     * @return A new session instance with the specified note.
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


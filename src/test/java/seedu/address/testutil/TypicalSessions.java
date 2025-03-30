package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionNote;
import seedu.address.model.session.SessionTime;

/**
 * A utility class containing a list of {@code Session} objects to be used in tests.
 */
public class TypicalSessions {
    public static final Session SESSION_ONE = new Session(
            UUID.fromString("317d2a70-9bb1-41cf-8bd2-5bc0f974cd5f"),
            new HouseholdId("H000001"),
            new SessionDate("2025-03-15"),
            new SessionTime("14:30"),
            new SessionNote("Follow-up on medical assistance application"));

    public static final Session SESSION_TWO = new Session(
            UUID.fromString("417d2a70-9bb1-41cf-8bd2-5bc0f974cd5e"),
            new HouseholdId("H000002"),
            new SessionDate("2025-04-01"),
            new SessionTime("10:00"),
            new SessionNote("Initial assessment"));

    private TypicalSessions() {} // prevents instantiation

    /**
     * Returns a list of all typical sessions.
     */
    public static List<Session> getTypicalSessions() {
        return new ArrayList<>(Arrays.asList(SESSION_ONE, SESSION_TWO));
    }
}

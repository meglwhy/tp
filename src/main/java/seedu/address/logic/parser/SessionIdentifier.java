package seedu.address.logic.parser;

import seedu.address.model.household.HouseholdId;

/**
 * Represents a parsed session identifier that contains a household ID and a session index.
 *
 * This object is used to encapsulate the two pieces of information extracted from a session identifier string,
 * which is expected to be in the format: {@code [id/]<HOUSEHOLD_ID>-<SESSION_INDEX>}.
 *
 */
public class SessionIdentifier {
    private final HouseholdId householdId;
    private final int sessionIndex;


    /**
     * Constructs a {@code SessionIdentifier} with the specified household ID and session index.
     *
     * @param householdId  The household identifier.
     * @param sessionIndex The session index.
     */
    public SessionIdentifier(HouseholdId householdId, int sessionIndex) {
        this.householdId = householdId;
        this.sessionIndex = sessionIndex;
    }

    public HouseholdId getHouseholdId() {
        return householdId;
    }

    public int getSessionIndex() {
        return sessionIndex;
    }
}

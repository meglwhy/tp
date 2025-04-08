package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.model.household.HouseholdId;



/**
 * Tests for the SessionIdentifier data object.
 */
public class SessionIdentifierTest {

    @Test
    public void sessionIdentifier_getters_returnCorrectValues() {
        HouseholdId householdId = HouseholdId.fromString("H000001");
        int index = 5;
        SessionIdentifier identifier = new SessionIdentifier(householdId, index);
        // Verify that the getters return the expected values.
        assertEquals(householdId, identifier.getHouseholdId());
        assertEquals(index, identifier.getSessionIndex());
    }
}


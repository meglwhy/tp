package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

/**
 * Tests for the SessionParserUtil class.
 */
public class SessionParserUtilTest {

    @Test
    public void parseSessionIdentifierValidInputWithIdPrefixSuccess() throws Exception {
        String input = "id/H000007-2";
        SessionIdentifier identifier = SessionParserUtil.parseSessionIdentifier(input);
        // Verify that the householdId is parsed correctly.
        assertEquals(HouseholdId.fromString("H000007"), identifier.getHouseholdId());
        // Verify that the session index is parsed correctly.
        assertEquals(2, identifier.getSessionIndex());
    }

    @Test
    public void parseSessionIdentifierValidInputWithoutIdPrefixSuccess() throws Exception {
        String input = "H000007-3";
        SessionIdentifier identifier = SessionParserUtil.parseSessionIdentifier(input);
        assertEquals(HouseholdId.fromString("H000007"), identifier.getHouseholdId());
        assertEquals(3, identifier.getSessionIndex());
    }

    @Test
    public void parseSessionIdentifier_missingHyphen_throwsParseException() {
        // A hyphen is required to separate household id and session index.
        String input = "id/H0000072";
        assertThrows(ParseException.class, () -> SessionParserUtil.parseSessionIdentifier(input));
    }

    @Test
    public void parseSessionIdentifier_insufficientParts_throwsParseException() {
        // If the session identifier is missing the session index part, it should throw.
        String input = "id/H000007-";
        assertThrows(ParseException.class, () -> SessionParserUtil.parseSessionIdentifier(input));
    }

    @Test
    public void parseSessionIdentifier_invalidSessionIndex_throwsParseException() {
        // Non-numeric session index should trigger ParseException.
        String input = "id/H000007-abc";
        assertThrows(ParseException.class, () -> SessionParserUtil.parseSessionIdentifier(input));
    }

    //add parse test
    @Test
    public void parseSessionIdentifier_invalidHouseholdId_throwsParseException() {
        // An invalid household id should trigger a ParseException.
        // Adjust the invalid value based on your actual HouseholdId constraints.
        String input = "id/INVALID-1";
        assertThrows(ParseException.class, () -> SessionParserUtil.parseSessionIdentifier(input));
    }
}


package seedu.address.logic.parser;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

/**
 * Utility class for parsing session identifier strings into {@link SessionIdentifier} objects.
 *
 * The expected input format is a string optionally prefixed with {@code "id/"} followed by a household ID,
 * a hyphen, and a session index (e.g. {@code "id/H000007-2"}).
 *
 */
public class SessionParserUtil {

    private static final String MESSAGE_INVALID_FORMAT = "Invalid format! Usage: id/<HOUSEHOLD_ID-SESSION_INDEX>";

    /**
     * Parses a session identifier string and returns a {@code SessionIdentifier} containing
     * the HouseholdId and session index.
     *
     * @param input The session identifier string (may optionally start with "id/").
     * @return A {@code SessionIdentifier} with the parsed household id and session index.
     * @throws ParseException If the format is invalid.
     */
    public static SessionIdentifier parseSessionIdentifier(String input) throws ParseException {
        // Remove the "id/" prefix if it exists.
        if (input.startsWith("id/")) {
            input = input.substring(3).trim();
        }
        if (!input.contains("-")) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }
        String[] parts = input.split("-", 2);
        if (parts.length < 2) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }
        String householdIdStr = parts[0].trim();
        String sessionIndexStr = parts[1].trim();

        if (!HouseholdId.isValidId(householdIdStr)) {
            throw new ParseException("Invalid household ID: " + householdIdStr);
        }
        HouseholdId householdId = HouseholdId.fromString(householdIdStr);

        int sessionIndex;
        try {
            sessionIndex = Integer.parseInt(sessionIndexStr);
        } catch (NumberFormatException e) {
            throw new ParseException("Session index must be an integer: " + sessionIndexStr);
        }
        return new SessionIdentifier(householdId, sessionIndex);
    }
}


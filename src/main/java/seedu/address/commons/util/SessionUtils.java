package seedu.address.commons.util;

import java.time.LocalDate;
import java.time.LocalTime;

import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;

/**
 * Utility class containing helper methods for session-related operations.
 */
public class SessionUtils {
    /**
     * Checks if the given session date and time are in the past.
     * A session is considered to be in the past if the session's date is before the current date,
     * or if the session's date is today and the session's time is before the current time.
     *
     * @param sessionDate The session's date to check. Must not be null.
     * @param sessionTime The session's time to check. Must not be null.
     * @return {@code true} if the session's date and time are in the past, {@code false} otherwise.
     */
    public static boolean isPastDateTime(SessionDate sessionDate, SessionTime sessionTime) {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (sessionDate.value.isBefore(today)) {
            return true;
        } else if (sessionDate.value.isEqual(today)) {
            return sessionTime.value.isBefore(currentTime);
        }
        return false;
    }
}

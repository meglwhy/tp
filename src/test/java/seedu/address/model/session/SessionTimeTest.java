package seedu.address.model.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionTimeTest {

    @Test
    public void constructor_validTime_success() {
        String validTimeString = "14:30";
        SessionTime sessionTime = new SessionTime(validTimeString);
        // Verify that the time is correctly parsed
        assertEquals(LocalTime.parse(validTimeString, SessionTime.FORMATTER), sessionTime.value);
        // toString() should return the time in the correct format
        assertEquals(validTimeString, sessionTime.toString());
    }

    @Test
    public void constructor_invalidTime_throwsException() {
        // Invalid hour (25:00 is not a valid time)
        String invalidTime = "25:00";
        assertThrows(IllegalArgumentException.class, () -> new SessionTime(invalidTime));

        // A time in an incorrect format should fail.
        String invalidFormat = "2:30 PM";
        assertThrows(IllegalArgumentException.class, () -> new SessionTime(invalidFormat));
    }

    @Test
    public void equalsAndHashCode_sameTimes_equal() {
        String timeStr = "14:30";
        SessionTime time1 = new SessionTime(timeStr);
        SessionTime time2 = new SessionTime(timeStr);
        assertEquals(time1, time2);
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void compareTo_returnsCorrectOrder() {
        SessionTime time1 = new SessionTime("14:30");
        SessionTime time2 = new SessionTime("15:30");
        // time1 should be earlier than time2
        assertTrue(time1.compareTo(time2) < 0);
    }
}


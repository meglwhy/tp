package seedu.address.model.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionDateTest {

    @Test
    public void constructor_validDate_success() {
        String validDateString = "2025-03-31";
        SessionDate sessionDate = new SessionDate(validDateString);
        // Check that the date is correctly parsed
        assertEquals(LocalDate.parse(validDateString), sessionDate.value);
        // toString should return the same format as input
        assertEquals(validDateString, sessionDate.toString());
    }

    @Test
    public void constructor_invalidDate_throwsException() {
        // An impossible date should fail (e.g., February 30th)
        String invalidDateString = "2025-02-30";
        assertThrows(IllegalArgumentException.class, () -> new SessionDate(invalidDateString));

        // A date in an invalid format should also fail.
        String invalidFormat = "31-03-2025";
        assertThrows(IllegalArgumentException.class, () -> new SessionDate(invalidFormat));
    }

    @Test
    public void equalsAndHashCode_sameDates_equal() {
        String dateStr = "2025-03-31";
        SessionDate date1 = new SessionDate(dateStr);
        SessionDate date2 = new SessionDate(dateStr);
        assertEquals(date1, date2);
        assertEquals(date1.hashCode(), date2.hashCode());
    }

    @Test
    public void compareTo_returnsCorrectOrder() {
        SessionDate date1 = new SessionDate("2025-03-31");
        SessionDate date2 = new SessionDate("2025-04-01");
        // date1 should be before date2
        assertTrue(date1.compareTo(date2) < 0);
    }
}


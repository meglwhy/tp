package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AssertionTest {
    @Test
    void testAssertionsEnabled() {
        int x = -1;
        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            assert x >= 0 : "x should not be negative";
        });

        assertEquals("x should not be negative", thrown.getMessage());
    }
}

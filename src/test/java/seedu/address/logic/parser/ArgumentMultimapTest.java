package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class ArgumentMultimapTest {

    @Test
    void testPutAndGetValue() {
        ArgumentMultimap multimap = new ArgumentMultimap();
        Prefix prefix = new Prefix("p/");

        // Initially, getValue should be empty.
        assertEquals(Optional.empty(), multimap.getValue(prefix));

        // Put a value and verify getValue returns it.
        multimap.put(prefix, "firstValue");
        assertEquals(Optional.of("firstValue"), multimap.getValue(prefix));
    }

    @Test
    void testMultiplePutForSamePrefix() {
        ArgumentMultimap multimap = new ArgumentMultimap();
        Prefix prefix = new Prefix("p/");

        // Put several values for the same prefix.
        multimap.put(prefix, "first");
        multimap.put(prefix, "second");
        multimap.put(prefix, "third");

        // getValue should return the last value.
        assertEquals(Optional.of("third"), multimap.getValue(prefix));

        // getAllValues should return all values in insertion order.
        List<String> values = multimap.getAllValues(prefix);
        assertEquals(3, values.size());
        assertEquals("first", values.get(0));
        assertEquals("second", values.get(1));
        assertEquals("third", values.get(2));
    }

    @Test
    void testGetAllValuesReturnsCopy() {
        ArgumentMultimap multimap = new ArgumentMultimap();
        Prefix prefix = new Prefix("p/");
        multimap.put(prefix, "firstValue");

        // Retrieve the list and modify it.
        List<String> values = multimap.getAllValues(prefix);
        values.add("manipulatedValue");

        // The internal state should not be affected.
        List<String> internalValues = multimap.getAllValues(prefix);
        assertFalse(internalValues.contains("manipulatedValue"));
        assertEquals(1, internalValues.size());
    }

    @Test
    void testGetPreamble() {
        ArgumentMultimap multimap = new ArgumentMultimap();
        Prefix emptyPrefix = new Prefix("");

        // If no preamble is set, getPreamble returns an empty string.
        assertEquals("", multimap.getPreamble());

        // Set the preamble.
        multimap.put(emptyPrefix, "Initial preamble");
        assertEquals("Initial preamble", multimap.getPreamble());

        // When multiple preamble values are added, the last one is returned.
        multimap.put(emptyPrefix, "Final preamble");
        assertEquals("Final preamble", multimap.getPreamble());
    }

    @Test
    void testArePrefixesPresent() {
        ArgumentMultimap multimap = new ArgumentMultimap();
        Prefix prefix1 = new Prefix("p1/");
        Prefix prefix2 = new Prefix("p2/");
        Prefix prefix3 = new Prefix("p3/");

        // No prefixes set: should return false.
        assertFalse(multimap.arePrefixesPresent(prefix1, prefix2));

        // Add values for prefix1 and prefix2.
        multimap.put(prefix1, "value1");
        multimap.put(prefix2, "value2");
        assertTrue(multimap.arePrefixesPresent(prefix1, prefix2));

        // Since prefix3 is not present, arePrefixesPresent should return false.
        assertFalse(multimap.arePrefixesPresent(prefix1, prefix2, prefix3));
    }
}

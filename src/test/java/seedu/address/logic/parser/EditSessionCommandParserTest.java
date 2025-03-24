package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

public class EditSessionCommandParserTest {

    private EditSessionCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new EditSessionCommandParser();
    }

    /**
     * Helper method to retrieve a private field value via reflection.
     */
    private <T> T getField(Object obj, String fieldName, Class<T> clazz) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return clazz.cast(field.get(obj));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void parse_validInputWithoutNote_returnsEditSessionCommand() throws ParseException {
        // Include a leading space so that the preamble is empty.
        String userInput = " id/H000007-2 d/2025-09-27 tm/19:00";
        HouseholdId expectedHouseholdId = new HouseholdId("H000007");
        int expectedSessionIndex = 2;
        String expectedDate = "2025-09-27";
        String expectedTime = "19:00";

        EditSessionCommand command = parser.parse(userInput);

        HouseholdId actualHouseholdId = getField(command, "householdId", HouseholdId.class);
        Integer actualSessionIndex = getField(command, "sessionIndex", Integer.class);
        String actualNewDate = getField(command, "newDate", String.class);
        String actualNewTime = getField(command, "newTime", String.class);
        Boolean actualHasNote = getField(command, "hasNote", Boolean.class);

        assertEquals(expectedHouseholdId, actualHouseholdId);
        assertEquals(expectedSessionIndex, actualSessionIndex.intValue());
        assertEquals(expectedDate, actualNewDate);
        assertEquals(expectedTime, actualNewTime);
        assertEquals(false, actualHasNote);
    }

    @Test
    public void parse_validInputWithNote_returnsEditSessionCommand() throws ParseException {
        // Include a leading space so that the preamble is empty.
        String userInput = " id/H000007-2 d/2025-09-27 tm/19:00 n/Follow-up";
        HouseholdId expectedHouseholdId = new HouseholdId("H000007");
        int expectedSessionIndex = 2;
        String expectedDate = "2025-09-27";
        String expectedTime = "19:00";
        String expectedNote = "Follow-up";

        EditSessionCommand command = parser.parse(userInput);

        HouseholdId actualHouseholdId = getField(command, "householdId", HouseholdId.class);
        Integer actualSessionIndex = getField(command, "sessionIndex", Integer.class);
        String actualNewDate = getField(command, "newDate", String.class);
        String actualNewTime = getField(command, "newTime", String.class);
        String actualNote = getField(command, "note", String.class);
        Boolean actualHasNote = getField(command, "hasNote", Boolean.class);

        assertEquals(expectedHouseholdId, actualHouseholdId);
        assertEquals(expectedSessionIndex, actualSessionIndex.intValue());
        assertEquals(expectedDate, actualNewDate);
        assertEquals(expectedTime, actualNewTime);
        assertEquals(expectedNote, actualNote);
        assertEquals(true, actualHasNote);
    }

    @Test
    public void parse_invalidFormat_throwsParseException() {
        // Missing "id/" prefix causes a non-empty preamble.
        String userInput = "H000007-2 d/2025-09-27 tm/19:00";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}






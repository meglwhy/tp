package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

public class DeleteSessionCommandParserTest {

    private DeleteSessionCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new DeleteSessionCommandParser();
    }

    /**
     * Helper method to retrieve a private field value via reflection.
     */
    private <T> T getField(Object obj, String fieldName, Class<T> clazz) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return clazz.cast(field.get(obj));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void parse_validInput_returnsDeleteSessionCommand() throws ParseException {
        String userInput = " id/H000001-2";
        HouseholdId expectedHouseholdId = new HouseholdId("H000001");
        int expectedSessionIndex = 2;

        DeleteSessionCommand resultCommand = parser.parse(userInput);
        assertEquals(expectedHouseholdId, getField(resultCommand, "householdId", HouseholdId.class));
        assertEquals(expectedSessionIndex,
                getField(resultCommand, "sessionIndex", Integer.class).intValue());
    }

    @Test
    public void parse_invalidFormat_throwsParseException() {
        // Missing hyphen.
        String userInput = " id/H0000012";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidSessionIndex_throwsParseException() {
        // Non-integer session index.
        String userInput = " id/H000001-abc";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}


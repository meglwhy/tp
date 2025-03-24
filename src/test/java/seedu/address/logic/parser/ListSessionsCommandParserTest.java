package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListSessionsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

public class ListSessionsCommandParserTest {

    private ListSessionsCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new ListSessionsCommandParser();
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
    public void parse_validInput_returnsListSessionsCommand() throws ParseException {
        String userInput = "id/H000003";
        HouseholdId expectedHouseholdId = new HouseholdId("H000003");
        ListSessionsCommand command = parser.parse(userInput);
        HouseholdId actualHouseholdId = getField(command, "householdId", HouseholdId.class);
        assertEquals(expectedHouseholdId, actualHouseholdId);
    }

    @Test
    public void parse_invalidInput_throwsParseException() {
        // Missing "id/" prefix.
        String userInput = "H000003";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}


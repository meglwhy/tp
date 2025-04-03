package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewHouseholdSessionsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

public class ViewHouseholdSessionsCommandParserTest {

    private ViewHouseholdSessionsCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new ViewHouseholdSessionsCommandParser();
    }

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
    public void parse_validInput_returnsViewHouseholdSessionsCommand() throws ParseException {
        String userInput = "id/H000007";
        HouseholdId expectedHouseholdId = new HouseholdId("H000007");
        ViewHouseholdSessionsCommand command = parser.parse(userInput);
        // The field name in the command is "targetHouseholdId"
        HouseholdId actualHouseholdId = getField(command, "targetHouseholdId", HouseholdId.class);
        assertEquals(expectedHouseholdId, actualHouseholdId);
    }

    @Test
    public void parse_invalidInput_throwsParseException() {
        String userInput = "";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}


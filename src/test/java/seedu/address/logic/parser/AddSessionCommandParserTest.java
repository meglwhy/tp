package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;

public class AddSessionCommandParserTest {

    private AddSessionCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new AddSessionCommandParser();
    }

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
    public void parse_validInput_returnsAddSessionCommand() throws ParseException {
        String userInput = " id/H000001 d/2025-12-31 tm/15:00";
        HouseholdId expectedHouseholdId = new HouseholdId("H000001");
        SessionDate expectedDate = new SessionDate("2025-12-31");
        SessionTime expectedTime = new SessionTime("15:00");

        AddSessionCommand resultCommand = parser.parse(userInput);
        // Retrieve and verify the householdId stored directly in the command.
        HouseholdId resultHouseholdId = getField(resultCommand, "householdId", HouseholdId.class);
        assertEquals(expectedHouseholdId, resultHouseholdId);

        // Retrieve the internal Session object (stored in the "toAdd" field).
        Session session = getField(resultCommand, "toAdd", Session.class);
        assertEquals(expectedHouseholdId, session.getHouseholdId());
        assertEquals(expectedDate, session.getDate());
        assertEquals(expectedTime, session.getTime());
    }

    @Test
    public void parse_missingTime_throwsParseException() {
        String userInput = " id/H000001 d/2025-12-31";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingId_throwsParseException() {
        String userInput = " d/2025-12-31 tm/15:00";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_extraParameters_throwsParseException() {
        String userInput = " id/H000001 d/2025-12-31 tm/15:00 extraParam";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}






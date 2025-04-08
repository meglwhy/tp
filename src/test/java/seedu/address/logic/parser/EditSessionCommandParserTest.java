package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.EditSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;

public class EditSessionCommandParserTest {

    private Model model;
    private HouseholdBook householdBook;
    private Household household;
    private HouseholdId validHouseholdId;
    private ObservableList<Session> sessionList;

    private EditSessionCommandParser parser;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        householdBook = mock(HouseholdBook.class);
        household = mock(Household.class);
        validHouseholdId = new HouseholdId("H000001");

        parser = new EditSessionCommandParser(model);

        // Stub the model to return our mocked householdBook.
        lenient().when(model.getHouseholdBook()).thenReturn(householdBook);
        // By default, the household is found.
        lenient().when(householdBook.getHouseholdById(eq(validHouseholdId)))
                .thenReturn(java.util.Optional.of(household));
        // For valid session index, assume the household has one session.
        sessionList = FXCollections.observableArrayList();
        Session dummySession = mock(Session.class);
        sessionList.add(dummySession);
        lenient().when(household.getSessions()).thenReturn(sessionList);
    }

    @Test
    void parse_missingIdPrefix_throwsParseException() {
        // Missing the required id/ prefix.
        String userInput = "d/2025-03-16 tm/15:00 n/Edited note";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_nonEmptyPreamble_throwsParseException() {
        // Extra text before the valid prefix results in a non-empty preamble.
        String userInput = "extraText id/H000001-1 d/2025-03-16 tm/15:00 n/Edited note";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_noEditFieldsProvided_throwsParseException() {
        // At least one edit field (date, time, note) must be provided.
        String userInput = "id/H000001-1";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_validInputAllFieldsProvided_success() throws Exception {
        // This test simulates the case shown in your screenshot:
        // a valid input with date, time, and note provided.
        // Note: A leading space is added so that the preamble is empty.
        String userInput = " id/H000001-1 d/2025-03-16 tm/15:00 n/Edited note";
        EditSessionCommand command = parser.parse(userInput);
        assertNotNull(command);

        // Use reflection to verify that the returned command has the expected field values.
        Field householdIdField = EditSessionCommand.class.getDeclaredField("householdId");
        householdIdField.setAccessible(true);
        HouseholdId parsedHouseholdId = (HouseholdId) householdIdField.get(command);
        assertEquals(validHouseholdId, parsedHouseholdId);

        Field sessionIndexField = EditSessionCommand.class.getDeclaredField("sessionIndex");
        sessionIndexField.setAccessible(true);
        int sessionIndex = sessionIndexField.getInt(command);
        assertEquals(1, sessionIndex);

        Field dateField = EditSessionCommand.class.getDeclaredField("newDate");
        dateField.setAccessible(true);
        String dateValue = (String) dateField.get(command);
        // Assuming ParserUtil.parseDate("2025-03-16").toString() returns "2025-03-16"
        assertEquals("2025-03-16", dateValue);

        Field timeField = EditSessionCommand.class.getDeclaredField("newTime");
        timeField.setAccessible(true);
        String timeValue = (String) timeField.get(command);
        // Assuming ParserUtil.parseTime("15:00").toString() returns "15:00"
        assertEquals("15:00", timeValue);

        Field noteField = EditSessionCommand.class.getDeclaredField("newNote");
        noteField.setAccessible(true);
        String noteValue = (String) noteField.get(command);
        assertEquals("Edited note", noteValue);
    }

    @Test
    void parse_validInputOnlyNoteProvided_success() throws Exception {
        // This test covers the branch where only the note is provided as an edit field.
        // The absence of date and time should be acceptable as long as one field is provided.
        String userInput = " id/H000001-1 n/Just a note update";
        EditSessionCommand command = parser.parse(userInput);
        assertNotNull(command);

        // Verify that newDate and newTime remain null while newNote is set.
        Field dateField = EditSessionCommand.class.getDeclaredField("newDate");
        dateField.setAccessible(true);
        String dateValue = (String) dateField.get(command);
        assertEquals(null, dateValue);

        Field timeField = EditSessionCommand.class.getDeclaredField("newTime");
        timeField.setAccessible(true);
        String timeValue = (String) timeField.get(command);
        assertEquals(null, timeValue);

        Field noteField = EditSessionCommand.class.getDeclaredField("newNote");
        noteField.setAccessible(true);
        String noteValue = (String) noteField.get(command);
        assertEquals("Just a note update", noteValue);
    }

    @Test
    void parse_emptyNoteProvided_throwsParseException() {
        // If a note prefix is provided but the note is an empty string (or only whitespace),
        // the parser should throw a ParseException.
        String userInput = " id/H000001-1 n/   ";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}






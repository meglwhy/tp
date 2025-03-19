package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteHouseholdCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

class DeleteHouseholdCommandParserTest {

    private DeleteHouseholdCommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new DeleteHouseholdCommandParser();
    }

    @Test
    void parse_validArgs_returnsDeleteHouseholdCommand() throws ParseException {
        // Valid input with household id
        String userInput = " " + PREFIX_ID + "H000001";

        HouseholdId expectedHouseholdId = new HouseholdId("H000001");
        DeleteHouseholdCommand expectedCommand = new DeleteHouseholdCommand(expectedHouseholdId);

        DeleteHouseholdCommand resultCommand = parser.parse(userInput);

        assertEquals(expectedCommand, resultCommand);
    }

    @Test
    void parse_missingId_throwsParseException() {
        // Input with missing household ID
        String userInput = " " + PREFIX_ID;

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_extraParameters_throwsParseException() {
        // Input with extra parameters (should be invalid)
        String userInput = " " + PREFIX_ID + "H000001 extraParam";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_emptyArgs_throwsParseException() {
        // Empty input, should throw ParseException
        String userInput = "";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_invalidHouseholdId_throwsParseException() {
        // Invalid household ID format
        String userInput = " " + PREFIX_ID + "invalid-id";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

}

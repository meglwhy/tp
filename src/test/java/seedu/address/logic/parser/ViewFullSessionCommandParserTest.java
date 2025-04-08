package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewFullSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;


public class ViewFullSessionCommandParserTest {

    private final ViewFullSessionCommandParser parser = new ViewFullSessionCommandParser();


    @Test
    public void parse_missingIdPrefix_throwsParseException() {
        // Without the required "id/" prefix, the parser should throw a ParseException.
        String userInput = "H000001-2";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(userInput));
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ViewFullSessionCommand.MESSAGE_USAGE);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void parse_invalidSessionIdentifier_throwsParseException() {
        // The value after "id/" is not in the expected format (e.g. "invalid-format").
        String userInput = "id/ invalid-format";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}

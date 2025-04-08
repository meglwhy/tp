package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;


public class DeleteSessionCommandParserTest {

    private final DeleteSessionCommandParser parser = new DeleteSessionCommandParser();


    @Test
    public void parse_missingPrefix_throwsParseException() {
        // Missing the "id/" prefix.
        String userInput = "H000001-2";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        // Input has extra text before the valid prefix.
        String userInput = "extraText id/H000001-2";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_emptyIdArgument_throwsParseException() {
        // "id/" is provided but with no argument (or only whitespace).
        String userInput = "id/   ";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidSessionIdentifier_throwsParseException() {
        // The identifier is not in the expected format, so the helper should throw ParseException.
        String userInput = "id/invalid-format";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}

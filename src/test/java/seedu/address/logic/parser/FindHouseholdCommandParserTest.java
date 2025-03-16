package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindHouseholdCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

class FindHouseholdCommandParserTest {

    private FindHouseholdCommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new FindHouseholdCommandParser();
    }

    @Test
    void parse_validArgs_returnsFindHouseholdCommand() throws ParseException, CommandException {
        // Valid input with multiple keywords
        String userInput = "John Doe Main St";
        FindHouseholdCommand expectedCommand = new FindHouseholdCommand("John Doe Main St");

        // Test the command creation without throwing CommandException
        FindHouseholdCommand resultCommand = parser.parse(userInput);
        assertEquals(expectedCommand, resultCommand);
    }

    @Test
    void parse_singleKeyword_returnsFindHouseholdCommand() throws ParseException, CommandException {
        // Valid input with a single keyword
        String userInput = "John";
        FindHouseholdCommand expectedCommand = new FindHouseholdCommand("John");

        // Test the command creation without throwing CommandException
        FindHouseholdCommand resultCommand = parser.parse(userInput);
        assertEquals(expectedCommand, resultCommand);
    }

    @Test
    void parse_emptyArgs_throwsParseException() {
        // Empty input should throw a ParseException
        String userInput = "";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_whitespaceOnly_throwsParseException() {
        // Input containing only spaces should throw a ParseException
        String userInput = "   ";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_invalidFormat_throwsParseException() {
        // Simulate invalid input (e.g., null) should result in a ParseException
        String userInput = null;
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_trailingWhitespace_returnsFindHouseholdCommand() throws ParseException, CommandException {
        // Input with leading and trailing whitespace should still be valid
        String userInput = "   John Doe Main St   ";
        FindHouseholdCommand expectedCommand = new FindHouseholdCommand("John Doe Main St");

        // Test the command creation without throwing CommandException
        FindHouseholdCommand resultCommand = parser.parse(userInput);
        assertEquals(expectedCommand, resultCommand);
    }

    @Test
    void parse_multipleSpacesBetweenKeywords_returnsFindHouseholdCommand() throws ParseException, CommandException {
        // Input with multiple spaces between keywords should still be valid
        String userInput = "John   Doe   Main   St";
        FindHouseholdCommand expectedCommand = new FindHouseholdCommand("John Doe Main St");

        // Test the command creation without throwing CommandException
        FindHouseholdCommand resultCommand = parser.parse(userInput);
        assertEquals(expectedCommand, resultCommand);
    }
}

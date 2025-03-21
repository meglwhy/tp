package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindHouseholdCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

public class FindHouseholdCommandParserTest {

    private final FindHouseholdCommandParser parser = new FindHouseholdCommandParser();

    @Test
    public void parse_validInput_returnsFindHouseholdCommand() throws ParseException {
        // Arrange
        String userInput = "John Family";

        // Act
        FindHouseholdCommand result = parser.parse(userInput);

        // Assert
        try {
            assertEquals(new FindHouseholdCommand("John Family"), result);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        // Arrange
        String userInput = "";

        // Act & Assert
        ParseException thrown = assertThrows(ParseException.class, () -> {
            parser.parse(userInput);
        });
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindHouseholdCommand.MESSAGE_USAGE),
                thrown.getMessage());
    }

    @Test
    public void parse_multipleSpacesBetweenKeywords_returnsFindHouseholdCommand() throws ParseException {
        // Arrange
        String userInput = "  John    Family  ";

        // Act
        FindHouseholdCommand result = parser.parse(userInput);

        // Assert
        try {
            assertEquals(new FindHouseholdCommand("John Family"), result);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void parse_validInputWithMultipleKeywords_returnsFindHouseholdCommand() throws ParseException {
        // Arrange
        String userInput = "John Family Friends";

        // Act
        FindHouseholdCommand result = parser.parse(userInput);

        // Assert
        try {
            assertEquals(new FindHouseholdCommand("John Family Friends"), result);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void parse_invalidInput_throwsParseException() {
        // Arrange
        String userInput = "!!!";

        // Act & Assert
        ParseException thrown = assertThrows(ParseException.class, () -> {
            parser.parse(userInput);
        });
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindHouseholdCommand.MESSAGE_USAGE),
                thrown.getMessage());
    }

    @Test
    public void parse_inputWithExtraSpacesBeforeAndAfter_keywordsAreTrimmed() throws ParseException {
        // Arrange
        String userInput = "   John   Family   ";

        // Act
        FindHouseholdCommand result = parser.parse(userInput);

        // Assert
        try {
            assertEquals(new FindHouseholdCommand("John Family"), result);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }
}

package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddHouseholdCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.Name;

class AddHouseholdCommandParserTest {

    private AddHouseholdCommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new AddHouseholdCommandParser();
    }

    // Test 1: Valid Input
    @Test
    void parse_validArgs_success() throws ParseException {
        String args = "n/Smith Family a/123 Main St p/98765432";

        AddHouseholdCommand command = parser.parse(args);
        Household expectedHousehold = new Household(
                new Name("Smith Family"),
                new Address("123 Main St"),
                new Contact("98765432")
        );

        // Verify that the command contains the correct household
        assertEquals(expectedHousehold, command.getHousehold());
    }

    // Test 2: Missing Name Prefix
    @Test
    void parse_missingNamePrefix_throwsParseException() {
        String args = "a/123 Main St p/98765432"; // Missing name prefix

        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(args));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHouseholdCommand.MESSAGE_USAGE),
                exception.getMessage());
    }

    // Test 3: Missing Address Prefix
    @Test
    void parse_missingAddressPrefix_throwsParseException() {
        String args = "n/Smith Family p/98765432"; // Missing address prefix

        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(args));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHouseholdCommand.MESSAGE_USAGE),
                exception.getMessage());
    }

    // Test 4: Missing Phone Prefix
    @Test
    void parse_missingPhonePrefix_throwsParseException() {
        String args = "n/Smith Family a/123 Main St"; // Missing phone prefix

        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(args));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHouseholdCommand.MESSAGE_USAGE),
                exception.getMessage());
    }

    // Test 5: Extra Arguments
    @Test
    void parse_extraArguments_throwsParseException() {
        String args = "n/Smith Family a/123 Main St p/98765432 extra/argument"; // Extra argument

        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(args));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHouseholdCommand.MESSAGE_USAGE),
                exception.getMessage());
    }

    // Test 6: Invalid Phone Number Format
    @Test
    void parse_invalidContact_throwsParseException() {
        String args = "n/Smith Family a/123 Main St p/invalidPhoneNumber"; // Invalid phone number format

        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(args));
        assertEquals("Invalid contact number", exception.getMessage());
    }

    // Test 7: Empty Input
    @Test
    void parse_emptyInput_throwsParseException() {
        String args = ""; // Empty input

        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(args));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHouseholdCommand.MESSAGE_USAGE),
                exception.getMessage());
    }

    // Test 8: Only Whitespaces
    @Test
    void parse_onlyWhitespaces_throwsParseException() {
        String args = "     "; // Only whitespaces

        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(args));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHouseholdCommand.MESSAGE_USAGE),
                exception.getMessage());
    }

    // Test 9: Name with Extra Spaces
    @Test
    void parse_nameWithExtraSpaces_success() throws ParseException {
        String args = "n/  Smith Family  a/123 Main St p/98765432"; // Name with extra spaces

        AddHouseholdCommand command = parser.parse(args);
        Household expectedHousehold = new Household(
                new Name("Smith Family"),
                new Address("123 Main St"),
                new Contact("98765432")
        );

        // Verify that the command contains the correct household
        assertEquals(expectedHousehold, command.getHousehold());
    }

    // Test 10: Address with Extra Spaces
    @Test
    void parse_addressWithExtraSpaces_success() throws ParseException {
        String args = "n/Smith Family a/ 123 Main St  p/98765432"; // Address with extra spaces

        AddHouseholdCommand command = parser.parse(args);
        Household expectedHousehold = new Household(
                new Name("Smith Family"),
                new Address("123 Main St"),
                new Contact("98765432")
        );

        // Verify that the command contains the correct household
        assertEquals(expectedHousehold, command.getHousehold());
    }

    // Test 11: Phone with Extra Spaces
    @Test
    void parse_phoneWithExtraSpaces_success() throws ParseException {
        String args = "n/Smith Family a/123 Main St p/ 98765432  "; // Phone with extra spaces

        AddHouseholdCommand command = parser.parse(args);
        Household expectedHousehold = new Household(
                new Name("Smith Family"),
                new Address("123 Main St"),
                new Contact("98765432")
        );

        // Verify that the command contains the correct household
        assertEquals(expectedHousehold, command.getHousehold());
    }
}

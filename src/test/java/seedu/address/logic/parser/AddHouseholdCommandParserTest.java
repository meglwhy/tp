package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddHouseholdCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.Name;

class AddHouseholdCommandParserTest {
    private final AddHouseholdCommandParser parser = new AddHouseholdCommandParser();

    @Test
    void parse_validArgs_returnsAddHouseholdCommand() throws ParseException {
        String userInput = " " + PREFIX_NAME + "John Doe " + PREFIX_ADDRESS + "123 Main St "
                + PREFIX_PHONE + "98765432";
        AddHouseholdCommand expectedCommand = new AddHouseholdCommand(
                new Household(new Name("John Doe"), new Address("123 Main St"), new Contact("98765432")));

        assertEquals(expectedCommand, parser.parse(userInput));
    }

    @Test
    void parse_missingName_throwsParseException() {
        String userInput = " " + PREFIX_ADDRESS + "123 Main St " + PREFIX_PHONE + "98765432";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_missingAddress_throwsParseException() {
        String userInput = " " + PREFIX_NAME + "John Doe " + PREFIX_PHONE + "98765432";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_missingPhone_throwsParseException() {
        String userInput = " " + PREFIX_NAME + "John Doe " + PREFIX_ADDRESS + "123 Main St";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_invalidFormat_throwsParseException() {
        String userInput = "random text without prefixes";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidName_throwsParseException() {
        String userInput = " n/!@# a/123 Main St p/98765432"; // Invalid name format
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidPhone_throwsParseException() {
        String userInput = " n/John Doe a/123 Main St p/abcd1234"; // Invalid phone number format
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_extraWhitespace_returnsAddHouseholdCommand() throws ParseException {
        String userInput = " " + PREFIX_NAME + "   John   Doe   " + PREFIX_ADDRESS + "  123   Main St   "
                + PREFIX_PHONE + "  98765432  ";
        AddHouseholdCommand expectedCommand = new AddHouseholdCommand(
                new Household(new Name("John Doe"), new Address("123 Main St"), new Contact("98765432")));

        assertEquals(expectedCommand, parser.parse(userInput));
    }
}

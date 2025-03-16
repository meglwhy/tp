package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditHouseholdCommand;
import seedu.address.logic.commands.EditHouseholdCommand.EditHouseholdDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.tag.Tag;

class EditHouseholdCommandParserTest {

    private EditHouseholdCommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new EditHouseholdCommandParser();
    }

    @Test
    void parse_validArgs_returnsEditHouseholdCommand() throws ParseException {
        String userInput = " " + PREFIX_ID + " H000001 " + PREFIX_NAME + " John Doe "
                + PREFIX_ADDRESS + " 123 Main St " + PREFIX_PHONE + " 98765432 ";

        // Create the EditHouseholdDescriptor and set each field individually
        EditHouseholdDescriptor editHouseholdDescriptor = new EditHouseholdDescriptor();
        editHouseholdDescriptor.setName(new Name("John Doe"));
        editHouseholdDescriptor.setAddress(new Address("123 Main St"));
        editHouseholdDescriptor.setContact(new Contact("98765432"));

        EditHouseholdCommand expectedCommand = new EditHouseholdCommand(
                new HouseholdId("H000001"),
                editHouseholdDescriptor
        );

        EditHouseholdCommand resultCommand = parser.parse(userInput);
        assertEquals(expectedCommand, resultCommand);
    }

    @Test
    void parse_missingId_throwsParseException() {
        String userInput = " " + PREFIX_NAME + " John Doe "
                + PREFIX_ADDRESS + " 123 Main St " + PREFIX_PHONE + " 98765432 ";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_noFieldsEdited_throwsParseException() {
        String userInput = " " + PREFIX_ID + " H000001 ";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_editPhoneNumber_returnsEditHouseholdCommand() throws ParseException {
        String userInput = " " + PREFIX_ID + " H000001 " + PREFIX_PHONE + " 91234567 ";

        // Create the EditHouseholdDescriptor and set contact individually
        EditHouseholdDescriptor editHouseholdDescriptor = new EditHouseholdDescriptor();
        editHouseholdDescriptor.setContact(new Contact("91234567"));

        EditHouseholdCommand expectedCommand = new EditHouseholdCommand(
                new HouseholdId("H000001"),
                editHouseholdDescriptor
        );

        EditHouseholdCommand resultCommand = parser.parse(userInput);
        assertEquals(expectedCommand, resultCommand);
    }

    @Test
    void parse_editTags_returnsEditHouseholdCommand() throws ParseException {
        String userInput = " " + PREFIX_ID + " H000001 " + PREFIX_TAG + " family " + PREFIX_TAG + " relatives ";

        Set<Tag> expectedTags = new HashSet<>(Arrays.asList(new Tag("family"), new Tag("relatives")));
        EditHouseholdDescriptor editHouseholdDescriptor = new EditHouseholdDescriptor();
        editHouseholdDescriptor.setTags(expectedTags);

        EditHouseholdCommand expectedCommand = new EditHouseholdCommand(
                new HouseholdId("H000001"),
                editHouseholdDescriptor
        );

        EditHouseholdCommand resultCommand = parser.parse(userInput);
        assertEquals(expectedCommand, resultCommand);
    }

    @Test
    void parse_invalidPrefix_throwsParseException() {
        String userInput = " " + PREFIX_ID + " H000001 " + " invalidPrefix someValue ";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_emptyTag_throwsParseException() {
        String userInput = " " + PREFIX_ID + " H000001 " + PREFIX_TAG + " ";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_duplicateTags_returnsEditHouseholdCommand() throws ParseException {
        String userInput = " " + PREFIX_ID + " H000001 " + PREFIX_TAG + " family " + PREFIX_TAG + " family ";

        Set<Tag> expectedTags = new HashSet<>(Arrays.asList(new Tag("family")));
        EditHouseholdDescriptor editHouseholdDescriptor = new EditHouseholdDescriptor();
        editHouseholdDescriptor.setTags(expectedTags);

        EditHouseholdCommand expectedCommand = new EditHouseholdCommand(
                new HouseholdId("H000001"),
                editHouseholdDescriptor
        );

        EditHouseholdCommand resultCommand = parser.parse(userInput);
        assertEquals(expectedCommand, resultCommand);
    }

    @Test
    void parse_multipleFieldsEdited_returnsEditHouseholdCommand() throws ParseException {
        String userInput = " " + PREFIX_ID + " H000001 " + PREFIX_NAME + " Jane Doe "
                + PREFIX_PHONE + " 91234567 " + PREFIX_TAG + " family ";

        Set<Tag> expectedTags = new HashSet<>(Arrays.asList(new Tag("family")));
        EditHouseholdDescriptor editHouseholdDescriptor = new EditHouseholdDescriptor();
        editHouseholdDescriptor.setName(new Name("Jane Doe"));
        editHouseholdDescriptor.setContact(new Contact("91234567"));
        editHouseholdDescriptor.setTags(expectedTags);

        EditHouseholdCommand expectedCommand = new EditHouseholdCommand(
                new HouseholdId("H000001"),
                editHouseholdDescriptor
        );

        EditHouseholdCommand resultCommand = parser.parse(userInput);
        assertEquals(expectedCommand, resultCommand);
    }
}

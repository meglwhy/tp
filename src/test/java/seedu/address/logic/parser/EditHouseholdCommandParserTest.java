package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditHouseholdCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.tag.Tag;


public class EditHouseholdCommandParserTest {

    private EditHouseholdCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new EditHouseholdCommandParser();
    }

    @Test
    public void parse_missingHouseholdId_throwsParseException() {
        String userInput = "n/John p/98765432 a/123 Street";

        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse(userInput));

        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditHouseholdCommand.MESSAGE_USAGE),
                thrown.getMessage());
    }

    @Test
    public void parse_validFields_createsEditHouseholdCommand() throws ParseException {
        String userInput = " id/H000001 n/John p/98765432 a/123 Street t/Friends t/Family";

        Name name = new Name("John");
        Contact contact = new Contact("98765432");
        Address address = new Address("123 Street");
        Set<Tag> tags = Set.of(new Tag("Friends"), new Tag("Family"));

        EditHouseholdCommand.EditHouseholdDescriptor descriptor = new EditHouseholdCommand.EditHouseholdDescriptor();
        descriptor.setName(name);
        descriptor.setContact(contact);
        descriptor.setAddress(address);
        descriptor.setTags(tags);

        EditHouseholdCommand expectedCommand = new EditHouseholdCommand(
                new HouseholdId("H000001"),
                descriptor
        );

        EditHouseholdCommand result = parser.parse(userInput);

        assertEquals(expectedCommand, result);
    }

    @Test
    public void parse_noFieldsEdited_throwsParseException() {
        String userInput = "id/H000001";

        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse(userInput));

        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditHouseholdCommand.MESSAGE_USAGE),
                thrown.getMessage());
    }

    @Test
    public void parse_invalidPhoneFormat_throwsParseException() {
        String userInput = "id/H000001 n/John p/invalidPhone a/123 Street";

        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse(userInput));

        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditHouseholdCommand.MESSAGE_USAGE),
                thrown.getMessage());
    }

    @Test
    public void parse_invalidAddressFormat_throwsParseException() {
        String userInput = "id/H000001 n/John p/98765432 a/invalidAddress";

        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse(userInput));

        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditHouseholdCommand.MESSAGE_USAGE),
                thrown.getMessage());
    }

    @Test
    public void parse_emptyTags_throwsParseException() {
        String userInput = "id/H000001 n/John p/98765432 a/123 Street t/";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}

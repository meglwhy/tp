package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditHouseholdCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.tag.Tag;

public class EditHouseholdCommandParserTest {

    private EditHouseholdCommandParser parser;
    private Model mockModel;
    private HouseholdBook mockHouseholdBook;

    @BeforeEach
    public void setUp() {
        mockModel = mock(Model.class);
        mockHouseholdBook = mock(HouseholdBook.class);
        when(mockModel.getHouseholdBook()).thenReturn(mockHouseholdBook);

        parser = new EditHouseholdCommandParser(mockModel);
    }

    @Test
    public void parse_validInput_createsEditHouseholdCommand() throws ParseException {
        String userInput = " id/H000001 n/John p/98765432 a/123 Street t/Friends t/Family";

        HouseholdId householdId = new HouseholdId("H000001");
        Name name = new Name("John");
        Contact contact = new Contact("98765432");
        Address address = new Address("123 Street");
        Set<Tag> tags = Set.of(new Tag("Friends"), new Tag("Family"));

        EditHouseholdCommand.EditHouseholdDescriptor descriptor = new EditHouseholdCommand.EditHouseholdDescriptor();
        descriptor.setName(name);
        descriptor.setContact(contact);
        descriptor.setAddress(address);
        descriptor.setTags(tags);

        EditHouseholdCommand expectedCommand = new EditHouseholdCommand(householdId, descriptor);

        // Mock the HouseholdBook's response to simulate a found household
        when(mockHouseholdBook.getHouseholdById(householdId))
                .thenReturn(Optional.of(new Household(name, address, contact, householdId, tags)));

        EditHouseholdCommand result = parser.parse(userInput);
        assertEquals(expectedCommand, result);
    }

    @Test
    public void parse_missingId_throwsParseException() {
        String userInput = "n/John p/98765432 a/123 Street t/Friends t/Family";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidId_throwsParseException() {
        String userInput = "id/InvalidID n/John p/98765432 a/123 Street t/Friends t/Family";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidPhone_throwsParseException() {
        String userInput = "id/H000001 n/John p/invalidPhone a/123 Street t/Friends t/Family";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingName_throwsParseException() {
        String userInput = "id/H000001 p/98765432 a/123 Street t/Friends t/Family";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingAddress_throwsParseException() {
        String userInput = "id/H000001 n/John p/98765432 t/Friends t/Family";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
    @Test
    public void parse_householdNotFound_throwsParseException() {
        String userInput = "id/H000001 n/John p/98765432 a/123 Street t/Friends t/Family";

        HouseholdId householdId = new HouseholdId("H000001");
        when(mockHouseholdBook.getHouseholdById(householdId)).thenReturn(Optional.empty());

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        String userInput = "";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}

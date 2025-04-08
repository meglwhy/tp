package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.tag.Tag;

public class MessagesTest {

    @Test
    void getErrorMessageForDuplicatePrefixes_returnsCorrectlyFormattedMessage() {
        Prefix p1 = new Prefix("a/");
        Prefix p2 = new Prefix("b/");
        String expected = Messages.MESSAGE_DUPLICATE_FIELDS + "a/ b/";
        String result = Messages.getErrorMessageForDuplicatePrefixes(p1, p2);
        // Since the prefixes are collected into a set, their order may vary.
        Set<String> expectedSet = Set.of("a/", "b/");
        Set<String> resultSet = Set.of(result.replace(Messages.MESSAGE_DUPLICATE_FIELDS, "").trim().split("\\s+"));
        assertEquals(expectedSet, resultSet);
    }

    @Test
    void format_household_returnsProperlyFormattedString() {
        Name name = new Name("Test Family");
        HouseholdId id = new HouseholdId("H000001");
        Address address = new Address("123 Test St");
        Contact contact = new Contact("91234567");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("tag1"));
        tags.add(new Tag("tag2"));
        Household household = new Household(name, address, contact, id, tags);
        String formatted = Messages.format(household);
        // Expected format:
        // "Test Family (ID: H000001); Address: 123 Test St; Contact: 91234567; Tags: [tag1][tag2]"
        // Since the order of tags is not guaranteed, we check that all parts are contained.
        assertTrue(formatted.contains("Test Family (ID: H000001)"));
        assertTrue(formatted.contains("Address: 123 Test St"));
        assertTrue(formatted.contains("Contact: 91234567"));
        // The tag portions:
        // Remove the fixed parts and then check if both tags are present (order independent).
        String tagPortion = formatted.substring(formatted.indexOf("Tags: ") + 6).trim();
        Set<String> expectedTags = tags.stream().map(tag -> "[" + tag + "]").collect(Collectors.toSet());
        for (String expectedTag : expectedTags) {
            assertTrue(tagPortion.contains(expectedTag));
        }
    }
}


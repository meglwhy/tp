package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedHousehold.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalHouseholds.BOB_HOUSEHOLD;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Name;

public class JsonAdaptedHouseholdTest {
    private static final String INVALID_NAME = "R@chel"; // Contains special character
    private static final String INVALID_ADDRESS = " "; // Empty address
    private static final String INVALID_CONTACT = "911a"; // Contains non-digit
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_ID = BOB_HOUSEHOLD.getId().toString();
    private static final String VALID_NAME = BOB_HOUSEHOLD.getName().toString();
    private static final String VALID_ADDRESS = BOB_HOUSEHOLD.getAddress().toString();
    private static final String VALID_CONTACT = BOB_HOUSEHOLD.getContact().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BOB_HOUSEHOLD.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validHouseholdDetails_returnsHousehold() throws Exception {
        JsonAdaptedHousehold household = new JsonAdaptedHousehold(BOB_HOUSEHOLD);
        assertEquals(BOB_HOUSEHOLD, household.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedHousehold household =
                new JsonAdaptedHousehold(VALID_ID, INVALID_NAME, VALID_ADDRESS, VALID_CONTACT, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, household::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedHousehold household =
                new JsonAdaptedHousehold(VALID_ID, null, VALID_ADDRESS, VALID_CONTACT, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, household::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedHousehold household =
                new JsonAdaptedHousehold(VALID_ID, VALID_NAME, INVALID_ADDRESS, VALID_CONTACT, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, household::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedHousehold household =
                new JsonAdaptedHousehold(VALID_ID, VALID_NAME, null, VALID_CONTACT, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, household::toModelType);
    }

    @Test
    public void toModelType_invalidContact_throwsIllegalValueException() {
        JsonAdaptedHousehold household =
                new JsonAdaptedHousehold(VALID_ID, VALID_NAME, VALID_ADDRESS, INVALID_CONTACT, VALID_TAGS);
        String expectedMessage = Contact.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, household::toModelType);
    }

    @Test
    public void toModelType_nullContact_throwsIllegalValueException() {
        JsonAdaptedHousehold household =
                new JsonAdaptedHousehold(VALID_ID, VALID_NAME, VALID_ADDRESS, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Contact.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, household::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedHousehold household =
                new JsonAdaptedHousehold(VALID_ID, VALID_NAME, VALID_ADDRESS, VALID_CONTACT, invalidTags);
        assertThrows(IllegalValueException.class, household::toModelType);
    }
}

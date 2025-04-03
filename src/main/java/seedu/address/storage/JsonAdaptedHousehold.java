package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Household}.
 */
public class JsonAdaptedHousehold {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Household's %s field is missing!";

    private final String id;
    private final String name;
    private final String address;
    private final String contact;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedHousehold} with the given household details.
     */
    @JsonCreator
    public JsonAdaptedHousehold(@JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("address") String address,
            @JsonProperty("contact") String contact,
            @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Household} into this class for Jackson use.
     */
    public JsonAdaptedHousehold(Household source) {
        id = source.getId().toString();
        name = source.getName().toString();
        address = source.getAddress().toString();
        contact = source.getContact().toString();
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted household object into the model's {@code Household} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted household.
     */
    public Household toModelType() throws IllegalValueException {
        final List<Tag> householdTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            householdTags.add(tag.toModelType());
        }

        if (id == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "ID"));
        }
        if (!HouseholdId.isValidId(id)) {
            throw new IllegalValueException(HouseholdId.MESSAGE_CONSTRAINTS);
        }
        final HouseholdId modelId = HouseholdId.fromString(id);

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (contact == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Contact.class.getSimpleName()));
        }
        if (!Contact.isValidContact(contact)) {
            throw new IllegalValueException(Contact.MESSAGE_CONSTRAINTS);
        }
        final Contact modelContact = new Contact(contact);

        final Set<Tag> modelTags = new HashSet<>(householdTags);
        return new Household(modelName, modelAddress, modelContact, modelId, modelTags);
    }
}

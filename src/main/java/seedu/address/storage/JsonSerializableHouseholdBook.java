package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.ReadOnlyHouseholdBook;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;

/**
 * An Immutable HouseholdBook that is serializable to JSON format.
 */
@JsonRootName(value = "householdbook")
public class JsonSerializableHouseholdBook {

    public static final String MESSAGE_DUPLICATE_HOUSEHOLD = "Households list contains duplicate household(s).";
    public static final String MESSAGE_DUPLICATE_SESSION = "Sessions list contains duplicate session(s).";

    private final List<JsonAdaptedHousehold> households = new ArrayList<>();
    private final List<JsonAdaptedSession> sessions = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableHouseholdBook} with the given households and sessions.
     */
    @JsonCreator
    public JsonSerializableHouseholdBook(@JsonProperty("households") List<JsonAdaptedHousehold> households,
                                         @JsonProperty("sessions") List<JsonAdaptedSession> sessions) {
        this.households.addAll(households);
        this.sessions.addAll(sessions);
    }

    /**
     * Converts a given {@code ReadOnlyHouseholdBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableHouseholdBook}.
     */
    public JsonSerializableHouseholdBook(ReadOnlyHouseholdBook source) {
        households.addAll(source.getHouseholdList().stream()
                .map(JsonAdaptedHousehold::new)
                .toList());
        sessions.addAll(source.getSessionList().stream()
                .map(JsonAdaptedSession::new)
                .toList());
    }

    /**
     * Converts this household book into the model's {@code HouseholdBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public ReadOnlyHouseholdBook toModelType() throws IllegalValueException {
        // Check for duplicates
        List<Household> householdList = new ArrayList<>();
        for (JsonAdaptedHousehold jsonAdaptedHousehold : households) {
            householdList.add(jsonAdaptedHousehold.toModelType());
        }
        Set<HouseholdId> ids = new HashSet<>();
        Set<String> names = new HashSet<>();
        Set<String> addresses = new HashSet<>();
        Set<String> contacts = new HashSet<>();
        for (Household household : householdList) {
            if (ids.contains(household.getId())) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_HOUSEHOLD);
            }
            ids.add(household.getId());
            String name = household.getName().toString();
            if (names.contains(name)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_HOUSEHOLD);
            }
            names.add(name);
            String address = household.getAddress().toString();
            if (addresses.contains(address)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_HOUSEHOLD);
            }
            addresses.add(address);
            String contact = household.getContact().toString();
            if (contacts.contains(contact)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_HOUSEHOLD);
            }
            contacts.add(contact);
        }
        HouseholdBook householdBook = new HouseholdBook();
        for (Household household : householdList) {
            householdBook.addHousehold(household);
        }
        // Convert and check for duplicate sessions
        List<Session> sessionList = new ArrayList<>();
        for (JsonAdaptedSession jsonAdaptedSession : sessions) {
            sessionList.add(jsonAdaptedSession.toModelType());
        }
        Set<String> sessionIds = new HashSet<>();
        for (Session session : sessionList) {
            if (sessionIds.contains(session.getSessionId())) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_SESSION);
            }
            sessionIds.add(session.getSessionId());
        }
        for (Session session : sessionList) {
            householdBook.addSessionToHousehold(session.getHouseholdId(), session);
        }
        return householdBook;
    }
}

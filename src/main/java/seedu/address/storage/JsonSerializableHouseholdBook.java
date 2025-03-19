package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.ReadOnlyHouseholdBook;
import seedu.address.model.household.Household;
import seedu.address.model.session.Session;

/**
 * An Immutable HouseholdBook that is serializable to JSON format.
 */
@JsonRootName(value = "householdbook")
class JsonSerializableHouseholdBook {

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
                .collect(Collectors.toList()));
        sessions.addAll(source.getSessionList().stream()
                .map(JsonAdaptedSession::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this household book into the model's {@code ReadOnlyHouseholdBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public ReadOnlyHouseholdBook toModelType() throws IllegalValueException {
        HouseholdBook householdBook = new HouseholdBook();

        for (JsonAdaptedHousehold jsonAdaptedHousehold : households) {
            Household household = jsonAdaptedHousehold.toModelType();
            if (householdBook.hasHousehold(household)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_HOUSEHOLD);
            }
            householdBook.addHousehold(household);
        }

        for (JsonAdaptedSession jsonAdaptedSession : sessions) {
            Session session = jsonAdaptedSession.toModelType();
            if (householdBook.hasSession(session)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_SESSION);
            }
            householdBook.addSessionToHousehold(session.getHouseholdId(), session);
        }

        return householdBook;
    }
}

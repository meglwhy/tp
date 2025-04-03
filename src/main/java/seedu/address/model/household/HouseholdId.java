package seedu.address.model.household;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Household's ID in the household book.
 * Guarantees: immutable; ID is valid as declared in {@link #isValidId(String)}
 */
public class HouseholdId {
    public static final String MESSAGE_CONSTRAINTS =
            "Household ID should start with 'H' followed by 6 digits";
    private static final String VALIDATION_REGEX = "H\\d{6}";
    private static long idCounter = 0;

    public final String value;

    /**
     * Constructs a new {@code HouseholdId} with the specified value.
     * Primarily used for testing purposes to create a HouseholdId with a specific value.
     *
     * @param value The ID value to be assigned.
     */
    public HouseholdId(String value) {
        this.value = value;
    }

    /**
     * Creates a new {@code HouseholdId} with an auto-generated ID value.
     * The ID is generated based on an incrementing counter to ensure uniqueness.
     *
     * @return A new {@code HouseholdId} with a unique, auto-generated value.
     */
    public static HouseholdId generateNewId() {
        return new HouseholdId(String.format("H%06d", ++idCounter));
    }

    /**
     * Creates a {@code HouseholdId} from the given ID string.
     * The ID counter is updated to ensure subsequent generated IDs are unique.
     *
     * @param id The ID string to create a {@code HouseholdId} from.
     * @return A new {@code HouseholdId} with the specified ID value.
     * @throws NullPointerException if the given ID is null.
     */
    public static HouseholdId fromString(String id) {
        requireNonNull(id);
        HouseholdId householdId = new HouseholdId(id);

        try {
            long storedId = Long.parseLong(id.substring(1));
            idCounter = Math.max(idCounter, storedId);
        } catch (NumberFormatException e) {
            // Handle invalid ID format if necessary
        }
        return householdId;
    }

    /**
     * Validates whether a given string is a valid household ID.
     * A valid ID must match the defined validation pattern.
     *
     * @param test The string to validate.
     * @return true if the string is a valid household ID, false otherwise.
     */
    public static boolean isValidId(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof HouseholdId
                && value.equals(((HouseholdId) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

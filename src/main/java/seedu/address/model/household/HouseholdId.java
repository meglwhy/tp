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
     * Constructs a new {@code HouseholdId}.
     */
    private HouseholdId(String value) {
        this.value = value;
    }

    /**
     * Creates a new HouseholdId with auto-generated ID.
     */
    public static HouseholdId generateNewId() {
        return new HouseholdId(String.format("H%06d", ++idCounter));
    }

    /**
     * Creates a HouseholdId from an existing ID string.
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
     * Returns true if a given string is a valid household ID.
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
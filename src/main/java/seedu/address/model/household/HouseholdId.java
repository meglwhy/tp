package seedu.address.model.household;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Household's ID in the household book.
 * Guarantees: immutable; ID is valid as declared in {@link #isValidId(String)}
 */
public class HouseholdId {
    public static final String MESSAGE_CONSTRAINTS =
            "Household ID should be a positive integer";
    private static long idCounter = 0;

    public final long value;

    /**
     * Constructs a new {@code HouseholdId}.
     */
    private HouseholdId(long value) {
        this.value = value;
    }

    /**
     * Creates a new HouseholdId with an auto-generated ID.
     */
    public static HouseholdId generateNewId() {
        return new HouseholdId(++idCounter);
    }

    /**
     * Creates a HouseholdId from an existing ID string.
     */
    public static HouseholdId fromString(String id) {
        requireNonNull(id);
        try {
            long storedId = Long.parseLong(id);
            idCounter = Math.max(idCounter, storedId);
            return new HouseholdId(storedId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Household ID: " + id);
        }
    }

    /**
     * Returns true if a given string is a valid household ID.
     */
    public static boolean isValidId(String test) {
        try {
            long parsedId = Long.parseLong(test);
            return parsedId > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof HouseholdId
                && value == ((HouseholdId) other).value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }
}

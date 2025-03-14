package seedu.address.model.household;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Household's ID in the household book.
 *
 * Guarantees: immutable; ID is valid as declared in {@link #isValidId(String)}.
 */
public class HouseholdId {
    public static final String MESSAGE_CONSTRAINTS = "Household ID should be in the format H followed by a positive integer";
    private static long idCounter = 0;

    public final String value;

    /**
     * Constructs a new {@code HouseholdId}.
     */
    private HouseholdId(String value) {
        this.value = value;
    }

    /**
     * Creates a new HouseholdId with an auto-generated ID (starting with H).
     */
    public static HouseholdId generateNewId() {
        idCounter++;  // Increment counter for the next ID
        return new HouseholdId("H" + idCounter);
    }

    /**
     * Creates a HouseholdId from an existing ID string.
     */
    public static HouseholdId fromString(String id) {
        requireNonNull(id);
        if (!id.startsWith("H") || !isValidId(id.substring(1))) {
            throw new IllegalArgumentException("Invalid Household ID: " + id);
        }
        return new HouseholdId(id);
    }

    /**
     * Returns true if a given string is a valid household ID (must start with "H" and followed by a positive integer).
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
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof HouseholdId && value.equals(((HouseholdId) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

package seedu.address.model.household;

/**
 * Represents a Household's ID in the household book.
 * Guarantees: immutable; ID is valid as declared in {@link #isValidId(String)}
 */
public class HouseholdId {
    public static final String MESSAGE_CONSTRAINTS = 
            "Household ID should start with 'H' followed by 6 digits";
    private static final String VALIDATION_REGEX = "H\\d{6}";
    private static int lastGeneratedNumber = 0;

    private final String id;

    private HouseholdId(String id) {
        this.id = id;
    }

    /**
     * Parses a string into a HouseholdId.
     * @throws IllegalArgumentException if the string is not a valid ID
     */
    public static HouseholdId parse(String id) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        return new HouseholdId(id);
    }

    /**
     * Generates a new unique household ID.
     */
    public static HouseholdId generateNewId() {
        lastGeneratedNumber++;
        return new HouseholdId(String.format("H%06d", lastGeneratedNumber));
    }

    /**
     * Returns true if a given string is a valid household ID.
     */
    public static boolean isValidId(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof HouseholdId
                && id.equals(((HouseholdId) other).id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
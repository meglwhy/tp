package seedu.address.model.household;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Household's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names may only contain alphanumeric characters or spaces, slashes (/),"
                    + "apostrophes ('), and dashes (-).";

    /**
     * Regular expression for validating names.
     * A valid name must start with an alphanumeric character and may contain
     * alphanumeric characters and spaces thereafter.
     * The first character must not be a whitespace to prevent blank inputs.
     */
    public static final String VALIDATION_REGEX = "[\\p{L}\\p{N}][\\p{L}\\p{N}/'\\- ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name} with the specified valid name.
     *
     * @param name A valid name that satisfies the {@code VALIDATION_REGEX}.
     * @throws NullPointerException If {@code name} is null.
     * @throws IllegalArgumentException If {@code name} does not satisfy {@code VALIDATION_REGEX}.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Validates the given string as a potential name.
     * Returns true if the string matches the {@code VALIDATION_REGEX}.
     *
     * @param test The string to validate.
     * @return true if the given string is a valid name, false otherwise.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Name // instanceof handles nulls
                && fullName.equals(((Name) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }
}

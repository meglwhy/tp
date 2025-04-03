package seedu.address.model.household;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Household's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Address {

    public static final String MESSAGE_CONSTRAINTS =
            "Addresses can take any values, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "\\S.*";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     * @throws NullPointerException if {@code address} is null.
     * @throws IllegalArgumentException if the {@code address} is invalid.
     */
    public Address(String address) {
        requireNonNull(address);
        checkArgument(isValidAddress(address), MESSAGE_CONSTRAINTS);
        value = address;
    }

    /**
     * Returns true if the given string is a valid address.
     *
     * @param test The string to test.
     * @return True if the input matches the validation regex; false otherwise.
     */
    public static boolean isValidAddress(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Address
                && value.equals(((Address) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

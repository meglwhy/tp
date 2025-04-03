package seedu.address.model.household;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Household's contact number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidContact(String)}
 */
public class Contact {

    public static final String MESSAGE_CONSTRAINTS =
            "Contact numbers should be exactly 8 digits long, start with 6, 8, or 9, and contain only numbers.";
    public static final String VALIDATION_REGEX = "^[689]\\d{7}$";
    public final String value;

    /**
     * Constructs a {@code Contact} with the specified contact number.
     *
     * @param contact A valid contact number.
     * @throws NullPointerException if the provided contact is null.
     * @throws IllegalArgumentException if the provided contact is invalid according to {@link #isValidContact(String)}.
     */
    public Contact(String contact) {
        requireNonNull(contact);
        checkArgument(isValidContact(contact), MESSAGE_CONSTRAINTS);
        value = contact;
    }

    /**
     * Returns true if the given string is a valid contact number based on the specified validation regex.
     *
     * @param test The string to validate.
     * @return true if the string matches the validation regex; false otherwise.
     */
    public static boolean isValidContact(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Contact // instanceof handles nulls
                && value.equals(((Contact) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

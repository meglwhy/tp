package seedu.address.model.session;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Session's note in the household book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNote(String)}
 */
public class SessionNote {
    public static final String MESSAGE_CONSTRAINTS =
            "Notes can take any values, and it should not be blank";

    public static final String VALIDATION_REGEX = "\\S.*";

    public final String value;

    /**
     * Constructs a {@code SessionNote} with the specified note content.
     * The note must satisfy the validation criteria defined by {@code VALIDATION_REGEX}.
     *
     * @param note A valid note string.
     * @throws NullPointerException If {@code note} is null.
     * @throws IllegalArgumentException If {@code note} does not meet validation criteria.
     */
    public SessionNote(String note) {
        requireNonNull(note);
        checkArgument(isValidNote(note), MESSAGE_CONSTRAINTS);
        value = note;
    }

    /**
     * Validates if the given string meets the criteria for a valid note.
     * The validation criteria are defined by {@code VALIDATION_REGEX}.
     *
     * @param test The string to validate.
     * @return {@code true} if {@code test} satisfies the validation criteria; {@code false} otherwise.
     */
    public static boolean isValidNote(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SessionNote
                && value.equals(((SessionNote) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

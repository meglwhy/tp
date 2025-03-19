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

    /*
     * The first character of the note must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs a {@code SessionNote}.
     *
     * @param note A valid note.
     */
    public SessionNote(String note) {
        requireNonNull(note);
        checkArgument(isValidNote(note), MESSAGE_CONSTRAINTS);
        value = note;
    }

    /**
     * Returns true if a given string is a valid note.
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

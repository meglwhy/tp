package seedu.address.model.session;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Session's time in the household book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class SessionTime implements Comparable<SessionTime> {
    public static final String MESSAGE_CONSTRAINTS =
            "Times should be in the 24-hour format HH:mm and should be a valid time";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public final LocalTime value;

    /**
     * Constructs a {@code SessionTime}.
     *
     * @param time A valid time string.
     */
    public SessionTime(String time) {
        requireNonNull(time);
        checkArgument(isValidTime(time), MESSAGE_CONSTRAINTS);
        value = LocalTime.parse(time, FORMATTER);
    }

    /**
     * Returns true if a given string is a valid time.
     */
    public static boolean isValidTime(String test) {
        try {
            LocalTime.parse(test, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value.format(FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SessionTime
                && value.equals(((SessionTime) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(SessionTime other) {
        return this.value.compareTo(other.value);
    }
}

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
     * Constructs a {@code SessionTime} with the specified time string.
     * The time string must adhere to the expected format defined by {@code FORMATTER}.
     *
     * @param time A valid time string.
     * @throws NullPointerException If {@code time} is null.
     * @throws IllegalArgumentException If {@code time} is not in the correct format.
     */
    public SessionTime(String time) {
        requireNonNull(time);
        checkArgument(isValidTime(time), MESSAGE_CONSTRAINTS);
        value = LocalTime.parse(time, FORMATTER);
    }

    /**
     * Validates if the given string represents a valid time.
     * The time string must adhere to the expected format defined by {@code FORMATTER}.
     *
     * @param test The string to validate.
     * @return {@code true} if {@code test} is a valid time string; {@code false} otherwise.
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

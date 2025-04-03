package seedu.address.model.session;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Session's date in the household book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class SessionDate implements Comparable<SessionDate> {
    public static final String MESSAGE_CONSTRAINTS =
            "Dates should be in the format YYYY-MM-DD and should be a valid date";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    public final LocalDate value;

    /**
     * Constructs a {@code SessionDate} with the specified date string.
     * The date string must adhere to the expected format defined by {@code FORMATTER}.
     *
     * @param date A valid date string.
     * @throws NullPointerException If {@code date} is null.
     * @throws IllegalArgumentException If {@code date} is not in the correct format.
     */
    public SessionDate(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(date, FORMATTER);
    }

    /**
     * Validates if the given string represents a valid date.
     * The date string must adhere to the expected format defined by {@code FORMATTER}.
     *
     * @param test The string to validate.
     * @return {@code true} if {@code test} is a valid date string; {@code false} otherwise.
     */
    public static boolean isValidDate(String test) {
        try {
            LocalDate.parse(test, FORMATTER);
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
                || (other instanceof SessionDate
                && value.equals(((SessionDate) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(SessionDate other) {
        return this.value.compareTo(other.value);
    }
}

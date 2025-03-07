package seedu.address.model.session;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import static java.util.Objects.requireNonNull;

import static seedu.address.commons.util.AppUtil.checkArgument;

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
     * Constructs a {@code SessionDate}.
     *
     * @param date A valid date string.
     */
    public SessionDate(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(date, FORMATTER);
    }

    /**
     * Returns true if a given string is a valid date.
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
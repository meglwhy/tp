package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.household.Household;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Constructs an error message indicating the presence of duplicate prefixes.
     * Ensures that at least one duplicate prefix is provided.
     *
     * @param duplicatePrefixes The prefixes that are duplicated.
     * @return A formatted error message listing the duplicate prefixes.
     * @throws AssertionError if no duplicate prefixes are provided.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the details of a household into a readable text representation.
     * Includes the household's name, ID, address, contact, and associated tags.
     *
     * @param household The household to be formatted.
     * @return A string representation of the household with all relevant details.
     */
    public static String format(Household household) {
        final StringBuilder builder = new StringBuilder();
        builder.append(household.getName())
                .append(" (ID: ")
                .append(household.getId())
                .append("); Address: ")
                .append(household.getAddress())
                .append("; Contact: ")
                .append(household.getContact())
                .append("; Tags: ");
        household.getTags().forEach(tag -> builder.append("[").append(tag).append("]"));
        return builder.toString();
    }

}

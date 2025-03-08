package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.household.HouseholdContainsKeywordsPredicate;
import seedu.address.model.household.Household;
import seedu.address.model.Model;
/**
 * Finds and lists all households in household book whose name, address, or tags contain any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindHouseholdCommand extends Command {

    public static final String COMMAND_WORD = "find-household";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all households whose names, addresses, or tags "
            + "contain any of the specified keywords (case-insensitive) and displays them as a list.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " smith";

    public static final String MESSAGE_NO_KEYWORDS = "Please provide at least one keyword.";
    public static final String MESSAGE_SUCCESS = "Found %1$d household(s) matching: %2$s";
    public static final String MESSAGE_NO_MATCHING_HOUSEHOLDS = "No households found matching: %1$s";

    private final HouseholdContainsKeywordsPredicate predicate;
    private final String keywords;

    public FindHouseholdCommand(String keywords) throws CommandException {
        requireNonNull(keywords);
        String trimmedKeywords = keywords.trim();
        if (trimmedKeywords.isEmpty()) {
            throw new CommandException(MESSAGE_NO_KEYWORDS);
        }
        
        this.keywords = trimmedKeywords;
        this.predicate = new HouseholdContainsKeywordsPredicate(
                Arrays.asList(trimmedKeywords.split("\\s+")));
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        // Apply the filter to the filtered household list
        model.updateFilteredHouseholdList(predicate);

        // Get the number of households in the filtered list
        int matchingCount = model.getFilteredHouseholdList().size();

        if (matchingCount == 0) {
            return new CommandResult(String.format(MESSAGE_NO_MATCHING_HOUSEHOLDS, keywords));
        }

        // Return the result showing how many households match
        return new CommandResult(
                String.format(MESSAGE_SUCCESS, matchingCount, keywords));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindHouseholdCommand // instanceof handles nulls
                && predicate.equals(((FindHouseholdCommand) other).predicate)); // state check
    }
} 
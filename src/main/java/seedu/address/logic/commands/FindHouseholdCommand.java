package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.HouseholdContainsKeywordsPredicate;

/**
 * Finds and lists all households in household book whose name, address, tags, household ID, or phone numbers
 * contain any of the argument keywords. Keyword matching is case-insensitive.
 */
public class FindHouseholdCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all households whose names, addresses, tags, "
            + "household ID or phone numbers contain any of the specified keywords (case-insensitive)"
            + "and displays them as a list.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]... or phrases in double quotes\n"
            + "Example: " + COMMAND_WORD + " \"John Smith\" 12345";

    public static final String MESSAGE_NO_KEYWORDS = "Please provide at least one keyword.";
    public static final String MESSAGE_SUCCESS = "Found %1$d household(s) matching: %2$s";
    public static final String MESSAGE_NO_MATCHING_HOUSEHOLDS = "No households found matching: %1$s";

    // Public for testing
    public final HouseholdContainsKeywordsPredicate predicate;
    private final String keywords;
    /**
     * Creates a {@code FindHouseholdCommand} to search for households based on the provided keywords.
     * <p>
     * Keywords can be single words or phrases enclosed in double quotes.
     * If all keywords are numeric, the search will be treated as a numeric search.
     *
     * @param keywords The search query containing one or more keywords.
     * @throws NullPointerException if {@code keywords} is null.
     * @throws CommandException     if the provided keywords are empty.
     */
    public FindHouseholdCommand(String keywords) throws CommandException {
        requireNonNull(keywords);
        String trimmedKeywords = keywords.trim();
        if (trimmedKeywords.isEmpty()) {
            throw new CommandException(MESSAGE_NO_KEYWORDS);
        }

        this.keywords = trimmedKeywords;

        // Extract keywords, considering phrases inside double quotes as a single keyword
        List<String> parsedKeywords = extractKeywords(trimmedKeywords);

        boolean numericSearch = parsedKeywords.stream().allMatch(this::isNumber);

        this.predicate = new HouseholdContainsKeywordsPredicate(parsedKeywords, numericSearch);
    }

    // Helper method to extract keywords, including handling quoted phrases
    private List<String> extractKeywords(String input) {
        List<String> keywords = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]+)\"|(\\S+)").matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Found a phrase inside double quotes, add it as a single keyword
                keywords.add(matcher.group(1));
            } else {
                // Found a single word, add it as a keyword
                keywords.add(matcher.group(2));
            }
        }
        return keywords;
    }

    // Helper method to check if a keyword is a number (e.g., household ID or phone number)
    private boolean isNumber(String keyword) {
        Pattern pattern = Pattern.compile("\\d+"); // Regex to match digits only
        Matcher matcher = pattern.matcher(keyword);
        return matcher.matches();
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

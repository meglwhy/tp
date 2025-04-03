package seedu.address.model.household;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Tests that a {@code Household}'s name, address, tags, household ID or phone number contains any of the keywords.
 */
public class HouseholdContainsKeywordsPredicate implements Predicate<Household> {
    private final List<String> keywords;
    private final boolean isNumberSearch; // Flag to indicate if we are doing a number-based search

    /**
     * Constructs a {@code HouseholdContainsKeywordsPredicate} with the given keywords and search mode.
     *
     * @param keywords       A list of keywords to match against household attributes.
     * @param isNumberSearch {@code true} if the search should be numeric-based; {@code false} otherwise.
     */
    public HouseholdContainsKeywordsPredicate(List<String> keywords, boolean isNumberSearch) {
        this.keywords = keywords;
        this.isNumberSearch = isNumberSearch;
    }
    /**
     * Tests a {@code Household} to determine if it matches the search criteria.
     * The matching criteria vary based on whether the search is by number or by keywords.
     *
     * @param household The household to test against.
     * @return true if the household matches the search criteria, false otherwise.
     */
    @Override
    public boolean test(Household household) {
        if (isNumberSearch) {
            // Search by household ID or phone numbers
            return testByNumber(household);
        } else {
            // Search by name, address, or tags
            return keywords.stream()
                    .anyMatch(keyword -> Stream.of(household.getName().toString(),
                                    household.getAddress().toString(),
                                    household.getTags().toString())
                            .anyMatch(field -> field.toLowerCase().contains(keyword.toLowerCase())));
        }
    }
    /**
     * Tests a {@code Household} against the search keywords based on numbers.
     * Matches if the household ID or contact number contains any of the keywords.
     *
     * @param household The household to test against.
     * @return true if the household's ID or contact number contains any of the keywords, false otherwise.
     */
    private boolean testByNumber(Household household) {
        // Test if any of the numbers (household ID or phone numbers) match the keyword
        String householdId = household.getId().toString(); // Assuming Household has a getId method
        String phoneNumber = household.getContact().toString(); // Assuming Household has getPhoneNumber method

        return keywords.stream()
                .anyMatch(keyword -> householdId.contains(keyword) || phoneNumber.contains(keyword));
    }
    /**
     * Checks equality between this predicate and another object.
     * Two predicates are equal if they have the same set of keywords.
     *
     * @param other The other object to compare against.
     * @return true if both objects are identical or have equivalent keyword sets, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof HouseholdContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((HouseholdContainsKeywordsPredicate) other).keywords)); // state check
    }
}

package seedu.address.model.household;

import java.util.List;
import java.util.function.Predicate;
import seedu.address.model.Household.*;

/**
 * Tests that a {@code Household}'s attributes matches any of the keywords given.
 */
public class HouseholdContainsKeywordsPredicate implements Predicate<Household> {
    private final List<String> keywords;

    public HouseholdContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Household household) {
        return keywords.stream()
                .anyMatch(keyword -> 
                    household.getName().fullName.toLowerCase().contains(keyword.toLowerCase())
                    || household.getAddress().value.toLowerCase().contains(keyword.toLowerCase())
                    || household.getTags().stream()
                            .anyMatch(tag -> tag.tagName.toLowerCase()
                                    .contains(keyword.toLowerCase())));
    }
    

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof HouseholdContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((HouseholdContainsKeywordsPredicate) other).keywords)); // state check
    }
} 
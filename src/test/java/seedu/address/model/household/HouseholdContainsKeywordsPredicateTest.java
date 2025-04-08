package seedu.address.model.household;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;

public class HouseholdContainsKeywordsPredicateTest {

    /**
     * Helper method to create a dummy Household.
     */
    private Household createDummyHousehold() {
        Name name = new Name("Test Family");
        Address address = new Address("123 Test Street");
        // Use a valid contact number: 8 digits starting with 9.
        Contact contact = new Contact("91234567");
        HouseholdId id = HouseholdId.fromString("H000001");
        Set<Tag> tags = Set.of(new Tag("urgent"));
        return new Household(name, address, contact, id, tags);
    }

    @Test
    public void testNonNumberSearchMatchesWhenKeywordInNameAddressOrTags() {
        Household household = createDummyHousehold();
        // Test matching by name.
        List<String> keywords = List.of("Test", "Family");
        HouseholdContainsKeywordsPredicate predicate =
                new HouseholdContainsKeywordsPredicate(keywords, false);
        assertTrue(predicate.test(household));

        // Test matching by address (case-insensitive).
        keywords = List.of("test street");
        predicate = new HouseholdContainsKeywordsPredicate(keywords, false);
        assertTrue(predicate.test(household));

        // Test matching by tags (using the set's string representation).
        keywords = List.of("urgent");
        predicate = new HouseholdContainsKeywordsPredicate(keywords, false);
        assertTrue(predicate.test(household));
    }

    @Test
    public void testNonNumberSearchNoMatchWhenKeywordNotPresent() {
        Household household = createDummyHousehold();
        List<String> keywords = List.of("nonexistent");
        HouseholdContainsKeywordsPredicate predicate =
                new HouseholdContainsKeywordsPredicate(keywords, false);
        assertFalse(predicate.test(household));
    }

    @Test
    public void testNumberSearchMatchesByHouseholdId() {
        Household household = createDummyHousehold();
        // Household ID "H000001" should match the keyword "000001"
        List<String> keywords = List.of("000001");
        HouseholdContainsKeywordsPredicate predicate =
                new HouseholdContainsKeywordsPredicate(keywords, true);
        assertTrue(predicate.test(household));
    }

    @Test
    public void testNumberSearchMatchesByContact() {
        Household household = createDummyHousehold();
        // The contact "91234567" should match the keyword "9123".
        List<String> keywords = List.of("9123");
        HouseholdContainsKeywordsPredicate predicate =
                new HouseholdContainsKeywordsPredicate(keywords, true);
        assertTrue(predicate.test(household));
    }

    @Test
    public void testNumberSearchNoMatchWhenKeywordsNotPresent() {
        Household household = createDummyHousehold();
        List<String> keywords = List.of("9999");
        HouseholdContainsKeywordsPredicate predicate =
                new HouseholdContainsKeywordsPredicate(keywords, true);
        assertFalse(predicate.test(household));
    }

    @Test
    public void testEmptyKeywordsReturnsFalse() {
        Household household = createDummyHousehold();
        List<String> keywords = List.of(); // empty keyword list
        HouseholdContainsKeywordsPredicate predicate =
                new HouseholdContainsKeywordsPredicate(keywords, false);
        assertFalse(predicate.test(household));
    }

    @Test
    public void testEqualsMethod() {
        List<String> keywords1 = List.of("test", "family");
        List<String> keywords2 = List.of("test", "family");
        List<String> keywords3 = List.of("different");

        HouseholdContainsKeywordsPredicate predicate1 =
                new HouseholdContainsKeywordsPredicate(keywords1, false);
        HouseholdContainsKeywordsPredicate predicate2 =
                new HouseholdContainsKeywordsPredicate(keywords2, false);
        HouseholdContainsKeywordsPredicate predicate3 =
                new HouseholdContainsKeywordsPredicate(keywords3, false);
        HouseholdContainsKeywordsPredicate predicate4 =
                new HouseholdContainsKeywordsPredicate(keywords1, true);

        // Check same object.
        assertEquals(predicate1, predicate1);
        // Predicates with equal keyword list are equal regardless of isNumberSearch flag.
        assertEquals(predicate1, predicate2);
        assertEquals(predicate1, predicate4);
        // Different keywords result in inequality.
        assertNotEquals(predicate1, predicate3);
        // Not equal to null or a different type.
        assertNotEquals(predicate1, null);
        assertNotEquals(predicate1, "some string");
    }
}



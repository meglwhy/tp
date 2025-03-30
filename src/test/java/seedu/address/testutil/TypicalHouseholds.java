package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import seedu.address.model.HouseholdBook;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;

/**
 * A utility class containing a list of {@code Household} objects to be used in tests.
 */
public class TypicalHouseholds {
    public static final Household ALICE_HOUSEHOLD = new Household(
            new Name("Alice Family"),
            new Address("123 Alice Road"),
            new Contact("91234567"),
            new HouseholdId("H000001"),
            new HashSet<>());

    public static final Household BOB_HOUSEHOLD = new Household(
            new Name("Bob Family"),
            new Address("456 Bob Street"),
            new Contact("98765432"),
            new HouseholdId("H000002"),
            new HashSet<>());

    private TypicalHouseholds() {} // prevents instantiation

    /**
     * Returns a {@code HouseholdBook} with all the typical households.
     */
    public static HouseholdBook getTypicalHouseholdBook() {
        HouseholdBook ab = new HouseholdBook();
        for (Household household : getTypicalHouseholds()) {
            ab.addHousehold(household);
        }
        return ab;
    }

    public static List<Household> getTypicalHouseholds() {
        return new ArrayList<>(Arrays.asList(ALICE_HOUSEHOLD, BOB_HOUSEHOLD));
    }
}

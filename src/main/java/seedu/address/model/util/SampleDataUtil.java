package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.HouseholdBook;
import seedu.address.model.ReadOnlyHouseholdBook;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionNote;
import seedu.address.model.session.SessionTime;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code HouseholdBook} with sample data.
 */
public class SampleDataUtil {
    public static Household[] getSampleHouseholds() {
        return new Household[] {
            new Household(new Name("Tan Family"), new Address("Blk 30 Geylang Street 29, #06-40"),
                new Contact("91234567"), HouseholdId.fromString("H000001"),
                getTagSet("elderly", "priority")),
            new Household(new Name("Lee Family"), new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                new Contact("92345678"), HouseholdId.fromString("H000002"),
                getTagSet("children", "needy")),
            new Household(new Name("Wong Family"), new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                new Contact("93456789"), HouseholdId.fromString("H000003"),
                getTagSet("elderly")),
        };
    }

    public static Session[] getSampleSessions() {
        return new Session[] {
            new Session(HouseholdId.fromString("H000001"), new SessionDate("2024-03-15"),
                new SessionTime("14:00"), new SessionNote("First visit completed")),
            new Session(HouseholdId.fromString("H000002"), new SessionDate("2024-03-16"),
                new SessionTime("10:00"), new SessionNote("Follow-up required")),
            new Session(HouseholdId.fromString("H000003"), new SessionDate("2024-03-17"),
                new SessionTime("15:30"))
        };
    }

    public static ReadOnlyHouseholdBook getSampleHouseholdBook() {
        HouseholdBook sampleHb = new HouseholdBook();
        for (Household sampleHousehold : getSampleHouseholds()) {
            sampleHb.addHousehold(sampleHousehold);
        }
        for (Session sampleSession : getSampleSessions()) {
            sampleHb.addSessionToHousehold(sampleSession.getHouseholdId(), sampleSession);
        }
        return sampleHb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }
}

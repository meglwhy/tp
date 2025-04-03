package seedu.address.model.household;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.session.Session;
import seedu.address.model.tag.Tag;

/**
 * Represents a Household in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Household {

    // Comparator to sort sessions by date (descending), then time (descending)
    private static final Comparator<Session> SESSION_COMPARATOR = (s1, s2) -> {
        int dateCompare = s2.getDate().compareTo(s1.getDate()); // Newest date first
        if (dateCompare != 0) {
            return dateCompare;
        }
        return s2.getTime().compareTo(s1.getTime()); // Newest time first
    };
    private final Name name;
    private final Address address;
    private final Contact contact;
    private final HouseholdId id;
    private final ObservableList<Session> sessions = FXCollections.observableArrayList();
    private final Set<Tag> tags;

    /**
     * Creates a Household object with an auto-generated ID.
     * All fields except tags must be non-null.
     */
    public Household(Name name, Address address, Contact contact) {
        this(name, address, contact, HouseholdId.generateNewId(), new HashSet<>());
    }

    /**
     * Creates a Household object with a specific ID and tags.
     * This constructor should only be used when loading data from storage.
     */
    public Household(Name name, Address address, Contact contact, HouseholdId id, Set<Tag> tags) {
        requireNonNull(name);
        requireNonNull(address);
        requireNonNull(contact);
        requireNonNull(id);
        requireNonNull(tags);
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.id = id;
        this.tags = new HashSet<>(tags); // defensive copy
    }

    public Name getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public Contact getContact() {
        return contact;
    }

    public HouseholdId getId() {
        return id;
    }


    /**
     * Returns the actual (sorted) sessions list.
     * The list is kept sorted using the defined comparator.
     */
    public ObservableList<Session> getSessions() {
        // Sort in place so that the underlying list is also sorted.
        FXCollections.sort(sessions, SESSION_COMPARATOR);
        return sessions;
    }

    public Set<Tag> getTags() {
        return new HashSet<>(tags); // defensive copy
    }

    /**
     * Adds a session to this household.
     */
    public void addSession(Session session) {
        requireNonNull(session);
        sessions.add(session);
    }

    /**
     * Returns true if both households have the same identifying fields.
     * This defines if adding/editing would result in duplicates.
     */

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Household)) {
            return false;
        }

        Household otherHousehold = (Household) other;

        return this.getId().equals(otherHousehold.getId())
                || this.getName().toString().equalsIgnoreCase(otherHousehold.getName().toString())
                || this.getAddress().toString().equalsIgnoreCase(otherHousehold.getAddress().toString())
                || this.getContact().toString().equals(otherHousehold.getContact().toString());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Household ")
                .append(id)
                .append(": ")
                .append(name)
                .append(" at ")
                .append(address)
                .append(" (Contact: ")
                .append(contact)
                .append(")");
        if (!tags.isEmpty()) {
            builder.append(" Tags: ");
            tags.forEach(tag -> builder.append(tag).append(" "));
        }
        return builder.toString();
    }
}

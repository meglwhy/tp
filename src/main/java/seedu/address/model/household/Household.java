package seedu.address.model.household;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Set;

import seedu.address.model.session.Session;
import seedu.address.model.tag.Tag;

/**
 * Represents a Household in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Household {
    private final Name name;
    private final Address address;
    private final Contact contact;
    private final HouseholdId id;
    private final List<Session> sessions;
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
        this.sessions = new ArrayList<>();
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

    public List<Session> getSessions() {
        return new ArrayList<>(sessions);
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
     * Updates the old session with the new session.
     * The old session must exist in this household.
     */
    public void updateSession(Session oldSession, Session newSession) {
        requireNonNull(oldSession);
        requireNonNull(newSession);
        
        int index = sessions.indexOf(oldSession);
        if (index != -1) {
            sessions.set(index, newSession);
        }
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

        return this.getName().toString().equalsIgnoreCase(otherHousehold.getName().toString()) ||
                this.getAddress().toString().equalsIgnoreCase(otherHousehold.getAddress().toString()) ||
                this.getContact().toString().equals(otherHousehold.getContact().toString());
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
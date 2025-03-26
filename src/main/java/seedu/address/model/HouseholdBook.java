package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;

/**
 * Wraps all data at the household-book level
 * Duplicates are not allowed (by household ID comparison)
 */
public class HouseholdBook implements ReadOnlyHouseholdBook {
    private final ObservableList<Household> households = FXCollections.observableArrayList();
    private final ObservableList<Household> unmodifiableHouseholds =
            FXCollections.unmodifiableObservableList(households);

    private final ObservableList<Session> sessions = FXCollections.observableArrayList();

    public HouseholdBook() {}

    /**
     * Creates a HouseholdBook using the Households and Sessions in the {@code toBeCopied}
     */
    public HouseholdBook(ReadOnlyHouseholdBook toBeCopied) {
        requireNonNull(toBeCopied);
        households.addAll(toBeCopied.getHouseholdList());
        sessions.addAll(toBeCopied.getSessionList());
    }
    /**
     * Resets the existing data of this {@code HouseholdBook} with an empty book.
     */
    public void resetData(HouseholdBook newData) {
        requireNonNull(newData);
        households.clear();
        sessions.clear();
        households.addAll(newData.getHouseholdList());
        sessions.addAll(newData.getSessionList());
    }

    /**
     * Returns true if a household with the same name, address or contact exists in the household book.
     * Excludes households with the same ID as the input household.
     */
    public boolean hasHousehold(Household household) {
        requireNonNull(household);
        return households.stream()
                .filter(existingHousehold -> !existingHousehold.getId().equals(household.getId()))
                .anyMatch(existingHousehold -> existingHousehold.equals(household));
    }

    /**
     * Adds a household to the household book.
     * The household must not already exist in the household book.
     */
    public void addHousehold(Household household) {
        requireNonNull(household);
        households.add(household);
    }

    /**
     * Removes {@code household} and it's sessions from this {@code HouseholdBook}.
     * {@code household} must exist in the household book.
     */
    public void removeHousehold(Household household) {
        requireNonNull(household);

        // Remove associated sessions from the global session list
        sessions.removeIf(session -> household.getSessions().contains(session));

        // Remove the household itself
        households.remove(household);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Household> getHouseholdList() {
        return unmodifiableHouseholds;
    }
    /**
     * Returns true if a household with the given ID exists in the household book.
     */
    public boolean hasHouseholdId(HouseholdId id) {
        requireNonNull(id);
        return households.stream()
                .anyMatch(household -> household.getId().equals(id));
    }
    /**
     * Adds a session to the specified household.
     * The household must exist in the household book.
     */
    public void addSessionToHousehold(HouseholdId householdId, Session session) {
        requireNonNull(householdId);
        requireNonNull(session);
        households.stream()
                .filter(h -> h.getId().equals(householdId))
                .findFirst()
                .ifPresent(h -> h.addSession(session));
        sessions.add(session);
    }
    /**
     * Returns the conflicting session if one exists.
     * Useful for providing more detailed error messages.
     */
    public Optional<Session> getConflictingSession(Session session, Session... exclude) {
        requireNonNull(session);
        return households.stream()
                .flatMap(h -> h.getSessions().stream())
                .filter(existingSession -> !List.of(exclude).contains(existingSession))
                .filter(existingSession ->
                        existingSession.getDate().equals(session.getDate())
                        && existingSession.getTime().equals(session.getTime()))
                .findFirst();
    }
    /**
     * Removes a session identified by the given session ID from both the corresponding household and
     * the global session list.
     *
     * <p>This method performs the following steps:</p>
     * <ul>
     *   <li>Finds the household that contains the session and removes it from that household.</li>
     *   <li>Removes the session from the global session list, if applicable.</li>
     * </ul>
     *
     * @param sessionId The ID of the session to be removed.
     */
    public void removeSessionById(String sessionId) {
        // 1) Remove from the household that has this session
        households.stream()
                .filter(h -> h.getSessions().stream().anyMatch(s -> s.getSessionId().equals(sessionId)))
                .findFirst()
                .ifPresent(h -> {
                    h.getSessions().removeIf(s -> s.getSessionId().equals(sessionId));
                });

        // 2) Also remove from the global sessions list (if you keep one)
        sessions.removeIf(s -> s.getSessionId().equals(sessionId));
    }
    /**
     * Returns the household with the given ID if it exists.
     */
    public Optional<Household> getHouseholdById(HouseholdId id) {
        requireNonNull(id);
        return households.stream()
                .filter(household -> household.getId().equals(id))
                .findFirst();
    }

    /**
     * Replaces the given household with the updated household.
     * The household must exist in the household book.
     */
    public void updateHousehold(Household target, Household editedHousehold) {
        requireNonNull(target);
        requireNonNull(editedHousehold);

        int index = households.indexOf(target);
        if (index == -1) {
            throw new IllegalArgumentException("Household does not exist in the household book");
        }

        households.set(index, editedHousehold);
    }

    public ObservableList<Session> getSessionList() {
        return FXCollections.unmodifiableObservableList(sessions);
    }

    /**
     * Returns true if a session with the same identity as {@code session} exists in the household book.
     */
    public boolean hasSession(Session session) {
        requireNonNull(session);
        return sessions.contains(session);
    }

    /**
     * Returns an unmodifiable list of all sessions across all households.
     */
    public List<Session> getSessions() {
        List<Session> allSessions = new ArrayList<>();
        for (Household household : households) {
            allSessions.addAll(household.getSessions());
        }
        return Collections.unmodifiableList(allSessions);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof HouseholdBook // instanceof handles nulls
                && households.equals(((HouseholdBook) other).households));
    }

    @Override
    public int hashCode() {
        return households.hashCode();
    }
}

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
 * Wraps all data at the household-book level.
 * Duplicates are not allowed (by household ID comparison).
 * This class serves as the main data model for storing household and session information.
 * It maintains collections of households and their associated sessions, providing methods
 * to add, update, and retrieve this data.
 */
public class HouseholdBook implements ReadOnlyHouseholdBook {
    /** The list of households in this book. */
    private final ObservableList<Household> households = FXCollections.observableArrayList();

    /** An unmodifiable view of the household list that prevents external modification. */
    private final ObservableList<Household> unmodifiableHouseholds =
            FXCollections.unmodifiableObservableList(households);

    /** The list of all sessions across all households. */
    private final ObservableList<Session> sessions = FXCollections.observableArrayList();

    /**
     * Creates an empty HouseholdBook.
     */
    public HouseholdBook() {}

    /**
     * Creates a HouseholdBook using the Households and Sessions in the {@code toBeCopied}.
     * @param toBeCopied the household book to copy from
     */
    public HouseholdBook(ReadOnlyHouseholdBook toBeCopied) {
        requireNonNull(toBeCopied);
        households.addAll(toBeCopied.getHouseholdList());
        sessions.addAll(toBeCopied.getSessionList());
    }
    /**
     * Resets the existing data of this {@code HouseholdBook} with data from {@code newData}.
     * This clears all households and sessions and replaces them with those from newData.
     * @param newData the new household book data
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
     * @param household the household to check for duplicates
     * @return true if a duplicate household exists, false otherwise
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
     * @param household the household to add
     */
    public void addHousehold(Household household) {
        requireNonNull(household);
        households.add(household);
    }

    /**
     * Removes {@code household} and its sessions from this {@code HouseholdBook}.
     * {@code household} must exist in the household book.
     * @param household the household to remove
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
     * @return an unmodifiable view of the household list
     */
    @Override
    public ObservableList<Household> getHouseholdList() {
        return unmodifiableHouseholds;
    }

    /**
     * Returns true if a household with the given ID exists in the household book.
     * @param id the household ID to check
     * @return true if a household with the given ID exists, false otherwise
     */
    public boolean hasHouseholdId(HouseholdId id) {
        requireNonNull(id);
        return households.stream()
                .anyMatch(household -> household.getId().equals(id));
    }

    /**
     * Adds a session to the specified household.
     * The household must exist in the household book.
     * @param householdId the ID of the household to add the session to
     * @param session the session to add
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
     * @param session the session to check for conflicts
     * @param exclude sessions to exclude from the conflict check
     * @return an Optional containing the conflicting session, or empty if none exists
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
        households.stream()
                .filter(h -> h.getSessions().stream().anyMatch(s -> s.getSessionId().equals(sessionId)))
                .findFirst()
                .ifPresent(h -> {
                    h.getSessions().removeIf(s -> s.getSessionId().equals(sessionId));
                });
        sessions.removeIf(s -> s.getSessionId().equals(sessionId));
    }

    /**
     * Returns the household with the given ID if it exists.
     * @param id the household ID to look for
     * @return an Optional containing the household if found, or empty if not found
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
     * @param target the household to replace
     * @param editedHousehold the updated household
     * @throws IllegalArgumentException if the household does not exist
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

    /**
     * Returns an unmodifiable view of the global session list.
     * @return an unmodifiable ObservableList of all sessions
     */
    @Override
    public ObservableList<Session> getSessionList() {
        return FXCollections.unmodifiableObservableList(sessions);
    }

    /**
     * Returns true if a session with the same identity as {@code session} exists in the household book.
     * @param session the session to check
     * @return true if the session exists, false otherwise
     */
    public boolean hasSession(Session session) {
        requireNonNull(session);
        return sessions.contains(session);
    }

    /**
     * Returns an unmodifiable list of all sessions across all households.
     * @return an unmodifiable list of all sessions
     */
    public List<Session> getSessions() {
        List<Session> allSessions = new ArrayList<>();
        for (Household household : households) {
            allSessions.addAll(household.getSessions());
        }
        return Collections.unmodifiableList(allSessions);
    }

    /**
     * Checks if this HouseholdBook is equal to another object.
     * Two HouseholdBooks are equal if they have the same households.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof HouseholdBook // instanceof handles nulls
                && households.equals(((HouseholdBook) other).households));
    }

    /**
     * Returns the hash code of this HouseholdBook.
     */
    @Override
    public int hashCode() {
        return households.hashCode();
    }
}

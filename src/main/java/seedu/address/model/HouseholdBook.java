package seedu.address.model;

import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.stream.Collectors;
import java.util.Optional;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.household.HouseholdContainsKeywordsPredicate;
import seedu.address.commons.core.index.Index;

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
     * Replaces the contents of the household list with {@code households}.
     */
    public void setHouseholds(List<Household> households) {
        this.households.setAll(households);
    }

    /**
     * Resets the existing data of this {@code HouseholdBook} with an empty book.
     */
    public void resetData(HouseholdBook newData) {
        requireNonNull(newData);
        households.clear();
        households.addAll(newData.getHouseholdList());
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
     * Removes {@code key} from this {@code HouseholdBook}.
     * {@code key} must exist in the household book.
     */
    public void removeHousehold(Household key) {
        households.remove(key);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Household> getHouseholdList() {
        return unmodifiableHouseholds;
    }

    /**
     * Returns a list of households that match the given predicate.
     */
    public List<Household> findHouseholds(HouseholdContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);
        return households.stream()
                .filter(predicate)
                .collect(Collectors.toList());
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
     * Returns true if there exists a session that conflicts with the given session.
     * A conflict occurs when two sessions have the same date and time.
     * Optionally excludes a session from the check (useful for edit operations).
     */
    public boolean hasConflictingSession(Session session, Session... exclude) {
        requireNonNull(session);
        
        return households.stream()
                .flatMap(h -> h.getSessions().stream())
                .filter(existingSession -> !List.of(exclude).contains(existingSession))
                .anyMatch(existingSession ->
                        existingSession.getDate().equals(session.getDate())
                        && existingSession.getTime().equals(session.getTime()));
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
     * Returns the session at the specified index.
     * @throws CommandException if index is invalid
     */
    public Optional<Session> getSession(Index index) {
        requireNonNull(index);
        List<Session> allSessions = households.stream()
                .flatMap(h -> h.getSessions().stream())
                .collect(Collectors.toList());
        
        if (index.getZeroBased() >= allSessions.size()) {
            return Optional.empty();
        }
        return Optional.of(allSessions.get(index.getZeroBased()));
    }

    /**
     * Updates the old session with the new session.
     */
    public void updateSession(Session oldSession, Session newSession) {
        requireAllNonNull(oldSession, newSession);
        
        households.stream()
                .filter(h -> h.getSessions().contains(oldSession))
                .findFirst()
                .ifPresent(h -> h.updateSession(oldSession, newSession));
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

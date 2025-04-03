package seedu.address.model;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.household.Household;
import seedu.address.model.session.Session;

/**
 * Unmodifiable view of a household book.
 *
 * <p>This interface provides a read-only view of the household book data model,
 * allowing components to access household and session data without the ability to modify it.</p>
 *
 * <p>It serves as a key part of the application's defensive programming strategy, ensuring that
 * components that only need to read data cannot accidentally modify it. This helps maintain
 * data integrity throughout the application.</p>
 *
 * <p>Implementations of this interface should ensure that all returned collections are unmodifiable
 * to prevent any external modifications to the underlying data.</p>
 */
public interface ReadOnlyHouseholdBook {

    /**
     * Returns an unmodifiable view of the households list.
     * This list will not contain any duplicate households.
     *
     * <p>Households are uniquely identified by their ID, name, address, and contact information.
     * The returned list provides a snapshot of all households currently in the system.</p>
     *
     * <p>This method is intended for UI display and data retrieval operations.</p>
     *
     * @return An unmodifiable ObservableList containing all households.
     */
    ObservableList<Household> getHouseholdList();

    /**
     * Returns an unmodifiable view of the sessions list.
     * This list will not contain any duplicate sessions.
     *
     * <p>Sessions are uniquely identified by their session ID.
     * The returned list provides a snapshot of all sessions across all households.</p>
     *
     * <p>This method is useful for global operations on sessions, such as filtering
     * or searching across all sessions regardless of household.</p>
     *
     * @return An unmodifiable ObservableList containing all sessions.
     */
    ObservableList<Session> getSessionList();

    /**
     * Returns true if a household with the same identity as {@code household} exists in the household book.
     *
     * <p>This method checks if a household with the same identifying information already exists in the system.
     * It compares households based on their ID, name, address, and contact information.</p>
     *
     * <p>This method is useful for validation to prevent duplicate households from being added.</p>
     *
     * @param household The household to check for existence.
     * @return true if a matching household exists, false otherwise.
     */
    boolean hasHousehold(Household household);

    /**
     * Returns an unmodifiable list of all sessions across all households.
     *
     * <p>Unlike {@link #getSessionList()}, this method returns a standard List rather than
     * an ObservableList. This is useful for operations that don't require
     * the reactive properties of ObservableLists.</p>
     *
     * <p>The returned list contains all sessions from all households, providing a
     * comprehensive view of session data in the system.</p>
     *
     * @return An unmodifiable List containing all sessions.
     */
    List<Session> getSessions();
}

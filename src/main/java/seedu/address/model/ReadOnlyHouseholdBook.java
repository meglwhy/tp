package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.household.Household;
import seedu.address.model.session.Session;

/**
 * Unmodifiable view of a household book
 */
public interface ReadOnlyHouseholdBook {

    /**
     * Returns an unmodifiable view of the households list.
     * This list will not contain any duplicate households.
     */
    ObservableList<Household> getHouseholdList();

    /**
     * Returns an unmodifiable view of the sessions list.
     * This list will not contain any duplicate sessions.
     */
    ObservableList<Session> getSessionList();

    /**
     * Returns true if a household with the same identity as {@code household} exists in the household book.
     */
    boolean hasHousehold(Household household);

    /**
     * Returns true if a session with the same identity as {@code session} exists in the household book.
     */
    boolean hasSession(Session session);
}

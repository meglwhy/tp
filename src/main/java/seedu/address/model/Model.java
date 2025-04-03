package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.household.Household;
import seedu.address.model.session.Session;

/**
 * The API of the Model component.
 *
 * <p>The Model component represents the data model of the application and defines the operations
 * that can be performed on this data. It provides methods to access and modify households,
 * sessions, and user preferences.</p>
 *
 * <p>This interface serves as the main point of interaction between the data layer and other
 * components of the application. It abstracts the underlying implementation details and
 * provides a clean API for working with application data.</p>
 */
public interface Model {
    /**
     * Returns the HouseholdBook containing all household and session data.
     *
     * @return The household book data model.
     */
    HouseholdBook getHouseholdBook();

    /**
     * Returns the user preferences GUI settings.
     *
     * @return The current GUI settings from user preferences.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user preferences GUI settings.
     * This updates the display settings like window size and position.
     *
     * @param guiSettings The new GUI settings to use.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user preferences.
     * User preferences contain persistent application settings like file paths and GUI settings.
     *
     * @return The read-only user preferences.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user preferences household book file path.
     * This is the path where household data is stored.
     *
     * @return The file path for the household book data.
     */
    Path getHouseholdBookFilePath();

    /**
     * Sets the user preferences household book file path.
     * This changes where household data will be saved.
     *
     * @param householdBookFilePath The new file path for saving household data.
     */
    void setHouseholdBookFilePath(Path householdBookFilePath);

    /**
     * Returns an unmodifiable view of the filtered household list.
     * This list reflects the current filtering applied to households.
     *
     * @return An observable list of households that pass the current filter.
     */
    ObservableList<Household> getFilteredHouseholdList();

    /**
     * Returns an unmodifiable view of the filtered session list.
     * This list reflects the current filtering applied to sessions.
     *
     * @return An observable list of sessions that pass the current filter.
     */
    ObservableList<Session> getFilteredSessionList();

    /**
     * Updates the filter of the filtered household list to filter by the given {@code predicate}.
     * This changes which households are displayed in the UI.
     *
     * @param predicate The predicate to use for filtering households.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredHouseholdList(Predicate<Household> predicate);

    /**
     * Updates the filter of the filtered session list to filter by the given {@code predicate}.
     * This changes which sessions are displayed in the UI.
     *
     * @param predicate The predicate to use for filtering sessions.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredSessionList(Predicate<Session> predicate);
}

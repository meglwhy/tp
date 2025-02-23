package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.household.Household;
import seedu.address.model.session.Session;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Household> PREDICATE_SHOW_ALL_HOUSEHOLDS = unused -> true;
    Predicate<Session> PREDICATE_SHOW_ALL_SESSIONS = unused -> true;

    /** Returns the HouseholdBook */
    HouseholdBook getHouseholdBook();

    /** Returns the user prefs' GUI settings. */
    GuiSettings getGuiSettings();

    /** Sets the user prefs' GUI settings. */
    void setGuiSettings(GuiSettings guiSettings);

    /** Returns the user prefs */
    ReadOnlyUserPrefs getUserPrefs();

    /** Returns the user prefs' household book file path. */
    Path getHouseholdBookFilePath();

    /** Sets the user prefs' household book file path. */
    void setHouseholdBookFilePath(Path householdBookFilePath);

    /** Returns an unmodifiable view of the filtered household list */
    ObservableList<Household> getFilteredHouseholdList();

    /** Returns an unmodifiable view of the filtered session list */
    ObservableList<Session> getFilteredSessionList();

    /**
     * Updates the filter of the filtered household list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredHouseholdList(Predicate<Household> predicate);

    /**
     * Updates the filter of the filtered session list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredSessionList(Predicate<Session> predicate);
}

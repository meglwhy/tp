package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.household.Household;
import seedu.address.model.session.Session;

/**
 * Represents the in-memory model of the household book data.
 *
 * <p>This class is the concrete implementation of the {@link Model} interface.
 * It manages the household book data in memory and handles operations such as
 * filtering, data access, and data modifications.</p>
 *
 * <p>ModelManager maintains filtered lists of households and sessions for UI display,
 * and provides methods to modify these filters.</p>
 */
public class ModelManager implements Model {
    /** Logger for this class */
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    /** The main data model containing households and sessions */
    private final HouseholdBook householdBook;

    /** User preferences for the application */
    private final UserPrefs userPrefs;

    /** A filtered view of households for UI display */
    private final FilteredList<Household> filteredHouseholds;

    /** A filtered view of sessions for UI display */
    private final FilteredList<Session> filteredSessions;

    /**
     * Initializes a ModelManager with the given householdBook and userPrefs.
     * This constructor creates a new model with data from the provided sources.
     *
     * @param householdBook the household book data to initialize with
     * @param userPrefs the user preferences to initialize with
     */
    public ModelManager(ReadOnlyHouseholdBook householdBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(householdBook, userPrefs);

        logger.fine("Initializing with household book: " + householdBook + " and user prefs " + userPrefs);

        this.householdBook = new HouseholdBook(householdBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredHouseholds = new FilteredList<>(this.householdBook.getHouseholdList());
        filteredSessions = new FilteredList<>(this.householdBook.getSessionList());
    }

    /**
     * Initializes a ModelManager with default household book and user preferences.
     * This constructor creates a new model with empty data and default settings.
     */
    public ModelManager() {
        this(new HouseholdBook(), new UserPrefs());
    }

    /**
     * Returns the household book containing all data.
     *
     * @return The household book data model.
     */
    @Override
    public HouseholdBook getHouseholdBook() {
        return householdBook;
    }

    /**
     * Returns the file path where the household book data is stored.
     * This path is retrieved from user preferences.
     *
     * @return The file path for storing household book data.
     */
    @Override
    public Path getHouseholdBookFilePath() {
        return userPrefs.getHouseholdBookFilePath();
    }

    /**
     * Sets the file path for storing the household book data.
     * This updates the path in user preferences.
     *
     * @param householdBookFilePath The new file path for storing data.
     * @throws NullPointerException if {@code householdBookFilePath} is null.
     */
    @Override
    public void setHouseholdBookFilePath(Path householdBookFilePath) {
        requireNonNull(householdBookFilePath);
        userPrefs.setHouseholdBookFilePath(householdBookFilePath);
    }

    /**
     * Returns the user preferences.
     *
     * @return The read-only user preferences.
     */
    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    /**
     * Returns the current GUI settings from user preferences.
     *
     * @return The current GUI settings.
     */
    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    /**
     * Sets the GUI settings in user preferences.
     *
     * @param guiSettings The new GUI settings.
     * @throws NullPointerException if {@code guiSettings} is null.
     */
    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    //=========== Filtered Household List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the filtered household list.
     * This list is filtered according to the currently set predicate.
     *
     * @return An unmodifiable view of the filtered household list.
     */
    @Override
    public ObservableList<Household> getFilteredHouseholdList() {
        return filteredHouseholds;
    }

    /**
     * Updates the filter of the filtered household list to filter by the given predicate.
     * This changes which households are visible in the UI.
     *
     * @param predicate The predicate to filter households.
     * @throws NullPointerException if {@code predicate} is null.
     */
    @Override
    public void updateFilteredHouseholdList(Predicate<Household> predicate) {
        requireNonNull(predicate);
        filteredHouseholds.setPredicate(predicate);
    }

    //=========== Filtered Session List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the filtered session list.
     * This list is filtered according to the currently set predicate.
     *
     * @return An unmodifiable view of the filtered session list.
     */
    @Override
    public ObservableList<Session> getFilteredSessionList() {
        return filteredSessions;
    }

    /**
     * Updates the filter of the filtered session list to filter by the given predicate.
     * This changes which sessions are visible in the UI.
     *
     * @param predicate The predicate to filter sessions.
     * @throws NullPointerException if {@code predicate} is null.
     */
    @Override
    public void updateFilteredSessionList(Predicate<Session> predicate) {
        requireNonNull(predicate);
        filteredSessions.setPredicate(predicate);
    }

    /**
     * Returns true if this model equals the other model.
     * Two models are equal if they have the same household book, user preferences,
     * filtered households, and filtered sessions.
     *
     * @param obj The object to compare against.
     * @return true if both models are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ModelManager)) {
            return false;
        }

        ModelManager other = (ModelManager) obj;
        return householdBook.equals(other.householdBook)
                && userPrefs.equals(other.userPrefs)
                && filteredHouseholds.equals(other.filteredHouseholds)
                && filteredSessions.equals(other.filteredSessions);
    }
}

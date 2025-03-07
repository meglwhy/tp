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
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final HouseholdBook householdBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Household> filteredHouseholds;
    private final FilteredList<Session> filteredSessions;

    /**
     * Initializes a ModelManager with the given householdBook and userPrefs.
     */
    public ModelManager(ReadOnlyHouseholdBook householdBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(householdBook, userPrefs);

        logger.fine("Initializing with household book: " + householdBook + " and user prefs " + userPrefs);

        this.householdBook = new HouseholdBook(householdBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredHouseholds = new FilteredList<>(this.householdBook.getHouseholdList());
        filteredSessions = new FilteredList<>(this.householdBook.getSessionList());
    }

    public ModelManager() {
        this(new HouseholdBook(), new UserPrefs());
    }

    @Override
    public HouseholdBook getHouseholdBook() {
        return householdBook;
    }

    @Override
    public Path getHouseholdBookFilePath() {
        return userPrefs.getHouseholdBookFilePath();
    }

    @Override
    public void setHouseholdBookFilePath(Path householdBookFilePath) {
        requireNonNull(householdBookFilePath);
        userPrefs.setHouseholdBookFilePath(householdBookFilePath);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    //=========== Filtered Household List Accessors =============================================================

    @Override
    public ObservableList<Household> getFilteredHouseholdList() {
        return filteredHouseholds;
    }

    @Override
    public void updateFilteredHouseholdList(Predicate<Household> predicate) {
        requireNonNull(predicate);
        filteredHouseholds.setPredicate(predicate);
    }

    //=========== Filtered Session List Accessors =============================================================

    @Override
    public ObservableList<Session> getFilteredSessionList() {
        return filteredSessions;
    }

    @Override
    public void updateFilteredSessionList(Predicate<Session> predicate) {
        requireNonNull(predicate);
        filteredSessions.setPredicate(predicate);
    }

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

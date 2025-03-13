package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyHouseholdBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of HouseholdBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private HouseholdBookStorage householdBookStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code HouseholdBookStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(HouseholdBookStorage householdBookStorage, UserPrefsStorage userPrefsStorage) {
        this.householdBookStorage = householdBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================ HouseholdBook methods ==============================

    @Override
    public Path getHouseholdBookFilePath() {
        return householdBookStorage.getHouseholdBookFilePath();
    }

    @Override
    public Optional<ReadOnlyHouseholdBook> readHouseholdBook() throws DataLoadingException {
        return readHouseholdBook(householdBookStorage.getHouseholdBookFilePath());
    }

    @Override
    public Optional<ReadOnlyHouseholdBook> readHouseholdBook(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return householdBookStorage.readHouseholdBook(filePath);
    }

    @Override
    public void saveHouseholdBook(ReadOnlyHouseholdBook householdBook) throws IOException {
        saveHouseholdBook(householdBook, householdBookStorage.getHouseholdBookFilePath());
    }

    @Override
    public void saveHouseholdBook(ReadOnlyHouseholdBook householdBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        householdBookStorage.saveHouseholdBook(householdBook, filePath);
    }
}

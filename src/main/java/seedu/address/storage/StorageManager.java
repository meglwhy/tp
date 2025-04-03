package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyHouseholdBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.household.Household;
import seedu.address.model.session.Session;

/**
 * Manages storage of HouseholdBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private final HouseholdBookStorage householdBookStorage;
    private final UserPrefsStorage userPrefsStorage;

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
        Optional<ReadOnlyHouseholdBook> householdBookOptional = householdBookStorage.readHouseholdBook(filePath);
        if (householdBookOptional.isPresent()) {
            ReadOnlyHouseholdBook householdBook = householdBookOptional.get();
            if (containsInvalidData(householdBook)) {
                logger.warning("Data file at " + filePath + " contains invalid household or session data.");
                return Optional.empty();
            }
        }
        return householdBookOptional;
    }

    /**
     * Checks if the household book contains any invalid data that passed JSON parsing but has invalid fields
     * @param householdBook The household book to check
     * @return true if any invalid data is found, false otherwise
     */
    private boolean containsInvalidData(ReadOnlyHouseholdBook householdBook) {
        for (Household household : householdBook.getHouseholdList()) {
            if (!isValidHousehold(household)) {
                logger.warning("Invalid household found: " + household.getName().toString());
                return true;
            }
        }
        for (Session session : householdBook.getSessions()) {
            if (!isValidSession(session)) {
                logger.warning("Invalid session found for household: " + session.getHouseholdId().toString());
                return true;
            }
        }
        return false;
    }

    /**
     * Validates a household entity beyond what JSON parsing does
     */
    private boolean isValidHousehold(Household household) {
        try {
            if (!household.getId().toString().matches("H\\d{6}")) {
                return false;
            }
            if (!household.getName().toString().matches("[\\p{Alnum}\\s,.'()-]+")) {
                return false;
            }
            if (household.getAddress().toString().trim().isEmpty()) {
                return false;
            }
            if (!household.getContact().toString().matches("\\d+")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            // Any exception means invalid data
            logger.warning("Exception during household validation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates a session entity beyond what JSON parsing does
     */
    private boolean isValidSession(Session session) {
        try {
            UUID.fromString(session.getSessionId().toString());
            if (!session.getHouseholdId().toString().matches("H\\d{6}")) {
                return false;
            }
            LocalDate.parse(session.getDate().toString());
            String[] timeParts = session.getTime().toString().split(":");
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);
            return hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59;
        } catch (Exception e) {
            // Any exception means invalid data
            logger.warning("Exception during session validation: " + e.getMessage());
            return false;
        }
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

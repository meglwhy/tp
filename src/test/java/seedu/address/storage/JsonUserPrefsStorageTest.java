package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.UserPrefs;

public class JsonUserPrefsStorageTest {

    @TempDir
    public Path testFolder;

    @Test
    public void getUserPrefsFilePath_returnsCorrectPath() {
        Path filePath = testFolder.resolve("prefs.json");
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(filePath);
        assertEquals(filePath, storage.getUserPrefsFilePath());
    }

    @Test
    public void readUserPrefs_nonExistentFile_returnsEmptyOptional() throws DataLoadingException {
        Path filePath = testFolder.resolve("nonexistent.json");
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(filePath);
        Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
        assertTrue(prefsOptional.isEmpty());
    }

    @Test
    public void saveAndReadUserPrefs_validPrefs_success() throws DataLoadingException, IOException {
        Path filePath = testFolder.resolve("prefs.json");
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(filePath);
        UserPrefs originalPrefs = new UserPrefs();
        // Adjust a setting for testing.
        originalPrefs.setHouseholdBookFilePath(testFolder.resolve("householdbook.json"));
        // Save the prefs.
        storage.saveUserPrefs(originalPrefs);
        // Read the prefs.
        Optional<UserPrefs> readPrefsOptional = storage.readUserPrefs();
        assertTrue(readPrefsOptional.isPresent());
        UserPrefs readPrefs = readPrefsOptional.get();
        // We assume that UserPrefs implements equals appropriately.
        assertEquals(originalPrefs, readPrefs);
    }
}


package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.ReadOnlyHouseholdBook;
import seedu.address.testutil.TypicalHouseholds;

public class JsonHouseholdBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonHouseholdBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readHouseholdBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readHouseholdBook(null));
    }

    private Optional<ReadOnlyHouseholdBook> readHouseholdBook(String filePath) throws Exception {
        return new JsonHouseholdBookStorage(Paths.get(filePath)).readHouseholdBook();
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readHouseholdBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readHouseholdBook(
                addToTestDataPathIfNotNull("notJsonFormatHouseholdBook.json").toString()));
    }

    @Test
    public void readHouseholdBook_invalidHouseholdBook_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readHouseholdBook(
                addToTestDataPathIfNotNull("invalidHouseholdBook.json").toString()));
    }

    @Test
    public void readHouseholdBook_invalidContactHouseholdBook_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readHouseholdBook(
                addToTestDataPathIfNotNull("invalidContactHouseholdBook.json").toString()));
    }

    @Test
    public void readHouseholdBook_invalidIdFormatHouseholdBook_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readHouseholdBook(
                addToTestDataPathIfNotNull("invalidIdFormatHouseholdBook.json").toString()));
    }

    @Test
    public void readHouseholdBook_emptyAddressHouseholdBook_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readHouseholdBook(
                addToTestDataPathIfNotNull("emptyAddressHouseholdBook.json").toString()));
    }

    @Test
    public void readHouseholdBook_invalidSessionBook_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readHouseholdBook(
                addToTestDataPathIfNotNull("invalidSessionBook.json").toString()));
    }

    @Test
    public void readHouseholdBook_duplicateHouseholdsBook_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readHouseholdBook(
                addToTestDataPathIfNotNull("duplicateHouseholdsBook.json").toString()));
    }

    @Test
    public void readHouseholdBook_validHouseholdBook_success() throws Exception {
        Path validFilePath = addToTestDataPathIfNotNull("validHouseholdBook.json");
        Optional<ReadOnlyHouseholdBook> result = readHouseholdBook(validFilePath.toString());
        assertTrue(result.isPresent());
        // Additional validation could be done here to check the content
    }

    @Test
    public void readHouseholdBook_validHouseholdWithSessionsBook_success() throws Exception {
        Path validFilePath = addToTestDataPathIfNotNull("validHouseholdWithSessionsBook.json");
        Optional<ReadOnlyHouseholdBook> result = readHouseholdBook(validFilePath.toString());
        assertTrue(result.isPresent());
        // Additional validation could be done here to check the content
    }

    @Test
    public void readAndSaveHouseholdBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("tempHouseholdBook.json");
        HouseholdBook original = new HouseholdBook();
        JsonHouseholdBookStorage jsonHouseholdBookStorage = new JsonHouseholdBookStorage(filePath);

        // Save in new file and read back
        jsonHouseholdBookStorage.saveHouseholdBook(original);
        ReadOnlyHouseholdBook readBack = jsonHouseholdBookStorage.readHouseholdBook().get();
        assertEquals(original, readBack);

        // Modify data, overwrite existing file, and read back
        original.addHousehold(TypicalHouseholds.ALICE_HOUSEHOLD);
        jsonHouseholdBookStorage.saveHouseholdBook(original);
        readBack = jsonHouseholdBookStorage.readHouseholdBook().get();
        assertEquals(original, readBack);

        // Save and read without specifying file path
        original.addHousehold(TypicalHouseholds.BOB_HOUSEHOLD);
        jsonHouseholdBookStorage.saveHouseholdBook(original); // file path not specified
        readBack = jsonHouseholdBookStorage.readHouseholdBook().get(); // file path not specified
        assertEquals(original, readBack);
    }

    @Test
    public void saveHouseholdBook_nullHouseholdBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveHouseholdBook(null, "SomeFile.json"));
    }

    private void saveHouseholdBook(ReadOnlyHouseholdBook householdBook, String filePath) {
        try {
            new JsonHouseholdBookStorage(Paths.get(filePath))
                    .saveHouseholdBook(householdBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveHouseholdBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveHouseholdBook(new HouseholdBook(), null));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }
}

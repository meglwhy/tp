package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.HouseholdBook;
import seedu.address.model.ReadOnlyHouseholdBook;
import seedu.address.testutil.TypicalHouseholds;

public class JsonSerializableHouseholdBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableHouseholdBookTest");
    private static final Path TYPICAL_HOUSEHOLDS_FILE = TEST_DATA_FOLDER.resolve("typicalHouseholdsBook.json");
    private static final Path INVALID_HOUSEHOLD_FILE = TEST_DATA_FOLDER.resolve("invalidHouseholdBook.json");
    private static final Path DUPLICATE_HOUSEHOLD_FILE = TEST_DATA_FOLDER.resolve("duplicateHouseholdBook.json");
    private static final Path TYPICAL_HOUSEHOLDS_WITH_SESSIONS_FILE =
            TEST_DATA_FOLDER.resolve("typicalHouseholdsWithSessionsBook.json");
    private static final Path INVALID_SESSION_FILE = TEST_DATA_FOLDER.resolve("invalidSessionBook.json");

    @Test
    public void toModelType_typicalHouseholdsFile_success() throws Exception {
        JsonSerializableHouseholdBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_HOUSEHOLDS_FILE,
                JsonSerializableHouseholdBook.class).get();
        ReadOnlyHouseholdBook householdBookFromFile = dataFromFile.toModelType();
        HouseholdBook typicalHouseholdBook = TypicalHouseholds.getTypicalHouseholdBook();
        assertEquals(householdBookFromFile, typicalHouseholdBook);
    }

    @Test
    public void toModelType_invalidHouseholdFile_throwsIllegalValueException() throws Exception {
        JsonSerializableHouseholdBook dataFromFile = JsonUtil.readJsonFile(INVALID_HOUSEHOLD_FILE,
                JsonSerializableHouseholdBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateHouseholds_throwsIllegalValueException() throws Exception {
        JsonSerializableHouseholdBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_HOUSEHOLD_FILE,
                JsonSerializableHouseholdBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_typicalHouseholdsWithSessionsFile_success() throws Exception {
        JsonSerializableHouseholdBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_HOUSEHOLDS_WITH_SESSIONS_FILE,
                JsonSerializableHouseholdBook.class).get();
        ReadOnlyHouseholdBook householdBookFromFile = dataFromFile.toModelType();
        // We don't have a utility method for a typical household book with sessions yet,
        // so we just check that it loads without errors
        assertEquals(2, householdBookFromFile.getHouseholdList().size());
        assertEquals(2, householdBookFromFile.getSessionList().size());
    }

    @Test
    public void toModelType_invalidSessionFile_throwsIllegalValueException() throws Exception {
        JsonSerializableHouseholdBook dataFromFile = JsonUtil.readJsonFile(INVALID_SESSION_FILE,
                JsonSerializableHouseholdBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }
}

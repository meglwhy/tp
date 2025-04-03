package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyHouseholdBook;

/**
 * A class to access HouseholdBook data stored as a json file on the hard disk.
 */
public class JsonHouseholdBookStorage implements HouseholdBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonHouseholdBookStorage.class);

    private final Path filePath;

    public JsonHouseholdBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getHouseholdBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyHouseholdBook> readHouseholdBook() throws DataLoadingException {
        return readHouseholdBook(filePath);
    }

    /**
     * Similar to {@link #readHouseholdBook()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if the file is not in the correct format.
     */
    @Override
    public Optional<ReadOnlyHouseholdBook> readHouseholdBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableHouseholdBook> jsonHouseholdBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableHouseholdBook.class);
        if (jsonHouseholdBook.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonHouseholdBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveHouseholdBook(ReadOnlyHouseholdBook householdBook) throws IOException {
        saveHouseholdBook(householdBook, filePath);
    }

    /**
     * Similar to {@link #saveHouseholdBook(ReadOnlyHouseholdBook)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    @Override
    public void saveHouseholdBook(ReadOnlyHouseholdBook householdBook, Path filePath) throws IOException {
        requireNonNull(householdBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableHouseholdBook(householdBook), filePath);
    }
}

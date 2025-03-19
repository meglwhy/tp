package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.ReadOnlyHouseholdBook;

/**
 * Represents a storage for {@link HouseholdBook}.
 */
public interface HouseholdBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getHouseholdBookFilePath();

    /**
     * Returns HouseholdBook data as a {@link ReadOnlyHouseholdBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataLoadingException if the data file is not in the correct format.
     */
    Optional<ReadOnlyHouseholdBook> readHouseholdBook() throws DataLoadingException;

    /**
     * @see #getHouseholdBookFilePath()
     */
    Optional<ReadOnlyHouseholdBook> readHouseholdBook(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyHouseholdBook} to the storage.
     * @param householdBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveHouseholdBook(ReadOnlyHouseholdBook householdBook) throws IOException;

    /**
     * @see #saveHouseholdBook(ReadOnlyHouseholdBook)
     */
    void saveHouseholdBook(ReadOnlyHouseholdBook householdBook, Path filePath) throws IOException;

}

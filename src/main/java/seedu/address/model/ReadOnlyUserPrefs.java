package seedu.address.model;

import java.nio.file.Path;

import seedu.address.commons.core.GuiSettings;

/**
 * Unmodifiable view of user preferences.
 *
 * <p>This interface provides a read-only view of the application's user preferences,
 * ensuring that components that only need to access preferences cannot modify them.
 * It supports the principle of least privilege by restricting modification capabilities.</p>
 *
 * <p>User preferences include settings like GUI configuration (window size and position)
 * and file paths for persistent data storage. These preferences are typically loaded
 * at application startup and can be saved when the application closes.</p>
 *
 * <p>This interface is part of the application's defensive programming strategy,
 * helping to maintain data integrity by preventing unintended modifications to user preferences.</p>
 */
public interface ReadOnlyUserPrefs {

    /**
     * Returns the user's GUI settings.
     *
     * <p>GUI settings contain display preferences like window dimensions and position.
     * These settings allow the application to restore the user's preferred view
     * when restarting the application.</p>
     *
     * <p>The returned settings object is immutable and changes to it will not
     * affect the stored preferences.</p>
     *
     * @return The user's GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Returns the file path for the household book data storage.
     *
     * <p>This path indicates where the application should save and load the
     * household book data. It allows users to specify custom storage locations
     * for their data files.</p>
     *
     * <p>The application uses this path when performing file operations related
     * to storing or retrieving household data.</p>
     *
     * @return The file path where household data is stored.
     */
    Path getHouseholdBookFilePath();

}

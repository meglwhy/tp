package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import seedu.address.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 *
 * <p>This class stores user-specific application settings that persist between
 * application sessions. It maintains preferences like GUI settings (window size
 * and position) and the file path for data storage.</p>
 *
 * <p>UserPrefs implements the ReadOnlyUserPrefs interface to provide a read-only
 * view of preferences while also offering methods to modify these preferences.</p>
 *
 * <p>Instances of this class are typically serialized to/deserialized from JSON
 * for persistent storage of user preferences.</p>
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    /** Settings for the GUI components like window size and position. */
    private GuiSettings guiSettings = new GuiSettings();

    /** File path where the household book data is stored. */
    private Path householdBookFilePath = Paths.get("data" , "householdbook.json");

    /**
     * Creates a {@code UserPrefs} with default values.
     *
     * <p>Default values include:
     * <ul>
     *   <li>Default GUI settings (standard window size and centered position)</li>
     *   <li>Default data file path ("data/householdbook.json")</li>
     * </ul></p>
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the preferences in {@code userPrefs}.
     *
     * <p>This constructor creates a new instance while copying all preferences
     * from an existing UserPrefs object. It's useful for creating independent
     * copies of preference objects.</p>
     *
     * @param userPrefs The user preferences to copy from.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     *
     * <p>This method updates all preferences in this object to match those in
     * the provided newUserPrefs object. It's used to completely replace the
     * current preferences with a new set.</p>
     *
     * @param newUserPrefs The new user preferences to copy from.
     * @throws NullPointerException if {@code newUserPrefs} is null.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setHouseholdBookFilePath(newUserPrefs.getHouseholdBookFilePath());
    }

    /**
     * Returns the user's GUI settings.
     *
     * <p>GUI settings contain properties like window dimensions and position
     * that control how the application window is displayed.</p>
     *
     * @return The user's GUI settings.
     */
    @Override
    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    /**
     * Sets the user's GUI settings.
     *
     * <p>This method updates the stored GUI settings to the new values provided.
     * These settings affect how the application window is displayed when the
     * application is restarted.</p>
     *
     * @param guiSettings The new GUI settings.
     * @throws NullPointerException if {@code guiSettings} is null.
     */
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    /**
     * Returns the file path where household book data is stored.
     *
     * <p>This path is used by the application to save and load the household
     * data between application sessions.</p>
     *
     * @return The file path for household book data.
     */
    @Override
    public Path getHouseholdBookFilePath() {
        return householdBookFilePath;
    }

    /**
     * Sets the file path for household book data storage.
     *
     * <p>This method updates the location where household data will be saved.
     * The application will use this path for subsequent save operations.</p>
     *
     * @param householdBookFilePath The new file path for household book data.
     * @throws NullPointerException if {@code householdBookFilePath} is null.
     */
    public void setHouseholdBookFilePath(Path householdBookFilePath) {
        requireNonNull(householdBookFilePath);
        this.householdBookFilePath = householdBookFilePath;
    }

    /**
     * Returns true if both UserPrefs objects have the same values.
     *
     * <p>UserPrefs are considered equal if they have the same GUI settings
     * and household book file path.</p>
     *
     * @param other The object to compare with.
     * @return true if both objects represent the same preferences, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) {
            return false;
        }
        UserPrefs otherUserPrefs = (UserPrefs) other;
        return guiSettings.equals(otherUserPrefs.guiSettings)
                && householdBookFilePath.equals(otherUserPrefs.householdBookFilePath);
    }

    /**
     * Returns the hash code value for this UserPrefs.
     *
     * <p>The hash code is computed based on the GUI settings and
     * household book file path.</p>
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, householdBookFilePath);
    }

    /**
     * Returns a string representation of this UserPrefs.
     *
     * <p>The string contains the GUI settings and household book file path
     * in a human-readable format.</p>
     *
     * @return A string representation of the preferences.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + householdBookFilePath);
        return sb.toString();
    }
}

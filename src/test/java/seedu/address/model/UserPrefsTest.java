package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;

class UserPrefsTest {

    @Test
    public void testDefaultValues() {
        UserPrefs prefs = new UserPrefs();
        assertNotNull(prefs.getGuiSettings());
        // Default file path should be "data/householdbook.json"
        assertEquals(Paths.get("data", "householdbook.json"), prefs.getHouseholdBookFilePath());
    }

    @Test
    public void testSetHouseholdBookFilePath() {
        UserPrefs prefs = new UserPrefs();
        Path newPath = Paths.get("new", "path.json");
        prefs.setHouseholdBookFilePath(newPath);
        assertEquals(newPath, prefs.getHouseholdBookFilePath());
    }

    @Test
    public void testSetGuiSettings() {
        UserPrefs prefs = new UserPrefs();
        GuiSettings newSettings = new GuiSettings(1024, 768, 100, 100);
        prefs.setGuiSettings(newSettings);
        assertEquals(newSettings, prefs.getGuiSettings());
    }

    @Test
    public void testResetData() {
        UserPrefs prefs = new UserPrefs();
        GuiSettings originalSettings = prefs.getGuiSettings();
        Path originalPath = prefs.getHouseholdBookFilePath();

        UserPrefs newPrefs = new UserPrefs();
        newPrefs.setGuiSettings(new GuiSettings(800, 600, 0, 0));
        newPrefs.setHouseholdBookFilePath(Paths.get("changed", "path.json"));

        prefs.resetData(newPrefs);
        assertEquals(newPrefs.getGuiSettings(), prefs.getGuiSettings());
        assertEquals(newPrefs.getHouseholdBookFilePath(), prefs.getHouseholdBookFilePath());
    }

    @Test
    public void testEqualsAndHashCode() {
        UserPrefs prefs1 = new UserPrefs();
        UserPrefs prefs2 = new UserPrefs();
        assertEquals(prefs1, prefs2);
        assertEquals(prefs1.hashCode(), prefs2.hashCode());

        prefs2.setHouseholdBookFilePath(Paths.get("different", "path.json"));
        assertNotEquals(prefs1, prefs2);
    }

    @Test
    public void testToString() {
        UserPrefs prefs = new UserPrefs();
        String str = prefs.toString();
        assertTrue(str.contains("Gui Settings :"));
        assertTrue(str.contains("Local data file location :"));
    }
}


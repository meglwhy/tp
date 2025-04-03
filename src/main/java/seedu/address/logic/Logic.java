package seedu.address.logic;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.household.Household;
import seedu.address.model.session.Session;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** Returns the HouseholdBook */
    HouseholdBook getHouseholdBook();

    /** Returns an unmodifiable view of the filtered list of households */
    ObservableList<Household> getFilteredHouseholdList();

    /** Returns an unmodifiable view of the filtered list of sessions */
    ObservableList<Session> getFilteredSessionList();

    /** Updates the filter of the filtered session list to filter by the given predicate */
    void updateFilteredSessionList(Predicate<Session> predicate);

    /** Updates the filter of the filtered household list to filter by the given predicate */
    void updateFilteredHouseholdList(Predicate<Household> predicate);

    /** Returns the user prefs household book file path */
    Path getHouseholdBookFilePath();

    /** Returns the user prefs GUI settings */
    GuiSettings getGuiSettings();

    /** Set the user prefs GUI settings */
    void setGuiSettings(GuiSettings guiSettings);
}

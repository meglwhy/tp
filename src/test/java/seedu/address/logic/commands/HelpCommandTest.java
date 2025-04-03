package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;

class HelpCommandTest {

    private static class ModelStub implements Model {
        @Override public seedu.address.model.HouseholdBook getHouseholdBook() {
            return null;
        }
        @Override public seedu.address.commons.core.GuiSettings getGuiSettings() {
            return null;
        }
        @Override public void setGuiSettings(seedu.address.commons.core.GuiSettings guiSettings) {

        }
        @Override public seedu.address.model.ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }
        @Override public java.nio.file.Path getHouseholdBookFilePath() {
            return null;
        }
        @Override public void setHouseholdBookFilePath(java.nio.file.Path householdBookFilePath) {

        }
        @Override public javafx.collections
                .ObservableList<seedu.address.model.household.Household> getFilteredHouseholdList() {
            return null;
        }
        @Override public javafx.collections
                .ObservableList<seedu.address.model.session.Session> getFilteredSessionList() {
            return null;
        }
        @Override public void updateFilteredHouseholdList(java.util.function.Predicate<seedu
                .address.model.household.Household> predicate) {

        }
        @Override public void updateFilteredSessionList(java.util.function.Predicate<seedu
                .address.model.session.Session> predicate) {

        }
    }

    @Test
    public void execute_helpCommand_returnsHelpMessage() throws Exception {
        HelpCommand command = new HelpCommand();
        CommandResult result = command.execute(new ModelStub());
        assertEquals(HelpCommand.SHOWING_HELP_MESSAGE, result.getFeedbackToUser());
        assertTrue(result.isShowHelp());
        assertFalse(result.isExit());
    }
}


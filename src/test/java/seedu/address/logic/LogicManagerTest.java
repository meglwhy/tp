package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.parser.HouseholdBookParser;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.session.Session;
import seedu.address.storage.Storage;

class LogicManagerTest {
    private Model model;
    private Storage storage;
    private LogicManager logicManager;
    private HouseholdBookParser dummyParser;
    // Dummy command always returns a preset CommandResult.
    private final Command dummyCommand = new Command() {
        @Override
        public CommandResult execute(Model model) {
            return new CommandResult("Dummy command result");
        }
    };

    @BeforeEach
    void setUp() throws Exception {
        model = Mockito.mock(Model.class);
        storage = Mockito.mock(Storage.class);
        // Instead of stubbing a storage save method (which doesn't exist), we simply proceed.
        // When model.getHouseholdBook() is called, return a new HouseholdBook.
        HouseholdBook dummyHb = new HouseholdBook();
        when(model.getHouseholdBook()).thenReturn(dummyHb);
        // Setup dummy filtered lists and file path.
        ObservableList<Household> dummyHouseholds = FXCollections.observableArrayList();
        when(model.getFilteredHouseholdList()).thenReturn(dummyHouseholds);
        ObservableList<Session> dummySessions = FXCollections.observableArrayList();
        when(model.getFilteredSessionList()).thenReturn(dummySessions);
        Path dummyPath = Path.of("dummy/path");
        when(model.getHouseholdBookFilePath()).thenReturn(dummyPath);

        logicManager = new LogicManager(model, storage);
        // Override the private householdBookParser to always return our dummyCommand.
        dummyParser = Mockito.mock(HouseholdBookParser.class);
        when(dummyParser.parseCommand(any(String.class))).thenReturn(dummyCommand);
        Field parserField = LogicManager.class.getDeclaredField("householdBookParser");
        parserField.setAccessible(true);
        parserField.set(logicManager, dummyParser);
    }

    @Test
    void execute_validCommand_success() throws Exception {
        CommandResult result = logicManager.execute("dummy command text");
        // The result should be the dummy command result.
        assertEquals("Dummy command result", result.getFeedbackToUser());
        // Since your Storage interface does not have a save method, we simply do not verify any call here.
    }

    // We omit tests for storage exceptions since there is no save method to simulate exceptions.

    @Test
    void getHouseholdBook_returnsModelHouseholdBook() {
        HouseholdBook dummyHb = new HouseholdBook();
        when(model.getHouseholdBook()).thenReturn(dummyHb);
        assertSame(dummyHb, logicManager.getHouseholdBook());
    }

    @Test
    void getFilteredHouseholdList_returnsModelFilteredHouseholdList() {
        ObservableList<Household> dummyHouseholds = FXCollections.observableArrayList();
        when(model.getFilteredHouseholdList()).thenReturn(dummyHouseholds);
        assertSame(dummyHouseholds, logicManager.getFilteredHouseholdList());
    }

    @Test
    void getFilteredSessionList_returnsModelFilteredSessionList() {
        ObservableList<Session> dummySessions = FXCollections.observableArrayList();
        when(model.getFilteredSessionList()).thenReturn(dummySessions);
        assertSame(dummySessions, logicManager.getFilteredSessionList());
    }

    @Test
    void getHouseholdBookFilePath_returnsModelFilePath() {
        Path dummyPath = Path.of("dummy/path");
        when(model.getHouseholdBookFilePath()).thenReturn(dummyPath);
        assertSame(dummyPath, logicManager.getHouseholdBookFilePath());
    }

    @Test
    void updateFilteredSessionList_callsModelUpdateFilteredSessionList() {
        Predicate<Session> predicate = session -> true;
        logicManager.updateFilteredSessionList(predicate);
        Mockito.verify(model).updateFilteredSessionList(predicate);
    }

    @Test
    void updateFilteredHouseholdList_callsModelUpdateFilteredHouseholdList() {
        Predicate<Household> predicate = household -> true;
        logicManager.updateFilteredHouseholdList(predicate);
        Mockito.verify(model).updateFilteredHouseholdList(predicate);
    }
}




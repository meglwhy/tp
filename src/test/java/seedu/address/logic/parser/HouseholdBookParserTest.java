package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddHouseholdCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteHouseholdCommand;
import seedu.address.logic.commands.EditHouseholdCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindHouseholdCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListHouseholdsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.Model;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;

public class HouseholdBookParserTest {
    private HouseholdBookParser parser;
    private Model mockModel;
    private HouseholdBook mockHouseholdBook;

    @BeforeEach
    public void setUp() {
        mockModel = mock(Model.class);
        mockHouseholdBook = mock(HouseholdBook.class);
        parser = new HouseholdBookParser(mockModel);

        // Set up return value for model.getHouseholdBook()
        when(mockModel.getHouseholdBook()).thenReturn(mockHouseholdBook);

        // Set up return value for householdBook.getHouseholdById(...)
        Household mockHousehold = mock(Household.class);
        when(mockHouseholdBook.getHouseholdById(any(HouseholdId.class))).thenReturn(Optional.of(mockHousehold));
    }

    @Test
    public void parseCommand_clearCommand_returnsClearCommand() throws Exception {
        Command command = parser.parseCommand("clear");
        assertTrue(command instanceof ClearCommand);
    }

    @Test
    public void parseCommand_exitCommand_returnsExitCommand() throws Exception {
        Command command = parser.parseCommand("exit");
        assertTrue(command instanceof ExitCommand);
    }

    @Test
    public void parseCommand_helpCommand_returnsHelpCommand() throws Exception {
        Command command = parser.parseCommand("help");
        assertTrue(command instanceof HelpCommand);
    }

    @Test
    public void parseCommand_addHouseholdCommand_returnsAddHouseholdCommand() throws Exception {
        Command command = parser.parseCommand("add n/sample name a/sample rd p/91234567");
        assertTrue(command instanceof AddHouseholdCommand);
    }

    @Test
    public void parseCommand_editHouseholdCommand_returnsEditHouseholdCommand() throws Exception {
        Command command = parser.parseCommand("edit id/H000001 n/change name");
        assertTrue(command instanceof EditHouseholdCommand);
    }

    @Test
    public void parseCommand_deleteHouseholdCommand_returnsDeleteHouseholdCommand() throws Exception {
        Command command = parser.parseCommand("delete id/H000001");
        assertTrue(command instanceof DeleteHouseholdCommand);
    }

    @Test
    public void parseCommand_findHouseholdCommand_returnsFindHouseholdCommand() throws Exception {
        Command command = parser.parseCommand("find keyword");
        assertTrue(command instanceof FindHouseholdCommand);
    }

    @Test
    public void parseCommand_listHouseholdsCommand_returnsListHouseholdsCommand() throws Exception {
        Command command = parser.parseCommand("list");
        assertTrue(command instanceof ListHouseholdsCommand);
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () -> parser.parseCommand("unknownCommand"));
        assertEquals(MESSAGE_UNKNOWN_COMMAND, exception.getMessage());
    }

    @Test
    public void parseCommand_invalidFormat_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () -> parser.parseCommand(""));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), exception.getMessage());
    }
}


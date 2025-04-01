package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

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

public class HouseholdBookParserTest {

    private final HouseholdBookParser parser = new HouseholdBookParser();

    @Test
    public void parseCommand_clearCommand_returnsClearCommand() throws Exception {
        // "clear" should return a ClearCommand instance.
        Command command = parser.parseCommand("clear");
        assertTrue(command instanceof ClearCommand);
    }

    @Test
    public void parseCommand_exitCommand_returnsExitCommand() throws Exception {
        // "exit" should return an ExitCommand instance.
        Command command = parser.parseCommand("exit");
        assertTrue(command instanceof ExitCommand);
    }

    @Test
    public void parseCommand_helpCommand_returnsHelpCommand() throws Exception {
        // "help" should return a HelpCommand instance.
        Command command = parser.parseCommand("help");
        assertTrue(command instanceof HelpCommand);
    }

    @Test
    public void parseCommand_addHouseholdCommand_returnsAddHouseholdCommand() throws Exception {
        // "list" should return a ListHouseholdsCommand instance.
        Command command = parser.parseCommand("add n/sample name a/sample rd p/91234567");
        assertTrue(command instanceof AddHouseholdCommand);
    }

    @Test
    public void parseCommand_editHouseholdCommand_returnsEditHouseholdCommand() throws Exception {
        // "list" should return a ListHouseholdsCommand instance.
        Command command = parser.parseCommand("edit id/H000001 n/change name");
        assertTrue(command instanceof EditHouseholdCommand);
    }

    @Test
    public void parseCommand_deleteHouseholdCommand_returnsDeleteHouseholdCommand() throws Exception {
        // "list" should return a ListHouseholdsCommand instance.
        Command command = parser.parseCommand("delete id/H000001");
        assertTrue(command instanceof DeleteHouseholdCommand);
    }

    @Test
    public void parseCommand_findHouseholdCommand_returnsFindHouseholdCommand() throws Exception {
        // "list" should return a ListHouseholdsCommand instance.
        Command command = parser.parseCommand("find keyword");
        assertTrue(command instanceof FindHouseholdCommand);
    }

    @Test
    public void parseCommand_listHouseholdsCommand_returnsListHouseholdsCommand() throws Exception {
        // "list" should return a ListHouseholdsCommand instance.
        Command command = parser.parseCommand("list");
        assertTrue(command instanceof ListHouseholdsCommand);
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        // An unknown command should throw a ParseException with the appropriate message.
        ParseException exception = assertThrows(ParseException.class, () -> parser.parseCommand("unknownCommand"));
        assertEquals(MESSAGE_UNKNOWN_COMMAND, exception.getMessage());
    }

    @Test
    public void parseCommand_invalidFormat_throwsParseException() {
        // An input that does not match the basic command format (e.g. empty string) should throw a ParseException.
        ParseException exception = assertThrows(ParseException.class, () -> parser.parseCommand(""));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), exception.getMessage());
    }
}


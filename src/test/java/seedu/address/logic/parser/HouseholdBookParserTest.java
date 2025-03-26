package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddHouseholdCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteHouseholdCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListHouseholdsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

class HouseholdBookParserTest {

    private HouseholdBookParser parser;

    @BeforeEach
    void setUp() {
        parser = new HouseholdBookParser();
    }

    @Test
    void parseCommand_addHousehold() throws Exception {
        Command command = parser.parseCommand("add-household n/JohnDoe a/John St p/91234567");
        assertEquals(AddHouseholdCommand.class, command.getClass());
    }

    @Test
    void parseCommand_deleteHousehold() throws Exception {
        Command command = parser.parseCommand("delete-household id/H000001");
        assertEquals(DeleteHouseholdCommand.class, command.getClass());
    }

    @Test
    void parseCommand_listHouseholds() throws Exception {
        Command command = parser.parseCommand("list-households");
        assertEquals(ListHouseholdsCommand.class, command.getClass());
    }

    @Test
    void parseCommand_exit() throws Exception {
        Command command = parser.parseCommand("exit");
        assertEquals(ExitCommand.class, command.getClass());
    }

    @Test
    void parseCommand_help() throws Exception {
        Command command = parser.parseCommand("help");
        assertEquals(HelpCommand.class, command.getClass());
    }

    @Test
    void parseCommand_unknown_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseCommand("unknownCommand"), MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    void parseCommand_invalidFormat_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseCommand(""),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }
}

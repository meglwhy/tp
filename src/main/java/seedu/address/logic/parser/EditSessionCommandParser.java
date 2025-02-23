package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditSessionCommand;
import seedu.address.logic.commands.EditSessionCommand.EditSessionDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditSessionCommand object
 */
public class EditSessionCommandParser implements Parser<EditSessionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditSessionCommand
     * and returns an EditSessionCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditSessionCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DATE, PREFIX_TIME, PREFIX_NOTE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, 
                    EditSessionCommand.MESSAGE_USAGE), pe);
        }

        EditSessionDescriptor editSessionDescriptor = new EditSessionDescriptor();
        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            editSessionDescriptor.setDate(ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get()));
        }
        if (argMultimap.getValue(PREFIX_TIME).isPresent()) {
            editSessionDescriptor.setTime(ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME).get()));
        }
        if (argMultimap.getValue(PREFIX_NOTE).isPresent()) {
            editSessionDescriptor.setNote(ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).get()));
        }

        if (!editSessionDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditSessionCommand.MESSAGE_NOT_EDITED);
        }

        return new EditSessionCommand(index, editSessionDescriptor);
    }
} 
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.logic.commands.AddHouseholdCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.*;

/**
 * Parses input arguments and creates a new AddHouseholdCommand object
 */
public class AddHouseholdCommandParser implements Parser<AddHouseholdCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddHouseholdCommand
     * and returns an AddHouseholdCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddHouseholdCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE);

        if (!argMultimap.arePrefixesPresent(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddHouseholdCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Contact contact = ParserUtil.parseContact(argMultimap.getValue(PREFIX_PHONE).get());

        Household household = new Household(name, address, contact);

        return new AddHouseholdCommand(household);
    }
}

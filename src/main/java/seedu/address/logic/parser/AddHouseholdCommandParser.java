package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.logic.commands.AddHouseholdCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.Name;


/**
 * Parses input arguments and creates a new AddHouseholdCommand object
 */
public class AddHouseholdCommandParser implements Parser<AddHouseholdCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddHouseholdCommand
     * and returns an {@code AddHouseholdCommand} object for execution.
     *
     * @param args The string of arguments to parse. Must contain a name, address, and contact.
     * @return An {@code AddHouseholdCommand} object initialized with the parsed household data.
     * @throws ParseException If the user input does not conform to the expected format or is missing required prefixes.
     */
    public AddHouseholdCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE);

        if (!argMultimap.arePrefixesPresent(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddHouseholdCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(
                argMultimap.getValue(PREFIX_NAME).orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHouseholdCommand.MESSAGE_USAGE))));
        Address address = ParserUtil.parseAddress(
                argMultimap.getValue(PREFIX_ADDRESS).orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHouseholdCommand.MESSAGE_USAGE))));
        Contact contact = ParserUtil.parseContact(
                argMultimap.getValue(PREFIX_PHONE).orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHouseholdCommand.MESSAGE_USAGE))));

        Household household = new Household(name, address, contact);

        return new AddHouseholdCommand(household);
    }
}

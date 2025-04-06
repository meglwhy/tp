package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.EditHouseholdCommand;
import seedu.address.logic.commands.EditHouseholdCommand.EditHouseholdDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditHouseholdCommand object
 */
public class EditHouseholdCommandParser implements Parser<EditHouseholdCommand> {
    private final Model model;

    public EditHouseholdCommandParser(Model model) {
        this.model = model;
    }
    /**
     * Parses the given {@code String} of arguments in the context of the {@code EditHouseholdCommand}
     * and returns an {@code EditHouseholdCommand} object for execution.
     *
     * @param args The string representing the user input to be parsed.
     * @return An {@code EditHouseholdCommand} object containing the parsed household ID and descriptor for editing.
     * @throws ParseException If the user input does not conform to the expected format.
     */
    public EditHouseholdCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_TAG);

        if (argMultimap.getValue(PREFIX_ID).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditHouseholdCommand.MESSAGE_USAGE));
        }

        HouseholdId householdId = ParserUtil.parseHouseholdId(argMultimap.getValue(PREFIX_ID).get());
        if (model.getHouseholdBook().getHouseholdById(householdId).isEmpty()) {
            throw new ParseException(String.format(EditHouseholdCommand.MESSAGE_HOUSEHOLD_NOT_FOUND, householdId));
        }
        EditHouseholdDescriptor editHouseholdDescriptor = new EditHouseholdDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            String name = argMultimap.getValue(PREFIX_NAME).get().trim();
            if (name.isEmpty()) {
                throw new ParseException("Household name cannot be empty.");
            }
            editHouseholdDescriptor.setName(ParserUtil.parseName(name));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            String phone = argMultimap.getValue(PREFIX_PHONE).get().trim();
            if (phone.isEmpty()) {
                throw new ParseException("Contact number cannot be empty.");
            }
            editHouseholdDescriptor.setContact(ParserUtil.parseContact(phone));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            String address = argMultimap.getValue(PREFIX_ADDRESS).get().trim();
            if (address.isEmpty()) {
                throw new ParseException("Address cannot be empty.");
            }
            editHouseholdDescriptor.setAddress(ParserUtil.parseAddress(address));
        }
        if (argMultimap.getAllValues(PREFIX_TAG).stream().anyMatch(String::isEmpty)) {
            throw new ParseException("Tags cannot be empty.");
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editHouseholdDescriptor::setTags);
        if (!editHouseholdDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditHouseholdCommand.MESSAGE_NOT_EDITED);
        }

        return new EditHouseholdCommand(householdId, editHouseholdDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     *
     * @param tags The collection of tags to be parsed.
     * @return An {@code Optional<Set<Tag>>} containing the parsed tags, or an empty optional if no tags were provided.
     * @throws ParseException If the tags cannot be parsed into a valid set of tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.iterator().next().isEmpty()
                ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }
}

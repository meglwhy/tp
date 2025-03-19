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
import seedu.address.model.household.HouseholdId;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditHouseholdCommand object
 */
public class EditHouseholdCommandParser implements Parser<EditHouseholdCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditHouseholdCommand
     * and returns an EditHouseholdCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditHouseholdCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_TAG);

        if (!argMultimap.getValue(PREFIX_ID).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditHouseholdCommand.MESSAGE_USAGE));
        }

        HouseholdId householdId = ParserUtil.parseHouseholdId(argMultimap.getValue(PREFIX_ID).get());
        EditHouseholdDescriptor editHouseholdDescriptor = new EditHouseholdDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editHouseholdDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editHouseholdDescriptor.setContact(ParserUtil.parseContact(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editHouseholdDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
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

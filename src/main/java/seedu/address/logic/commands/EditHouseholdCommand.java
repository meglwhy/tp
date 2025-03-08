package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.household.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.Model;

/**
 * Edits the details of an existing household in the household book.
 */
public class EditHouseholdCommand extends Command {

    public static final String COMMAND_WORD = "edit-household";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the household identified "
            + "by the household ID. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + PREFIX_ID + "HOUSEHOLD_ID "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_PHONE + "CONTACT] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_TAG + "priority "
            + PREFIX_TAG + "followup";

    public static final String MESSAGE_EDIT_HOUSEHOLD_SUCCESS = "Edited Household: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID: %1$s";

    private final HouseholdId householdId;
    private final EditHouseholdDescriptor editHouseholdDescriptor;

    /**
     * @param householdId ID of the household in the filtered household list to edit
     * @param editHouseholdDescriptor details to edit the household with
     */
    public EditHouseholdCommand(HouseholdId householdId, EditHouseholdDescriptor editHouseholdDescriptor) {
        requireNonNull(householdId);
        requireNonNull(editHouseholdDescriptor);

        this.householdId = householdId;
        this.editHouseholdDescriptor = new EditHouseholdDescriptor(editHouseholdDescriptor);
    }

    @Override
    public CommandResult execute(Model householdBook) throws CommandException {
        requireNonNull(householdBook);

        Household householdToEdit = householdBook.getHouseholdBook().getHouseholdById(householdId)
                .orElseThrow(() -> new CommandException(
                        String.format(MESSAGE_HOUSEHOLD_NOT_FOUND, householdId)));

        Household editedHousehold = createEditedHousehold(householdToEdit, editHouseholdDescriptor);

        householdBook.getHouseholdBook().updateHousehold(householdToEdit, editedHousehold);
        return new CommandResult(String.format(MESSAGE_EDIT_HOUSEHOLD_SUCCESS, editedHousehold));
    }

    /**
     * Creates and returns a {@code Household} with the details of {@code householdToEdit}
     * edited with {@code editHouseholdDescriptor}.
     */
    private static Household createEditedHousehold(Household householdToEdit,
                                                 EditHouseholdDescriptor editHouseholdDescriptor) {
        assert householdToEdit != null;

        Name updatedName = editHouseholdDescriptor.getName().orElse(householdToEdit.getName());
        Address updatedAddress = editHouseholdDescriptor.getAddress().orElse(householdToEdit.getAddress());
        Contact updatedContact = editHouseholdDescriptor.getContact().orElse(householdToEdit.getContact());
        Set<Tag> updatedTags = editHouseholdDescriptor.getTags().orElse(householdToEdit.getTags());

        return new Household(updatedName, updatedAddress, updatedContact, householdToEdit.getId(), updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EditHouseholdCommand // instanceof handles nulls
                && householdId.equals(((EditHouseholdCommand) other).householdId)
                && editHouseholdDescriptor.equals(((EditHouseholdCommand) other).editHouseholdDescriptor));
    }

    /**
     * Stores the details to edit the household with. Each non-empty field value will replace the
     * corresponding field value of the household.
     */
    public static class EditHouseholdDescriptor {
        private Name name;
        private Address address;
        private Contact contact;
        private Set<Tag> tags;

        public EditHouseholdDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditHouseholdDescriptor(EditHouseholdDescriptor toCopy) {
            setName(toCopy.name);
            setAddress(toCopy.address);
            setContact(toCopy.contact);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return name != null || address != null || contact != null || tags != null;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Contact> getContact() {
            return Optional.ofNullable(contact);
        }

        public void setContact(Contact contact) {
            this.contact = contact;
        }

        public Optional<Set<Tag>> getTags() {
            return Optional.ofNullable(tags);
        }

        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditHouseholdDescriptor)) {
                return false;
            }

            EditHouseholdDescriptor e = (EditHouseholdDescriptor) other;

            return getName().equals(e.getName())
                    && getAddress().equals(e.getAddress())
                    && getContact().equals(e.getContact())
                    && getTags().equals(e.getTags());
        }
    }
} 
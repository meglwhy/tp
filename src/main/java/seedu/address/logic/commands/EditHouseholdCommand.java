package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Address;
import seedu.address.model.household.Contact;
import seedu.address.model.household.Household;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.household.Name;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing household in the household book.
 */
public class EditHouseholdCommand extends Command {
    public static final String COMMAND_WORD = "edit";

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
            + PREFIX_ID + "H000001 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_TAG + "priority "
            + PREFIX_TAG + "followup";

    public static final String MESSAGE_EDIT_HOUSEHOLD_SUCCESS = "Edited Household: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with ID: %1$s";
    public static final String MESSAGE_DUPLICATE_HOUSEHOLD = "This household would be a duplicate"
            + " of an existing household.";

    private final HouseholdId householdId;
    private final EditHouseholdDescriptor editHouseholdDescriptor;

    /**
     * Creates an {@code EditHouseholdCommand} to edit the specified {@code Household}.
     *
     * @param householdId ID of the household in the filtered household list to edit. Must not be null.
     * @param editHouseholdDescriptor Details to edit the household with. Must not be null.
     */
    public EditHouseholdCommand(HouseholdId householdId, EditHouseholdDescriptor editHouseholdDescriptor) {
        requireNonNull(householdId);
        requireNonNull(editHouseholdDescriptor);

        this.householdId = householdId;
        this.editHouseholdDescriptor = new EditHouseholdDescriptor(editHouseholdDescriptor);
    }
    /**
     * Executes the command to edit the specified household in the model.
     *
     * @param householdBook The model containing the household to be edited. Must not be null.
     * @return A {@code CommandResult} indicating the result of the edit operation.
     * @throws CommandException If the household ID does not exist or the edited household would be a duplicate.
     */
    @Override
    public CommandResult execute(Model householdBook) throws CommandException {
        requireNonNull(householdBook);

        Household householdToEdit = householdBook.getHouseholdBook().getHouseholdById(householdId).get();

        Household editedHousehold = createEditedHousehold(householdToEdit, editHouseholdDescriptor);

        // Check if edited household would result in a duplicate
        if (householdBook.getHouseholdBook().hasHousehold(editedHousehold)) {
            throw new CommandException(MESSAGE_DUPLICATE_HOUSEHOLD);
        }

        householdBook.getHouseholdBook().updateHousehold(householdToEdit, editedHousehold);
        return new CommandResult(String.format(MESSAGE_EDIT_HOUSEHOLD_SUCCESS, editedHousehold));
    }

    /**
     * Creates a new {@code Household} instance with updated details based on the provided
     * {@code EditHouseholdDescriptor}. If a field in the descriptor is not specified,
     * the corresponding value from the original household is retained.
     *
     * This method also copies all existing sessions from the original household to the
     * newly created household.
     *
     *
     * @param householdToEdit the original {@code Household} to be edited; must not be {@code null}
     * @param descriptor the {@code EditHouseholdDescriptor} containing the updated details; may provide optional
     *                   new values for name, contact, address, and tags
     * @return a new {@code Household} instance with updated fields and copied sessions
     */
    public Household createEditedHousehold(Household householdToEdit, EditHouseholdDescriptor descriptor) {
        assert householdToEdit != null;
        Name updatedName = descriptor.getName().orElse(householdToEdit.getName());
        Contact updatedContact = descriptor.getContact().orElse(householdToEdit.getContact());
        Address updatedAddress = descriptor.getAddress().orElse(householdToEdit.getAddress());
        Set<Tag> updatedTags = descriptor.getTags().orElse(householdToEdit.getTags());

        // Create a new Household instance with the updated details.
        Household editedHousehold = new Household(updatedName, updatedAddress, updatedContact,
                householdToEdit.getId(), updatedTags);

        // Copy the existing sessions from the original household.
        editedHousehold.getSessions().addAll(householdToEdit.getSessions());

        return editedHousehold;
    }

    /**
     * Checks if this {@code EditHouseholdCommand} is equal to another object.
     *
     * @param other The other object to compare against.
     * @return {@code true} if the other object is the same instance or an equivalent
     *         {@code EditHouseholdCommand} with the same household ID and edit descriptor,
     *         {@code false} otherwise.
     */
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
         * Creates a copy of the given {@code EditHouseholdDescriptor}.
         *
         * @param toCopy The descriptor to copy from. Must not be null.
         */
        public EditHouseholdDescriptor(EditHouseholdDescriptor toCopy) {
            setName(toCopy.name);
            setAddress(toCopy.address);
            setContact(toCopy.contact);
            setTags(toCopy.tags);
        }

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

        /**
         * Checks if this {@code EditHouseholdDescriptor} is equal to another object.
         *
         * @param other The other object to compare against.
         * @return {@code true} if the other object is the same instance or an equivalent
         *         {@code EditHouseholdDescriptor} with identical values for all fields,
         *         {@code false} otherwise.
         */
        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditHouseholdDescriptor e)) {
                return false;
            }

            return getName().equals(e.getName())
                    && getAddress().equals(e.getAddress())
                    && getContact().equals(e.getContact())
                    && getTags().equals(e.getTags());
        }
    }
}

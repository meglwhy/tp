package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.Household;

/**
 * Adds a household to the household book.
 */
@SuppressWarnings("checkstyle:Regexp")
public class AddHouseholdCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a household to the household book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_PHONE + "CONTACT\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Smith Family "
            + PREFIX_ADDRESS + "123 Main St "
            + PREFIX_PHONE + "98765432";

    public static final String MESSAGE_SUCCESS = "New household added: %1$s";
    public static final String MESSAGE_DUPLICATE_HOUSEHOLD = "This is a duplicate name, address or contact number.";
    private final Household toAdd;

    /**
     * Creates an {@code AddHouseholdCommand} to add the specified {@code Household}.
     *
     * @param household The household to be added.
     * @throws NullPointerException If {@code household} is null.
     */
    public AddHouseholdCommand(Household household) {
        requireNonNull(household);
        toAdd = household;
    }
    /**
     * Returns the {@code Household} to be added by this command.
     *
     * @return The household to be added.
     */
    public Household getHousehold() {
        return toAdd;
    }
    /**
     * Executes the command to add the specified {@code Household} to the model.
     *
     * @param model The model to which the household will be added.
     * @return A {@code CommandResult} indicating the result of the operation.
     * @throws CommandException If the household already exists in the model.
     * @throws NullPointerException If {@code model} is null.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.getHouseholdBook().hasHousehold(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_HOUSEHOLD);
        }

        model.getHouseholdBook().addHousehold(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
    /**
     * Checks if this {@code AddHouseholdCommand} is equal to another object.
     *
     * @param other The other object to compare against.
     * @return True if the other object is an {@code AddHouseholdCommand} with the same household to add,
     *     false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddHouseholdCommand // instanceof handles nulls
                && toAdd.equals(((AddHouseholdCommand) other).toAdd));
    }
}

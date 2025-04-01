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
     * Creates an AddHouseholdCommand to add the specified {@code Household}
     */
    public AddHouseholdCommand(Household household) {
        requireNonNull(household);
        toAdd = household;
    }
    /**
     * Getter for the household to be added.
     */
    public Household getHousehold() {
        return toAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.getHouseholdBook().hasHousehold(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_HOUSEHOLD);
        }

        model.getHouseholdBook().addHousehold(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddHouseholdCommand // instanceof handles nulls
                && toAdd.equals(((AddHouseholdCommand) other).toAdd));
    }
}

package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.time.LocalDate;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;

/**
 * Adds a session to a household in the household book.
 */
public class AddSessionCommand extends Command {

    public static final String COMMAND_WORD = "add-s";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a session to a household. "
            + "Parameters: "
            + PREFIX_ID + "HOUSEHOLD_ID "
            + PREFIX_DATE + "DATE (in YYYY-MM-DD) "
            + PREFIX_TIME + "TIME (in HH:mm)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "H000001 "
            + PREFIX_DATE + "2025-05-15 "
            + PREFIX_TIME + "14:30";

    public static final String MESSAGE_SUCCESS = "New session added to household %1$s: %2$s";
    public static final String MESSAGE_DUPLICATE_SESSION =
            "This time slot is already booked.\n"
            + "Existing session: %1$s";
    public static final String MESSAGE_PAST_DATE = "Session date cannot be in the past";
    public static final String MESSAGE_HOUSEHOLD_NOT_FOUND = "No household found with this ID";

    private final HouseholdId householdId;
    private final Session toAdd;

    /**
     * Creates an {@code AddSessionCommand} to add the specified {@code Session} to the specified {@code Household}.
     *
     * @param householdId The ID of the household to which the session will be added. Must not be null.
     * @param date The date of the session to be added. Must not be null.
     * @param time The time of the session to be added. Must not be null.
     */
    public AddSessionCommand(HouseholdId householdId, SessionDate date, SessionTime time) {
        requireNonNull(householdId);
        requireNonNull(date);
        requireNonNull(time);

        this.householdId = householdId;
        this.toAdd = new Session(householdId, date, time);
    }
    /**
     * Executes the add session command, adding the specified session to the household.
     *
     * @param model The model in which the session is to be added. Must not be null.
     * @return A {@code CommandResult} indicating the result of the add operation.
     * @throws CommandException If the household does not exist, the session date is in the past,
     *                          or a conflicting session already exists.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.getHouseholdBook().hasHouseholdId(householdId)) {
            throw new CommandException(MESSAGE_HOUSEHOLD_NOT_FOUND);
        }

        if (isPastDate(toAdd.getDate())) {
            throw new CommandException(MESSAGE_PAST_DATE);
        }

        Optional<Session> conflict = model.getHouseholdBook().getConflictingSession(toAdd);
        if (conflict.isPresent()) {
            throw new CommandException(
                    String.format(MESSAGE_DUPLICATE_SESSION, conflict.get()));
        }

        model.getHouseholdBook().addSessionToHousehold(householdId, toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, householdId, toAdd));
    }

    private boolean isPastDate(SessionDate sessionDate) {
        LocalDate today = LocalDate.now();
        return sessionDate.value.isBefore(today);
    }
    /**
     * Checks if this command is equal to another object.
     *
     * @param other The other object to compare with.
     * @return {@code true} if the other object is the same instance or an equivalent {@code AddSessionCommand};
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddSessionCommand // instanceof handles nulls
                && householdId.equals(((AddSessionCommand) other).householdId)
                && toAdd.equals(((AddSessionCommand) other).toAdd));
    }
}

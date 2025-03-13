package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.HouseholdBook;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionDate;
import seedu.address.model.session.SessionTime;
import seedu.address.model.household.HouseholdId;
import seedu.address.model.Model;

/**
 * Adds a session to a household in the household book.
 */
public class AddSessionCommand extends Command {

    public static final String COMMAND_WORD = "add-session";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a session to a household. "
            + "Parameters: "
            + PREFIX_ID + "HOUSEHOLD_ID "
            + PREFIX_DATE + "DATE (in YYYY-MM-DD) "
            + PREFIX_TIME + "TIME (in HH:mm)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "1 "
            + PREFIX_DATE + "2025-03-15 "
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
     * Creates an AddSessionCommand to add the specified {@code Session} to the specified {@code Household}
     */
    public AddSessionCommand(HouseholdId householdId, SessionDate date, SessionTime time) {
        requireNonNull(householdId);
        requireNonNull(date);
        requireNonNull(time);
        
        this.householdId = householdId;
        this.toAdd = new Session(householdId, date, time);
    }

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

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddSessionCommand // instanceof handles nulls
                && householdId.equals(((AddSessionCommand) other).householdId)
                && toAdd.equals(((AddSessionCommand) other).toAdd));
    }
} 
package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import seedu.address.logic.commands.EditSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.HouseholdId;

/**
 * Parses input for the edit-session command.
 * Expected format: "edit-s id/HOUSEHOLD_ID-SESSION_INDEX [d/DATE] [tm/TIME] [n/NOTE]"
 * Example: "edit-s id/H000007-2 d/2025-09-27 tm/19:00"
 * Example with note: "edit-s id/H000007-2 n/Follow-up"
 */
public class EditSessionCommandParser implements Parser<EditSessionCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid format! Usage: edit-s id/<HOUSEHOLD_ID-SESSION_INDEX> [d/DATE] [tm/TIME] [n/NOTE]\n"
                    + "Example: edit-s id/H000007-2 d/2025-09-27 tm/19:00\n"
                    + "Example with note: edit-s id/H000007-2 n/Follow-up";

    public static final String MESSAGE_NO_FIELDS_PROVIDED =
            "At least one field to edit must be provided (date, time, or note).";

    @Override
    public EditSessionCommand parse(String userInput) throws ParseException {
        // Tokenize the input using the prefixes.
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_ID, PREFIX_DATE,
                PREFIX_TIME, PREFIX_NOTE);

        // The session identifier is required.
        if (!argMultimap.arePrefixesPresent(PREFIX_ID) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        // At least one field (date, time, or note) must be provided.
        if (!argMultimap.getValue(PREFIX_DATE).isPresent()
                && !argMultimap.getValue(PREFIX_TIME).isPresent()
                && !argMultimap.getValue(PREFIX_NOTE).isPresent()) {
            throw new ParseException(MESSAGE_NO_FIELDS_PROVIDED);
        }

        // Extract the session identifier and remove a leading "id/" if present.
        String sessionIdentifier = argMultimap.getValue(PREFIX_ID).get().trim();
        if (sessionIdentifier.startsWith("id/")) {
            sessionIdentifier = sessionIdentifier.substring(3).trim();
        }

        // Ensure the identifier is in the format "HOUSEHOLD_ID-SESSION_INDEX".
        if (!sessionIdentifier.contains("-")) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }
        String[] parts = sessionIdentifier.split("-", 2);
        if (parts.length < 2) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }
        String householdIdStr = parts[0].trim();
        String sessionIndexStr = parts[1].trim();

        if (!HouseholdId.isValidId(householdIdStr)) {
            throw new ParseException("Invalid household ID: " + householdIdStr);
        }
        HouseholdId householdId = HouseholdId.fromString(householdIdStr);

        int sessionIndex;
        try {
            sessionIndex = Integer.parseInt(sessionIndexStr);
        } catch (NumberFormatException e) {
            throw new ParseException("Session index must be an integer: " + sessionIndexStr);
        }

        // Optional fields: if present, trim and validate that they are not empty.
        String datePart = argMultimap.getValue(PREFIX_DATE).orElse(null);
        if (datePart != null) {
            datePart = datePart.trim();
            if (datePart.isEmpty()) {
                throw new ParseException("Date provided is empty.");
            }
        }

        String timePart = argMultimap.getValue(PREFIX_TIME).orElse(null);
        if (timePart != null) {
            timePart = timePart.trim();
            if (timePart.isEmpty()) {
                throw new ParseException("Time provided is empty.");
            }
        }

        String notePart = argMultimap.getValue(PREFIX_NOTE).orElse(null);
        if (notePart != null) {
            notePart = notePart.trim();
            if (notePart.isEmpty()) {
                throw new ParseException("Note provided is empty.");
            }
        }

        // Create the command with the parsed values. Any optional field not provided will be null.
        return new EditSessionCommand(householdId, sessionIndex, datePart, timePart, notePart);
    }
}


package seedu.address.logic.commands.exceptions;

/**
 * Represents an error which occurs during execution of a Command.
 */
public class CommandException extends Exception {
    /**
     * Constructs a new {@code CommandException} with the specified detail message.
     *
     * @param message The detail message providing information about the exception.
     */
    public CommandException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CommandException} with the specified detail message and cause.
     *
     * @param message The detail message providing information about the exception.
     * @param cause The cause of the exception, which can be retrieved later using {@link Throwable#getCause()}.
     */
    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}


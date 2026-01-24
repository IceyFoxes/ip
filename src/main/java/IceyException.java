/**
 * Represents an exception specific to the Icey application.
 * Thrown when user input is invalid or a command cannot be executed.
 */
public class IceyException extends Exception {
    /**
     * Creates a new IceyException with the specified error message.
     *
     * @param message The error message describing the exception.
     */
    public IceyException(String message) {
        super(message);
    }
}

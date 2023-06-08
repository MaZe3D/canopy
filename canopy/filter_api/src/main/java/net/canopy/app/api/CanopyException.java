package net.canopy.app.api;

/**
 * The CanopyException class is an exception that is thrown when an error occurs in the Canopy application, or core API.
 */
public class CanopyException extends Exception {

    /**
     * Creates a new CanopyException with the given message.
     *
     * @param source  The source of the exception. Typically the name of the class that threw the exception.
     * @param message The message of the exception.
     */
    public CanopyException(String source, String message) {
        super(source + ": " + message);
    }
}

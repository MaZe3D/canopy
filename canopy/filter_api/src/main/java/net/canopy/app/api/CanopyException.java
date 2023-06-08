package net.canopy.app.api;

/**
 * The CanopyException class is an exception that is thrown when an error occurs in the Canopy application, or core API.
 * This exceptions indicate Fatal errors in the program routine and should be reported to the developer.
 */
public class CanopyException extends Exception {
    private static String prependText = "A fatal problem in the program routine has occurred. Please contact the developer. Additional information: ";

    /**
     * Creates a new CanopyException with the given message.
     * The message is prefixed with the following text: "A fatal problem in the program routine has occurred. Please contact the developer. Additional information: "
     *
     * @param source  The source of the exception. Typically the name of the class that threw the exception.
     * @param message The message of the exception.
     */
    public CanopyException(String source, String message) {
        super(prependText + source + ": " + message);
    }
}

package net.canopy.app.api;

/**
 * The CanopyException class is an exception that is thrown when an error occurs in the Canopy application, or core API.
 * This exceptions indicate Fatal errors in the program routine and should be reported to the developer.
 */
public class CanopyException extends Exception {
    private static String prependText = "A fatal problem in the program routine has occurred. Please contact the developer. Additional information: ";

    public CanopyException(String soruce, String message) {
        super(prependText + soruce + ": " + message);
    }
}

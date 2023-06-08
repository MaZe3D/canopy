package net.canopy.app.api;

public class CanopyException extends Exception {
    private static String prependText = "A fatal problem in the program routine has occurred. Please contact the developer. Additional information: ";

    public CanopyException(String soruce, String message) {
        super(prependText + soruce + ": " + message);
    }
}

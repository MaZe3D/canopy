package net.canopy.app.api;

/**
 * The FilterException class is an exception that is thrown when an error occurs while applying a filter.
 */

public class FilterException extends Exception {

    /**
     * The filter that threw the exception.
     */
    private IFilter filter;

    /**
     * Creates a new FilterException with the given message.
     *
     * @param filter  The filter that threw the exception.
     * @param message The message of the exception.
     */
    public FilterException(IFilter filter, String message) {
        super(message);
        this.filter = filter;
    }
}
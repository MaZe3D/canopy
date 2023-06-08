package net.canopy.app.api;

public class FilterException extends Exception {
    IFilter filter;

    public FilterException(IFilter filter, String message) {
        super(message);
        this.filter = filter;
    }
}

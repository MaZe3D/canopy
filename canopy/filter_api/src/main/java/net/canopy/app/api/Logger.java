package net.canopy.app.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The Logger class is used to log messages to the standard error stream. This helps utulize the standard out stream for filter output.
 */
public class Logger {
    String source;

    /**
     * Creates a new Logger with the given source.
     *
     * @param source The source of the logger. Typically the name of the class that created the logger.
     */
    public Logger(String source) {
        this.source = source;
    }

    /**
     * Logs the given message to the standard error stream.
     * The message is prefixed with the current date and time and the source of the logger.
     * The message is logged in the following format: yyyy-MM-dd HH:mm:ss - [source]: [message]
     *
     * @param message The message to log.
     */
    public void log(String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        System.err.println(dtf.format(now) + " - " + source + ": " + message);
    }
}

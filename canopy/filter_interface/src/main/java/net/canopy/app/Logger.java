package net.canopy.app;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    String source;

    public Logger(String source) {
        this.source = source;
    }

    public void log(String message) {
        //Log in the following format:
        // 2017-01-01 00:00:00 - source: message

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        System.err.println(dtf.format(now) + " - " + source + ": " + message);
    }
}
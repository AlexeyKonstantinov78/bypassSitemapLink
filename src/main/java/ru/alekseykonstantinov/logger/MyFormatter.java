package ru.alekseykonstantinov.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {
    // ANSI цветовые коды
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    @Override
    public String format(LogRecord record) {
        String color;
        if (record.getLevel() == Level.SEVERE) {
            color = ANSI_RED;
        } else if (record.getLevel() == Level.WARNING) {
            color = ANSI_YELLOW;
        } else if (record.getLevel() == Level.INFO) {
            color = ANSI_GREEN;
        } else if (record.getLevel() == Level.CONFIG) {
            color = ANSI_CYAN;
        } else if (record.getLevel() == Level.FINE) {
            color = ANSI_BLUE;
        } else {
            color = ANSI_WHITE;
        }

        return String.format(
                "%1s[%2$s] [%3$-7s] %4$s: %5$s %6$s [%7$s] %n",
                color,
                getDateFormat(),
                record.getLevel().getName(),
                record.getSourceClassName(),
                record.getSourceMethodName(),
                record.getLongThreadID(),
                record.getMessage(),
                ANSI_RESET
        );
    }

    public static String getDateFormat() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return sdf.format(now);
    }
}

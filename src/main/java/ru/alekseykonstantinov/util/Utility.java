package ru.alekseykonstantinov.util;

import ru.alekseykonstantinov.logger.MyLogger;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

public class Utility {
    private final static Logger logger = MyLogger.logger();

    public static String getDateFormat() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return sdf.format(now);
    }

    public static void printInputStream(InputStream input) throws IOException {
        logger.info("\nTeлo: ");
        int с;
        // Прочитать и отобразить все тело.
        while ((с = input.read()) != -1) {
            System.out.print((char) с);
        }
    }

    public static boolean searchMatchParameters(String param, String[] args) {

        for (String arg : args) {
            if (arg.equals(param)) {
                return true;
            }
        }

        return false;
    }
}

package ru.alekseykonstantinov.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger {
    private static final Logger logger = Logger.getLogger(MyLogger.class.getName());

    private MyLogger() {
        // Убираем обработчики по умолчанию (если нужно)
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }

        // Создаем новый обработчик (например, ConsoleHandler)
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new MyFormatter()); // Устанавливаем наш форматтер
        // Добавляем обработчик к нашему логгеру
        logger.setLevel(Level.ALL);
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }

    public static Logger logger() {
        MyLogger myLogger = new MyLogger();
        return logger;
    }
}

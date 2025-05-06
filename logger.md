## Изменение формата вывода в java.util.logging.Logger

Чтобы изменить формат вывода логов в java.util.logging.Logger, вам нужно настроить обработчик (Handler) и задать для
него собственный форматтер (Formatter). Вот как это можно сделать:

1. Создание собственного Formatter

        import java.util.Date;
        import java.util.logging.Formatter;
        import java.util.logging.LogRecord;
        
        public class MyFormatter extends Formatter {    
            @Override
            public String format(LogRecord record) {
             return String.format("[%1$tF %1$tT] [%2$-7s] %3$s %n",
             new Date(record.getMillis()),
             record.getLevel().getName(),
             record.getMessage());
            }
        }
2. Настройка Logger с пользовательским Formatter
   import java.util.logging.*;

   public class LoggerExample {
   private static final Logger logger = Logger.getLogger(LoggerExample.class.getName());

        public static void main(String[] args) {
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
            logger.addHandler(handler);
            logger.setLevel(Level.ALL);
            handler.setLevel(Level.ALL);
            
            // Пример вывода логов
            logger.severe("Это сообщение уровня SEVERE");
            logger.warning("Это сообщение уровня WARNING");
            logger.info("Это сообщение уровня INFO");
            logger.config("Это сообщение уровня CONFIG");
            logger.fine("Это сообщение уровня FINE");
        }
   }

3. Альтернативный вариант - использование SimpleFormatter с параметрами

Вы можете изменить формат вывода через конфигурационный файл. Создайте файл logging.properties:

    import java.io.IOException;
    import java.util.logging.*;
    
    public class LoggerConfigExample {
    public static void main(String[] args) {
    try {
    LogManager.getLogManager().readConfiguration(
    LoggerConfigExample.class.getResourceAsStream("/logging.properties"));
    } catch (IOException e) {
    System.err.println("Could not setup logger configuration: " + e.toString());
    }
    
            Logger logger = Logger.getLogger(LoggerConfigExample.class.getName());
            logger.info("Тестовое сообщение с новым форматом");
        }
    }

### Доступные параметры для формата

В строке формата можно использовать следующие параметры:

* %1 - дата/время
* %2 - уровень логирования
* %3 - сообщение
* %4 - имя класса
* %5 - имя метода
* %6 - идентификатор потока
* %7 - имя логгера

#### Примеры форматов:

[%1$tF %1$tT] [%2$-7s] %3$s %n → [2023-05-15 14:30:45] [INFO ] Сообщение

%1$tH:%1$tM:%1$tS %2$s%n%4$s: %5$s%6$s%n → 14:30:45 INFO\nClassName: methodNameThreadID

Таким образом, вы можете полностью настроить формат вывода логов под свои потребности.

      Полная таблица уровней:
      Уровень	Когда использовать	Пример использования
      SEVERE	Критические ошибки	Ошибки подключения к БД, фатальные исключения
      WARNING	Проблемы, не мешающие работе	Устаревший API, почти полная память
      INFO	Информационные сообщения	Старт/остановка сервиса
      CONFIG	Конфигурационные сообщения	Загруженные настройки
      FINE	Детальная отладочная информация	Вход/выход из метода
      FINER	Более детальная отладочная информация	Промежуточные результаты
      FINEST	Максимально детальная отладочная информация	Значения переменных в цикле
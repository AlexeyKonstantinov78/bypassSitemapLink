package ru.alekseykonstantinov.storage;

import ru.alekseykonstantinov.logger.MyLogger;
import ru.alekseykonstantinov.model.Link;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class LinkFileStorage {
    private final static Logger logger = MyLogger.logger();
    Path path = Path.of("data", "AlllinkUrl.txt");

    public void saveLink(List<Link> listUrl) throws FileNotFoundException {

        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(path, listUrl.stream().map(Link::link).toList(), StandardCharsets.UTF_8);
            logger.info("Список ссылок сохранен в файл!");
        } catch (IOException ex) {
            logger.warning("Не удалось сохранить список ссылок: " + ex.getMessage());
            throw new FileNotFoundException("Не удалось сохранить");
        }
    }
}

package ru.alekseykonstantinov.service;

import ru.alekseykonstantinov.logger.MyLogger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ParseXml {
    private final static Logger logger = MyLogger.logger();
    private static final List<String> listXml = new ArrayList<>();
    private static final List<String> listUrl = new ArrayList<>();

    public void parseXml(InputStream input, String type) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(input);

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamReader.START_ELEMENT) {

                    if (type.equals("sitemap") && reader.getLocalName().equals("loc")) {
                        String loc = reader.getElementText();
                        listXml.add(loc);
                    }

                    if (type.equals("url") && reader.getLocalName().equals("loc") && !reader.getPrefix().equals("image")) {
                        String loc = reader.getElementText();
                        listUrl.add(loc);
                    }
                }
            }
        } catch (Exception e) {
            logger.severe(String.format("Ошибка в методе parseXml при parse %s %s", type, e.getMessage()));
        }
    }

    public List<String> getListXml() {
        return new ArrayList<>(listXml);
    }

    public List<String> getListUrl() {
        return new ArrayList<>(listUrl);
    }
}

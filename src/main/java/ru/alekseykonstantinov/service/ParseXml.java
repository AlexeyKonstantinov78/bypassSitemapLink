package ru.alekseykonstantinov.service;

import ru.alekseykonstantinov.logger.MyLogger;
import ru.alekseykonstantinov.storage.LinkFileStorage;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ParseXml {
    private final static Logger logger = MyLogger.logger();
    private final List<String> listXml = new ArrayList<>();
    private final List<String> listUrl = new ArrayList<>();
    private final LinkFileStorage linkFileStorage;

    public ParseXml() {
        this.linkFileStorage = new LinkFileStorage();
    }

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

    public boolean saveUrl() {
        try {
            linkFileStorage.saveLink(getListUrl());
        }  catch (FileNotFoundException ex) {
            return false;
        }
        return true;
    }
}

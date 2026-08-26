package ru.alekseykonstantinov.app;

import ru.alekseykonstantinov.logger.MyLogger;
import ru.alekseykonstantinov.service.LinkType;
import ru.alekseykonstantinov.service.ParseXml;
import ru.alekseykonstantinov.service.SendingRequest;
import ru.alekseykonstantinov.util.Utility;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

public class ParserXmlSitemap {

    public static void main(String[] args) {
        boolean errLink = true;

        final Logger logger = MyLogger.logger();

        final SendingRequest sendingRequest = new SendingRequest();
        final ParseXml parseXml = new ParseXml();
        final LinkType linkType = new LinkType(sendingRequest, parseXml);

        String url = "https://nasosyvodoly.ru/sitemap_index.xml";

        linkType.getLinkType(url, "sitemap");

        List<String> listXml = parseXml.getListXml();
        if (!listXml.isEmpty()) {
            logger.info("Запуск обхода listXml");
            listXml.forEach(urlXml ->
                    linkType.getLinkType(urlXml, "url")
            );
        }

        List<String> listUrl = parseXml.getListUrl();
        logger.info(String.format("Количество ссылок sitemap: %1d", listXml.size()));
        logger.info(String.format("Количество всех ссылок на страницы: %1d", listUrl.size()));

        if (args.length > 0 && Utility.searchMatchParameters("save", args)) {
            parseXml.saveUrl();
        }

        if (!listUrl.isEmpty()) {
            logger.info("Запуск обхода listUrl");

            listUrl.forEach(urlPost ->
                    {
                        try {
                            sendingRequest.sendHttpClient(new URI(urlPost));
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                            sendingRequest.addErr(urlPost + "; " + e.getMessage());
                        }
                    }
            );
        }

        while (errLink) {
            List<String> sendErrorUrl = sendingRequest.getErrorUrl();
            if (sendErrorUrl.isEmpty()) {
                logger.info("Ошибок нет");
                break;
            }
            logger.info(String.format("Есть ошибки %d", sendErrorUrl.size()));
            logger.info("Проход по ошибкам");

            String urlErr = sendErrorUrl.getFirst().split(";")[0];

            try {
                sendingRequest.sendHttpClient(new URI(urlErr));
                sendingRequest.deleteErrLinkFirst();
            } catch (Exception e) {
                logger.warning(e.getMessage());
            }

            errLink = !sendingRequest.getErrorUrl().isEmpty();
        }
    }


}

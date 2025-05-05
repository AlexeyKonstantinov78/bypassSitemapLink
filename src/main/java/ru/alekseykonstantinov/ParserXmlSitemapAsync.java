package ru.alekseykonstantinov;

import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Slf4j
public class ParserXmlSitemapAsync {

    private static List<String> listXml = new ArrayList<>();
    private static List<String> listUrl = new ArrayList<>();
    private static Boolean isAsync = true;
    private static List<String> errorUrl = new ArrayList<>();


    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        String url = "https://nasosyvodoly.ru/sitemap_index.xml";

        getLinkType(url, "sitemap");

        if (!listXml.isEmpty()) {
            log.info("Запуск обхода listXml");

            listXml.stream().forEach(urlXml ->
                    getLinkType(urlXml, "url")
            );
        }

        log.info("Количество ссылок sitemap: " + listXml.size());
        log.info("Количество всех ссылок на страницы: " + listUrl.size());

        if (!listUrl.isEmpty()) {
            log.info("Запуск обхода listUrl");
            listUrl.stream().forEach(urlPost ->
                    {
                        try {
                            sendHttpClientAsync(new URI(urlPost));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }

        if (!errorUrl.isEmpty()) {
            errorUrl.stream().forEach(System.out::println);
        } else {
            log.info("Ошибок нет");
        }
    }

    public static void getLinkType(String url, String type) {
        try {
            HttpResponse<InputStream> httpResponseU = sendHttpClient(new URI(url));
            InputStream inputU = httpResponseU.body();
            parseXml(inputU, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpResponse<InputStream> sendHttpClient(URI uri) throws InterruptedException, IOException {

        HttpClient hl2 = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<InputStream> httpResponse = hl2.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        log.info(getDateFormat() + " " + uri + " status code: " + httpResponse.statusCode());

        return httpResponse;
    }

    public static void sendHttpClientAsync(URI uri) {
        HttpClient hl2 = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri).build();
        hl2.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofInputStream())
                .thenAccept(response -> {
                    log.info(getDateFormat() + " " + uri + " status code: " + response.statusCode());
                    if (response.statusCode() != 200) {
                        errorUrl.add(uri + " status code: " + response.statusCode());
                    }
                });
    }

    public static void parseXml(InputStream input, String type) {
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

                    if (type.equals("url") && reader.getLocalName().equals("loc")) {
                        if (reader.getLocalName().equals("loc")) {
                            String loc = reader.getElementText();
                            listUrl.add(loc);
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void printInputStream(InputStream input) throws IOException {
        log.info("\nTeлo: ");
        int с;
        // Прочитать и отобразить все тело .
        while ((с = input.read()) != -1) {
            System.out.print((char) с);
        }
    }

    public static String getDateFormat() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return sdf.format(now);
    }


}

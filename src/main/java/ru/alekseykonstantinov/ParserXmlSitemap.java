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
public class ParserXmlSitemap {
    
    private static List<String> listXml = new ArrayList<>();
    private static List<String> listUrl = new ArrayList<>();

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI("https://nasosyvodoly.ru/sitemap_index.xml");

        HttpResponse<InputStream> httpResponse = sendHttpClient(uri);
        InputStream input = httpResponse.body();
        //printInputStream(input);
        parseXml(input, "sitemap");

        if (!listXml.isEmpty()) {
            System.out.println("Запуск обхода listXml");
            listXml.stream().forEach(url -> {
                try {
                    HttpResponse<InputStream> httpResponseU = sendHttpClient(new URI(url));
                    InputStream inputU = httpResponseU.body();
                    parseXml(inputU, "url");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        System.out.println(listXml.size());
        System.out.println(listUrl.size());

        if (!listUrl.isEmpty()) {
            listUrl.stream().forEach(url ->
                    {
                        try {
                            sendHttpClient(new URI(url));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }
    }

    public static HttpResponse<InputStream> sendHttpClient(URI uri) throws IOException, InterruptedException {

        HttpClient hl2 = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<InputStream> httpResponse = hl2.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        log.info(getDateFormat() + " " + uri + " status code: " + httpResponse.statusCode());
        //System.out.println(getDateFormat() + " " + uri + " status code: " + httpResponse.statusCode());
        return httpResponse;
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
                        //System.out.println("loc: " + loc);
                        listXml.add(loc);
                    }

                    if (type.equals("url") && reader.getLocalName().equals("loc")) {
                        if (reader.getLocalName().equals("loc")) {
                            String loc = reader.getElementText();
                            //System.out.println("loc: " + loc);
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
        System.out.println("\nTeлo: ");
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

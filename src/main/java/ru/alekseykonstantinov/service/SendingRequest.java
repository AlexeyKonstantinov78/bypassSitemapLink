package ru.alekseykonstantinov.service;

import ru.alekseykonstantinov.logger.MyLogger;
import ru.alekseykonstantinov.model.Link;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SendingRequest {
    private final Logger logger = MyLogger.logger();
    private final List<Link> errorUrl = new ArrayList<>();

    public HttpResponse<InputStream> sendHttpClient(URI uri) throws InterruptedException, IOException {

        Long start = System.nanoTime();

        HttpClient hl2 = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<InputStream> httpResponse = hl2.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        long end = System.nanoTime();
        double time = (end - start) / 1_000_000_000.0;
        logger.info(String.format("%s; status code: %s time(сек.) %s", uri, httpResponse.statusCode(), time));
        if (httpResponse.statusCode() != 200) {
            errorUrl.add(new Link(String.format("%s; status code: %s time(сек.) %s", uri, httpResponse.statusCode(), time)));
            throw new IOException(String.format("Код: %s", httpResponse.statusCode()));
        }

        return httpResponse;
    }

    public HttpResponse<InputStream> sendHttpClient(HttpClient hl2, URI uri) throws InterruptedException, IOException {
        Long start = System.nanoTime();

        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<InputStream> httpResponse = hl2.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        long end = System.nanoTime();
        double time = (end - start) / 1_000_000_000.0;
        logger.info(String.format("%s; status code: %s time(сек.) %s", uri, httpResponse.statusCode(), time));
        if (httpResponse.statusCode() != 200) {
            errorUrl.add(new Link(String.format("%s; status code: %s time(сек.) %s", uri, httpResponse.statusCode(), time)));
            throw new IOException(String.format("Код: %s", httpResponse.statusCode()));
        }

        return httpResponse;
    }

    public List<Link> getErrorUrl() {
        return new ArrayList<>(errorUrl);
    }

    public void addErr(Link err) {
        errorUrl.add(err);
    }

    public void deleteErrLinkFirst() {
        errorUrl.removeFirst();
    }
}

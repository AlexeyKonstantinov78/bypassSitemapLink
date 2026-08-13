package ru.alekseykonstantinov.service;

import ru.alekseykonstantinov.logger.MyLogger;

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
    private final List<String> errorUrl = new ArrayList<>();

    public HttpResponse<InputStream> sendHttpClient(URI uri) throws InterruptedException, IOException {

        HttpClient hl2 = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<InputStream> httpResponse = hl2.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        logger.info(String.format("%s; status code: %s", uri, httpResponse.statusCode()));
        if (httpResponse.statusCode() != 200) {
            errorUrl.add(String.format("%s; status code: %s", uri, httpResponse.statusCode()));
            throw new IOException(String.format("Код: %s", httpResponse.statusCode()));
        }

        return httpResponse;
    }

    public HttpResponse<InputStream> sendHttpClient(HttpClient hl2, URI uri) throws InterruptedException, IOException {

        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<InputStream> httpResponse = hl2.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        logger.info(String.format("%s; status code: %s", uri, httpResponse.statusCode()));
        if (httpResponse.statusCode() != 200) {
            errorUrl.add(String.format("%s; status code: %s", uri, httpResponse.statusCode()));
            throw new IOException(String.format("Код: %s", httpResponse.statusCode()));
        }

        return httpResponse;
    }

    public List<String> getErrorUrl() {
        return new ArrayList<>(errorUrl);
    }

    public void addErr(String err) {
        errorUrl.add(err);
    }
}

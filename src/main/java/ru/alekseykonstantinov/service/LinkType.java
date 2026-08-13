package ru.alekseykonstantinov.service;

import ru.alekseykonstantinov.logger.MyLogger;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class LinkType {
    private final static Logger logger = MyLogger.logger();
    private SendingRequest sendingRequest;
    private ParseXml parseXml;

    public LinkType(SendingRequest sendingRequest, ParseXml parseXml) {
        this.sendingRequest = sendingRequest;
        this.parseXml = parseXml;
    }

    public void getLinkType(String url, String type) {
        try (HttpClient hl2 = HttpClient.newHttpClient()) {
            HttpResponse<InputStream> httpResponseU = sendingRequest.sendHttpClient(hl2, new URI(url));
            InputStream inputU = httpResponseU.body();
            parseXml.parseXml(inputU, type);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            sendingRequest.addErr(String.format("%s; %s ", url, e.getMessage()));
        }
    }
}

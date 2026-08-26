package ru.alekseykonstantinov.service;

import ru.alekseykonstantinov.logger.MyLogger;
import ru.alekseykonstantinov.model.Link;
import ru.alekseykonstantinov.model.XmlUrl;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class LinkType {
    private final Logger logger = MyLogger.logger();
    private final SendingRequest sendingRequest;
    private final ParseXml parseXml;

    public LinkType(SendingRequest sendingRequest, ParseXml parseXml) {
        this.sendingRequest = sendingRequest;
        this.parseXml = parseXml;
    }

    public void getLinkType(XmlUrl url, String type) {
        try (HttpClient hl2 = HttpClient.newHttpClient()) {
            HttpResponse<InputStream> httpResponseU = sendingRequest.sendHttpClient(hl2, new URI(url.url()));
            InputStream inputU = httpResponseU.body();
            parseXml.parseXml(inputU, type);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            sendingRequest.addErr(new Link(String.format("%s; %s ", url, e.getMessage())));
        }
    }

    public void getLinkType(Link url, String type) {
        try (HttpClient hl2 = HttpClient.newHttpClient()) {
            HttpResponse<InputStream> httpResponseU = sendingRequest.sendHttpClient(hl2, new URI(url.link()));
            InputStream inputU = httpResponseU.body();
            parseXml.parseXml(inputU, type);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            sendingRequest.addErr(new Link(String.format("%s; %s ", url, e.getMessage())));
        }
    }
}

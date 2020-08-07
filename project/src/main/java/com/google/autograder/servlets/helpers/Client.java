package com.google.autograder.servlets.helpers;

import java.util.Optional;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStreamReader;
import org.json.simple.JSONObject;
import java.util.stream.Collectors;
import org.json.simple.parser.JSONParser;
import java.nio.charset.StandardCharsets;
import org.json.simple.parser.ParseException;

public final class Client {

    public static final String CLIENT_ID;
    public static final String CLIENT_SECRET;

    private static final String CREDENTIALS_PATH = "auth/credentials.json";
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    static {
        InputStream inputStream = Client.class.getClassLoader().getResourceAsStream(CREDENTIALS_PATH);

        String json = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        ).lines().collect(Collectors.joining("\n"));

        JSONObject web;

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            web = (JSONObject) jsonObject.get("web");
        } catch(ParseException exception) {
            LOGGER.log(Level.SEVERE, "Error retrieving credentials from the resources folder at: " + CREDENTIALS_PATH, exception);
            throw new RuntimeException(exception);
        }

        CLIENT_ID = web.get("client_id").toString();
        CLIENT_SECRET = web.get("client_secret").toString();
    }

}
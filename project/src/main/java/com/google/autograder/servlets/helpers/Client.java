package com.google.autograder.servlets.helpers;

import java.io.File;
import java.util.Optional;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.simple.JSONObject;
import java.util.stream.Collectors;
import org.json.simple.parser.JSONParser;
import java.nio.charset.StandardCharsets;
import org.json.simple.parser.ParseException;

public final class Client {

    private static final String CREDENTIALS_PATH = "auth/credentials.json";
    private static final JSONObject CREDENTIALS_WEB_JSON_CONTENT = retrieveCredentialsWebContent().get();

    public static final String CLIENT_ID = retrieveClientID().get();
    public static final String CLIENT_SECRET = retrieveClientSecret().get();

    private static Optional<JSONObject> retrieveCredentialsWebContent() {
        InputStream inputStream = Client.class.getClassLoader().getResourceAsStream(CREDENTIALS_PATH);

        String json = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        ).lines().collect(Collectors.joining("\n"));

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            JSONObject web = (JSONObject) jsonObject.get("web");
            return Optional.of(web);
        } catch(ParseException e) {
            return Optional.empty();
        }
    }

    private static Optional<String> retrieveClientID() {
        try {
            return Optional.of(CREDENTIALS_WEB_JSON_CONTENT.get("client_id").toString());
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static Optional<String> retrieveClientSecret() {
        try {
            return  Optional.of(CREDENTIALS_WEB_JSON_CONTENT.get("client_secret").toString());
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
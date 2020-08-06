package com.google.autograder.servlets.helpers;

import java.io.File;
import java.util.Optional;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.simple.JSONObject;
import java.util.stream.Collectors;
import org.json.simple.parser.JSONParser;
import java.nio.charset.StandardCharsets;

public final class Client {

    private static final String CREDENTIALS_PATH = "auth/credentials.json";

    public static final String CLIENT_ID;
    public static final String CLIENT_SECRET;

    static {
        CLIENT_ID = getClientID().get();
        CLIENT_SECRET = getClientSecret().get();
    }

    private static Optional<String> getClientID() {
        try {
            InputStream inputStream = Client.class.getClassLoader().getResourceAsStream(CREDENTIALS_PATH);

            String json = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            ).lines().collect(Collectors.joining("\n"));

            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            JSONObject web = (JSONObject) new JSONParser().parse(jsonObject.get("web").toString());
            
            return Optional.of(web.get("client_id").toString());
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static Optional<String> getClientSecret() {
        try {            
            InputStream inputStream = Client.class.getClassLoader().getResourceAsStream(CREDENTIALS_PATH);
            
            String json = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            ).lines().collect(Collectors.joining("\n"));

            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            JSONObject web = (JSONObject) new JSONParser().parse(jsonObject.get("web").toString());
            
            return  Optional.of(web.get("client_secret").toString());
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
package com.google.autograder.servlets.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public final class Client {

    public static final String CLIENT_ID = getClientID();
    public static final String CLIENT_SECRET = getClientSecret();

    private static final String RESOURCES_PATH = "../../../project/src/main/resources"; // "../classes"
    private static final String CREDENTIALS_PATH = "/auth/credentials.json";

    private static String getClientID() {
        String client_id = null;

        try {
            String json = new String(Files.readAllBytes(Paths.get(RESOURCES_PATH + CREDENTIALS_PATH)));
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            JSONObject credentials = (JSONObject) new JSONParser().parse(jsonObject.get("credentials").toString());
            client_id = credentials.get("client_id").toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return client_id;
    }

    private static String getClientSecret() {
        String client_secret = null;

        try {
            String json = new String(Files.readAllBytes(Paths.get(RESOURCES_PATH + CREDENTIALS_PATH)));
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            JSONObject credentials = (JSONObject) new JSONParser().parse(jsonObject.get("credentials").toString());
            client_secret = credentials.get("client_secret").toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return client_secret;
    }

}
package com.google.autograder.servlets.helpers;

import java.net.URL;
import java.io.File;
import java.util.Iterator;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URLEncoder;
import java.io.IOException;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.json.simple.JSONObject;
import java.net.ProtocolException;
import java.net.MalformedURLException;
import org.json.simple.parser.JSONParser;
import java.nio.charset.StandardCharsets;
import com.google.autograder.data.Database;
import java.io.UnsupportedEncodingException;
import com.google.autograder.data.UserHandler;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.autograder.servlets.auth.AccessTokenResponse;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public final class API {

    private static final String RESOURCES_PATH  = "../../../project/src/main/resources"; // "../classes"
    private static final Logger LOGGER = Logger.getLogger(API.class.getName());
    private static final String API_KEY_PATH = "/auth/api_key.json";
    private static final Gson GSON = new Gson();

    public static final String UTF_8 = StandardCharsets.UTF_8.name();
    public static final String API_KEY;

    static {
        API_KEY = getAPIKey();
    }
    
    private static String getAPIKey() {
        String apiKeyPath = RESOURCES_PATH + API_KEY_PATH;
        String api_key = null;

        try {
            String json = new String(Files.readAllBytes(Paths.get(apiKeyPath)));
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            JSONObject apiKeyObject = (JSONObject) new JSONParser().parse(jsonObject.get("api_key").toString());
            api_key = apiKeyObject.get("api_key").toString();
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving API Key from the file system at:" + apiKeyPath, e);
        }
        
        return api_key;
    }

    public static String getCurrentUserAPIAuthorization() {
        String userEmail = UserHandler.getCurrentUserEmail();
        Filter userEmailFilter = new FilterPredicate("user_email", FilterOperator.EQUAL, userEmail);
        Query query = new Query("AccessTokenResponse").setFilter(userEmailFilter).addSort("expires_in", SortDirection.DESCENDING);

        Iterator<Entity> results = Database.query(query).iterator();
        AccessTokenResponse accessTokenResponse = null;
        String authorization = null;

        if (results.hasNext()) {
            accessTokenResponse = AccessTokenResponse.buildAccessTokenResponseFromDatastoreEntity(results.next());
        }

        if (accessTokenResponse != null) {
            authorization = (accessTokenResponse.getToken_Type() + " " + accessTokenResponse.getAccess_Token());
        }
        
        return authorization;
    }

    public static HttpURLConnection getAuthenticatedRequest(String method, String endpoint) {
        String authorization = getCurrentUserAPIAuthorization();
        
        if (authorization == null) {
            return null;
        }

        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) new URL(endpoint).openConnection();
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening connection to given endpoint: " + endpoint, e);
            return null;
        }

        try {
            connection.setRequestMethod(method);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", authorization);
        } catch(ProtocolException e) {
            LOGGER.log(Level.SEVERE, "Error setting request properties on the http endpoint connection.", e);
            return null;
        }    

        return connection;
    }

    public static String getJSON(HttpURLConnection connection) throws IOException, UnsupportedEncodingException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), UTF_8));
        StringBuilder jsonBuffer = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            jsonBuffer.append(line);
            jsonBuffer.append(System.lineSeparator());
        }

        bufferedReader.close();

        return jsonBuffer.toString();
    }

    public static String urlEncode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, UTF_8);
    }

}
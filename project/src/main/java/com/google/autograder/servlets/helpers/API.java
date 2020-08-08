package com.google.autograder.servlets.helpers;

import java.net.URL;
import java.util.Optional;
import java.util.Iterator;
import java.net.URLEncoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.json.simple.JSONObject;
import java.net.ProtocolException;
import java.util.stream.Collectors;
import java.net.MalformedURLException;
import org.json.simple.parser.JSONParser;
import java.nio.charset.StandardCharsets;
import com.google.autograder.data.Database;
import java.io.UnsupportedEncodingException;
import org.json.simple.parser.ParseException;
import com.google.autograder.data.UserHandler;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.autograder.servlets.auth.AccessTokenResponse;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public final class API {

    public static final String API_KEY;
    public static final String UTF_8 = StandardCharsets.UTF_8.name();

    private static final String API_KEY_PATH = "auth/api_key.json";
    private static final Logger LOGGER = Logger.getLogger(API.class.getName());

    static {
        InputStream inputStream = API.class.getClassLoader().getResourceAsStream(API_KEY_PATH);
            
        String json = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        ).lines().collect(Collectors.joining("\n"));

        JSONObject apiKeyObject;

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            apiKeyObject = (JSONObject) jsonObject.get("api_key");
        } catch(ParseException exception) {
            LOGGER.log(Level.SEVERE, "Error parsing API Key from api_key.json at:" + API_KEY_PATH, exception);
            throw new RuntimeException(exception);
        }

        API_KEY = apiKeyObject.get("api_key").toString();
    }

    public static String getCurrentUserAPIAuthorization() {
        String userEmail = UserHandler.getCurrentUserEmail();
        Filter userEmailFilter = new FilterPredicate("user_email", FilterOperator.EQUAL, userEmail);
        Query query = new Query("AccessTokenResponse").setFilter(userEmailFilter).addSort("expires", SortDirection.DESCENDING);

        Iterator<Entity> results = Database.query(query).iterator();
        AccessTokenResponse accessTokenResponse = null;

        if (!results.hasNext()) {
            return null;
        }
        
        accessTokenResponse = AccessTokenResponse.buildAccessTokenResponseFromDatastoreEntity(results.next());

        if (accessTokenResponse == null) {
            return null;
        }

        return (accessTokenResponse.getToken_Type() + " " + accessTokenResponse.getAccess_Token());
    }

    public static Optional<HttpURLConnection> getAuthenticatedRequest(String method, String endpoint) {
        String authorization = getCurrentUserAPIAuthorization();
        HttpURLConnection connection;
        
        if (authorization == null) {
            return Optional.empty();
        }

        try {
            connection = (HttpURLConnection) new URL(endpoint).openConnection();
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening connection to given endpoint: " + endpoint, e);
            return Optional.empty();
        }

        try {
            connection.setRequestMethod(method);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", authorization);
        } catch(ProtocolException e) {
            LOGGER.log(Level.SEVERE, "Error setting request properties on the http endpoint connection.", e);
            return Optional.empty();
        }    

        return Optional.of(connection);
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
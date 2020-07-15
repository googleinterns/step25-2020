package com.google.autograder.servlets.helpers;


import java.net.URLEncoder;
import java.io.IOException;
import java.net.URLEncoder;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.autograder.servlets.helpers.Services;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.autograder.servlets.auth.AccessTokenResponse;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public final class API {

    public static String API_KEY = "AIzaSyBIaLkphMl31DiaklesnnZNdNJ_ailldto";
    public static String UTF_8 = StandardCharsets.UTF_8.name();
    public static Gson GSON = new Gson();

    public static String getCurrentUserAPIAuthorization() {
        String userEmail = Services.USER_SERVICE.getCurrentUser().getEmail();

        Filter userEmailFilter = new FilterPredicate("user_email", FilterOperator.EQUAL, userEmail);

        Query query = new Query("AccessTokenResponse").setFilter(userEmailFilter).addSort("expires_in", SortDirection.DESCENDING);;

        PreparedQuery results = Services.DATA_STORE.prepare(query);

        AccessTokenResponse accessTokenResponse = null;

        Entity entity = (results.asIterable().iterator().hasNext() ? results.asIterable().iterator().next() : null); ;

        if (entity != null) {
            accessTokenResponse = AccessTokenResponse.buildAccessTokenResponseFromDatastoreEntity(entity);
        }

        String authorization = null;

        if (accessTokenResponse != null) {
            authorization = (accessTokenResponse.getToken_Type() + " " + accessTokenResponse.getAccess_Token());
        }
        
        return authorization;
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

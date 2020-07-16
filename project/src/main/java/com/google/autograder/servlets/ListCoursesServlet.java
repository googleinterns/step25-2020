package com.google.autograder.servlets;

import java.net.URL;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import javax.servlet.http.HttpServlet;
import java.nio.charset.StandardCharsets;
import javax.servlet.annotation.WebServlet;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.autograder.servlets.helpers.Services;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.autograder.servlets.auth.AccessTokenResponse;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@WebServlet("/listCourses")
public final class ListCoursesServlet extends HttpServlet {

    private static String END_POINT = "https://classroom.googleapis.com/v1/courses";
    private static String API_KEY = "AIzaSyBIaLkphMl31DiaklesnnZNdNJ_ailldto";
    private static String UTF_8 = StandardCharsets.UTF_8.name();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (Services.USER_SERVICE.isUserLoggedIn()) {
            response.setContentType("application/json");

            String authorization = getCurrentUserAuthorization();

            if (authorization != null) {
                HttpURLConnection connection = (HttpURLConnection) new URL(END_POINT + "?key=" + API_KEY).openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", authorization);

                String json = getJSON(connection);

                response.getWriter().println(json);
            } else {
                response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
            }

        } else {
            response.setHeader("redirect", "/index.html");
        }
    }

    private String getCurrentUserAuthorization() {
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

    private String getJSON(HttpURLConnection connection) throws IOException, UnsupportedEncodingException {
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

}

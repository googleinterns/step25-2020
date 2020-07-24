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
import com.google.autograder.servlets.helpers.API;
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
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (Services.USER_SERVICE.isUserLoggedIn()) {
            response.setContentType("application/json");

            String authorization = API.getCurrentUserAPIAuthorization();

            if (authorization != null) {
                HttpURLConnection connection = (HttpURLConnection) new URL(END_POINT + "?key=" + API.API_KEY).openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("courseStates", "ACTIVE");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", authorization);

                String json = API.getJSON(connection);

                response.getWriter().println(json);
            } else {
                response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
            }

        } else {
            response.setHeader("redirect", "/index.html");
        }
    }

}

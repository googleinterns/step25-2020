package com.google.autograder.servlets;

import java.net.URL;
import java.util.Iterator;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.simple.JSONArray;
import java.net.HttpURLConnection;
import org.json.simple.JSONObject;
import javax.servlet.http.HttpServlet;
import org.json.simple.parser.JSONParser;
import java.nio.charset.StandardCharsets;
import javax.servlet.annotation.WebServlet;
import com.google.autograder.data.Database;
import java.io.UnsupportedEncodingException;
import org.json.simple.parser.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.autograder.data.UserHandler;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.autograder.servlets.helpers.API;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.autograder.servlets.auth.AccessTokenResponse;
import com.google.appengine.api.datastore.Query.FilterPredicate;

// @WebServlet("/listAssignments")
public final class ListAssignmentsServlet extends HttpServlet {

    private static String END_POINT = "https://classroom.googleapis.com/v1/courses/{courseId}/courseWork";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (UserHandler.isUserLoggedIn()) {
            response.setContentType("application/json");

            String authorization = API.getCurrentUserAPIAuthorization();

            if (authorization != null) {
                String courseID = request.getParameter("courseID");
                String endpoint = END_POINT.replace("{courseId}", courseID) + "?courseWorkStates=PUBLISHED&courseWorkStates=DRAFT&key=" + API.API_KEY;

                URL url = new URL(endpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", authorization);

                String json = API.getJSON(connection);

                Database.storeAssignmentsData(json, courseID);

                String assignmentsData = Database.getAssignmentsData(courseID);
                
                response.getWriter().println(assignmentsData);
            } else {
                response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
            }
        } else {
            response.setHeader("redirect", "/index.html");
        }
    }

}
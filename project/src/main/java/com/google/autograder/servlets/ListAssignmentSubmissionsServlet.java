package com.google.autograder.servlets;

import java.net.URL;
import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import com.google.autograder.data.Database;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.autograder.data.UserHandler;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.autograder.servlets.helpers.API;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

// @WebServlet("/listAssignmentSubmissions")
public final class ListAssignmentSubmissionsServlet extends HttpServlet {

    private static String END_POINT = "https://classroom.googleapis.com/v1/courses/{courseId}/courseWork/{courseWorkId}/studentSubmissions";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (UserHandler.isUserLoggedIn()) {

            response.setContentType("application/json");

            String authorization = API.getCurrentUserAPIAuthorization();

            if (authorization != null) {

                String requestURL = (String) request.getHeader("Referer");

                int courseIDIndex = requestURL.indexOf("&courseID=") + 10;
                int assignmentIDIndex = requestURL.indexOf("?assignmentID=") + 14;

                String courseID = requestURL.substring(courseIDIndex);
                String assignmentID = requestURL.substring(assignmentIDIndex, courseIDIndex - 10);

                String endpoint = END_POINT.replace("{courseId}", courseID);
                endpoint = endpoint.replace("{courseWorkId}", assignmentID);
                endpoint = endpoint += "?key=" + API.API_KEY;

                System.out.println("\n\nCourse ID:\t\t" + courseID + "\n\n");
                System.out.println("\n\nAssignment ID:\t\t" + assignmentID + "\n\n");
                System.out.println("\n\nRequest Endpoint:\t" + endpoint + "\n\n");

                URL url = new URL(endpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", authorization);

                String json = API.getJSON(connection);

                System.out.println("\n\n" + json + "\n\n");

                storeAssignmentSubmissionsData(json, courseID, assignmentID);
                // Database.storeAssignmentSubmissionsData(json, courseID, assignmentID);

                String assignmentSubmissionsData = getAssignmentSubmissionsData(courseID, assignmentID);
                // Database.getAssignmentSubmissionsData(courseID, assignmentID);
                
                response.getWriter().println(assignmentSubmissionsData);

            } else {
                response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
            }
        } else {
            response.setHeader("redirect", "/index.html");
        }
    }

    public static void storeAssignmentSubmissionsData(String submissionsJSON, String courseID, String assignmentID) {}

    public static String getAssignmentSubmissionsData(String courseID, String assignmentID) {
        return null;
    }

}
package com.google.autograder.servlets;

import java.net.URL;
import java.util.Iterator;
import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.http.HttpServlet;
import com.google.autograder.data.Database;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.autograder.data.UserHandler;
import com.google.autograder.servlets.helpers.API;

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
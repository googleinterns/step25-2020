package com.google.autograder.servlets;

import java.util.Optional;
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
        if (!UserHandler.isUserLoggedIn()) {
            response.setHeader("redirect", "/index.html");
        }

        String courseID = request.getParameter("courseID");
        String endpoint = END_POINT.replace("{courseId}", courseID) + "?courseWorkStates=PUBLISHED&courseWorkStates=DRAFT&key=" + API.API_KEY;

        Optional<HttpURLConnection> connection = API.getAuthenticatedRequest("GET", endpoint);

        if (!connection.isPresent()) {
            response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
            return;
        }

        String json = API.getJSON(connection.get());

        Database.storeAssignmentsData(json, courseID);

        String assignmentsData = Database.getAssignmentsData(courseID);
                
        response.setContentType("application/json");
        response.getWriter().println(assignmentsData);
    }

}
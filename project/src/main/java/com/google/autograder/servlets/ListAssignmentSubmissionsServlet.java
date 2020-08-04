package com.google.autograder.servlets;

import java.net.URL;
import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.http.HttpServlet;
import com.google.autograder.data.Database;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.autograder.data.UserHandler;
import com.google.autograder.servlets.helpers.API;

public final class ListAssignmentSubmissionsServlet extends HttpServlet {

    private static String CLASSROOM_END_POINT = "https://classroom.googleapis.com/v1/courses/{courseId}/courseWork/{courseWorkId}/studentSubmissions";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!UserHandler.isUserLoggedIn()) {
            response.setHeader("redirect", "/index.html");
        }

        String courseID = request.getParameter("courseID");
        String assignmentID = request.getParameter("assignmentID");
        String assignmentKey = request.getParameter("assignmentKey");
        String endpoint = CLASSROOM_END_POINT.replace("{courseId}", courseID).replace("{courseWorkId}", assignmentID).concat("?key=" + API.API_KEY);

        HttpURLConnection connection = API.getAuthenticatedRequest("GET", endpoint).orElse(null);
        
        if (connection == null) {
            // TODO: Send the redirect url to the OAuth handler to resume user flow after authentication.
            response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
        }
        
        String json = API.getJSON(connection);

        Database.storeAssignmentSubmissionsData(json, courseID, assignmentID, assignmentKey);

        String submissionsJSON = Database.getAssignmentSubmissionsData(courseID, assignmentID, assignmentKey);
                
        response.setContentType("application/json");
        response.getWriter().println(submissionsJSON);
    }

}
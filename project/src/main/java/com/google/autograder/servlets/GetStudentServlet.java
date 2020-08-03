package com.google.autograder.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.autograder.data.UserHandler;
import com.google.autograder.servlets.helpers.API;

public final class GetStudentServlet extends HttpServlet {

    private static String GET_STUDENT_END_POINT = "https://classroom.googleapis.com/v1/courses/{courseId}/students/{userId}";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!UserHandler.isUserLoggedIn()) {
            response.setHeader("redirect", "/index.html");
        }

        String courseID = request.getParameter("courseID");
        String studentID = request.getParameter("studentID");
        String endpoint = GET_STUDENT_END_POINT.replace("{courseId}", courseID).replace("{userId}", studentID).concat("?key=" + API.API_KEY);

        HttpURLConnection connection = API.getAuthenticatedRequest("GET", endpoint);

        if (connection == null) {
            response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
        }

        String json = API.getJSON(connection);

        response.setContentType("application/json");
        response.getWriter().println(json);
    }

}
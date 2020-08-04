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

public final class ListCoursesServlet extends HttpServlet {

    private static String END_POINT = "https://classroom.googleapis.com/v1/courses";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (UserHandler.isUserLoggedIn()) {
            response.setContentType("application/json");

            String authorization = API.getCurrentUserAPIAuthorization();

            if (authorization != null) {
                HttpURLConnection connection = (HttpURLConnection) new URL(END_POINT + "?key=" + API.API_KEY).openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("courseStates", "ACTIVE");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", authorization);

                String json = API.getJSON(connection);

                Database.storeCoursesData(json);

                String coursesData = Database.getCoursesData();

                response.getWriter().println(coursesData);
            } else {
                response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
            }

        } else {
            response.setHeader("redirect", "/index.html");
        }
    }

}
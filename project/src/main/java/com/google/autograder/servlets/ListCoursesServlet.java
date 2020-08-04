package com.google.autograder.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import org.json.simple.JSONObject;
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
        if (!UserHandler.isUserLoggedIn()) {
            response.setHeader("redirect", "/index.html");
            return;
        }

        HttpURLConnection connection = API.getAuthenticatedRequest("GET", END_POINT.concat("?key=" + API.API_KEY)).orElse(null);
        
        if (connection == null) {
            response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
            return;
        }

        connection.setRequestProperty("courseStates", "ACTIVE");
        
        String json = API.getJSON(connection);

        Database.storeCoursesData(json);

        String coursesData = Database.getCoursesData();

        response.setContentType("application/json");
        response.getWriter().println(coursesData);    
    }

}
package com.google.autograder.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.autograder.servlets.Services;

@WebServlet("/listCourses")
public final class ListCoursesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        if (Services.USER_SERVICE.isUserLoggedIn()) {
            response.setContentType("application/json");

            // Query query = new Query("Course");
            // PreparedQuery results = Services.DATA_STORE.prepare(query);

            System.out.println("\n\n" + "BANG BANG BANG ! ! !" + "\n\n");

        } else {
            response.sendRedirect("/index.html");
        }

    }

}
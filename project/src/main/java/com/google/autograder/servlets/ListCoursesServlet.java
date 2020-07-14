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

        if (Services.USER_SERVICE.isUserLoggedIn()) {

            

        } else {
            response.sendRedirect("/index.html");
        }

    }

}
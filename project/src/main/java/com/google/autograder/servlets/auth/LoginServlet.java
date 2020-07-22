package com.google.autograder.servlets.auth;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import com.google.autograder.data.Database;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public final class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String urlToRedirectToAfterUserLogsIn = "/index.html";
        String urlToRedirectToAfterUserLogsOut = "/index.html";
        String loginURL = Database.getUserService().createLoginURL(urlToRedirectToAfterUserLogsIn);
        String logoutURL = Database.getUserService().createLogoutURL(urlToRedirectToAfterUserLogsOut);

        if (Database.getUserService().isUserLoggedIn()) {
            String userEmail = Database.getCurrentUserEmail();
            response.setHeader("user-email", userEmail);
            response.setHeader("is-logged-in", "true");
        } else {
            response.setHeader("is-logged-in", "false");
        }

        response.setHeader("login-url", loginURL);
        response.setHeader("logout-url", logoutURL);
    }

}
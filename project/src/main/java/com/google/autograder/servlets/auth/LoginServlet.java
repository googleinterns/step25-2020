package com.google.autograder.servlets.auth;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.autograder.data.UserHandler;

public final class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String urlToRedirectToAfterUserLogsIn = "/index.html";
        String urlToRedirectToAfterUserLogsOut = "/index.html";
        String loginURL = UserHandler.createLoginURL(urlToRedirectToAfterUserLogsIn);
        String logoutURL = UserHandler.createLogoutURL(urlToRedirectToAfterUserLogsOut);

        if (UserHandler.isUserLoggedIn()) {
            String userEmail = UserHandler.getCurrentUserEmail();
            response.setHeader("user-email", userEmail);
            response.setHeader("is-logged-in", "true");
        } else {
            response.setHeader("is-logged-in", "false");
        }

        response.setHeader("login-url", loginURL);
        response.setHeader("logout-url", logoutURL);
    }

}
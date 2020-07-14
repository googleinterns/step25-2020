package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.autograder.servlets.Services;

@WebServlet("/login")
public final class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String urlToRedirectToAfterUserLogsIn = "/index.html";
        String urlToRedirectToAfterUserLogsOut = "/index.html";
        String loginURL = Services.USER_SERVICE.createLoginURL(urlToRedirectToAfterUserLogsIn);
        String logoutURL = Services.USER_SERVICE.createLogoutURL(urlToRedirectToAfterUserLogsOut);

        if (Services.USER_SERVICE.isUserLoggedIn()) {
            String userEmail = Services.USER_SERVICE.getCurrentUser().getEmail();
            response.setHeader("user-email", userEmail);
            response.setHeader("is-logged-in", "true");
        } else {
            response.setHeader("is-logged-in", "false");
        }

        response.setHeader("login-url", loginURL);
        response.setHeader("logout-url", logoutURL);
    }

}

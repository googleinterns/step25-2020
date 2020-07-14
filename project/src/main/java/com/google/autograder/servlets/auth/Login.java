package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@WebServlet("/login")
public final class Login extends HttpServlet {

    private static final UserService userService = UserServiceFactory.getUserService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String urlToRedirectToAfterUserLogsIn = "/index.html";
        String urlToRedirectToAfterUserLogsOut = "/index.html";
        String loginURL = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
        String logoutURL = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

        if (userService.isUserLoggedIn()) {
            String userEmail = userService.getCurrentUser().getEmail();
            response.setHeader("user-email", userEmail);
            response.setHeader("is-logged-in", "true");
        } else {
            response.setHeader("is-logged-in", "false");
        }

        response.setHeader("login-url", loginURL);
        response.setHeader("logout-url", logoutURL);
    }

}

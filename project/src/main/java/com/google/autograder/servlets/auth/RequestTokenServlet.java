package com.google.autograder.servlets.auth;

import java.net.URL;
import java.io.IOException;
import java.io.BufferedReader;
import java.net.URLConnection;
import java.lang.StringBuilder;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.MalformedURLException;
import javax.servlet.http.HttpServlet;
import java.nio.charset.StandardCharsets;
import javax.servlet.annotation.WebServlet;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.autograder.servlets.helpers.API;
import com.google.autograder.servlets.helpers.Client;

// @WebServlet("/requestAccessToken")
public final class RequestTokenServlet extends HttpServlet {

    private static String AUTH_CODE_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static String REDIRECT_URI = "/pages/auth/googleAuthenticator.html";
    private static String STATE = "auth_code_received";
    private static String ACCESS_TYPE = "offline";
    private static String RESPONSE_TYPE = "code";

    private static String SCOPE = "https://www.googleapis.com/auth/classroom.courses "
                                + "https://www.googleapis.com/auth/classroom.coursework.students "
                                + "https://www.googleapis.com/auth/drive";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder authEndpointBuffer = new StringBuilder(AUTH_CODE_ENDPOINT);
        String baseURL = (String) request.getParameter("baseURL");

        authEndpointBuffer.append("?client_id=" + API.urlEncode(Client.CLIENT_ID));

        authEndpointBuffer.append("&redirect_uri=" + API.urlEncode(baseURL + REDIRECT_URI));

        authEndpointBuffer.append("&response_type=" + API.urlEncode(RESPONSE_TYPE));

        authEndpointBuffer.append("&scope=" + API.urlEncode(SCOPE));

        authEndpointBuffer.append("&access_type=" + API.urlEncode(ACCESS_TYPE));

        authEndpointBuffer.append("&state=" + API.urlEncode(STATE));

        response.setHeader("next-page", authEndpointBuffer.toString());
    }

}

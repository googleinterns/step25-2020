package com.google.autograder.servlets.auth;

import java.net.URL;
import java.io.IOException;
import java.net.URLEncoder;
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

@WebServlet("/requestAccessToken")
public final class RequestTokenServlet extends HttpServlet {

    private static final String CLIENT_ID = "361755208772-l0oo78304ot5ded6rb0tgbhjhrqgmc53.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "jbzCa4-vkwu394TEk9PEnqNj";

    private static String HOST_URL = "https://8080-778d1d95-8447-4f8a-990b-b90da194d107.us-east1.cloudshell.dev";
    private static String REDIRECT_URI = "/pages/auth/googleAuthenticator.html";

    private static String AUTH_CODE_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static String STATE = "auth_code_received";
    private static String ACCESS_TYPE = "offline";
    private static String RESPONSE_TYPE = "code";

    private static String SCOPE = "https://www.googleapis.com/auth/classroom.courses.readonly";

    private static String CHAR_SET = StandardCharsets.UTF_8.name();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder authEndpointBuffer = new StringBuilder(AUTH_CODE_ENDPOINT);

        authEndpointBuffer.append("?client_id=" + URLEncoder.encode(CLIENT_ID, CHAR_SET));

        authEndpointBuffer.append("&redirect_uri=" + URLEncoder.encode((HOST_URL + REDIRECT_URI), CHAR_SET));

        authEndpointBuffer.append("&response_type=" + URLEncoder.encode(RESPONSE_TYPE, CHAR_SET));

        authEndpointBuffer.append("&scope=" + URLEncoder.encode(SCOPE, CHAR_SET));

        authEndpointBuffer.append("&access_type=" + URLEncoder.encode(ACCESS_TYPE, CHAR_SET));

        authEndpointBuffer.append("&state=" + URLEncoder.encode(STATE, CHAR_SET));

        response.setHeader("next-page", authEndpointBuffer.toString());
    }

}

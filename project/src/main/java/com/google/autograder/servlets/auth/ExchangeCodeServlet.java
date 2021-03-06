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
import com.google.autograder.data.Database;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.autograder.data.UserHandler;
import com.google.appengine.api.datastore.Entity;
import com.google.autograder.servlets.helpers.API;
import com.google.autograder.servlets.helpers.Client;

// @WebServlet("/exchangeAuthCode")
public final class ExchangeCodeServlet extends HttpServlet {
    
    private static String ACCESS_TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static String REDIRECT_URI = "/pages/auth/googleAuthenticator.html";
    private static String GRANT_TYPE = "authorization_code";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String requestURL = (String) request.getHeader("Referer");
        String baseURL = (String) request.getParameter("baseURL");

        String authCode = requestURL.substring (requestURL.indexOf("&code=") + 6, requestURL.indexOf("&scope="));

        if (authCode != null && authCode.length() > 0) {
            try {
                byte[] postBodyData = buildPostBody(authCode, baseURL).getBytes(StandardCharsets.UTF_8);
                HttpURLConnection connection = buildHttpURLConnection(postBodyData.length);
                connection.getOutputStream().write(postBodyData);
                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {        
                    String json = API.getJSON(connection);

                    AccessTokenResponse accessTokenResponse = AccessTokenResponse.getAccessTokenResponseObjectFromJSON(json);

                    String userEmail = UserHandler.getCurrentUserEmail();

                    Entity accessTokenResponseEntity = AccessTokenResponse.createDatastoreAccessTokenResponseEntity(accessTokenResponse, userEmail);
                    
                    Database.save(accessTokenResponseEntity);

                } else {
                    System.out.println("\n" + "INVALID RESPONSE CODE : " + responseCode);
                }

                connection.disconnect();
            } catch(Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        } else {
            System.out.println("\n" + "INVALID AUTH CODE : " + authCode);
        }

        response.setHeader("next-page", "/pages/courses.html");
    }

    private String buildPostBody(String authCode, String baseURL) throws UnsupportedEncodingException {
        StringBuilder postBody = new StringBuilder();
        
        postBody.append("code=" + authCode);
        postBody.append("&client_id=" + URLEncoder.encode(Client.CLIENT_ID, API.UTF_8));
        postBody.append("&client_secret=" + URLEncoder.encode(Client.CLIENT_SECRET, API.UTF_8));
        postBody.append("&redirect_uri=" + URLEncoder.encode(baseURL + REDIRECT_URI, API.UTF_8));
        postBody.append("&grant_type=" + URLEncoder.encode(GRANT_TYPE, API.UTF_8));

        return postBody.toString();
    }

    private HttpURLConnection buildHttpURLConnection(int postBodyDataLength) throws IOException, MalformedURLException, ProtocolException {
        HttpURLConnection connection = (HttpURLConnection) new URL(ACCESS_TOKEN_ENDPOINT).openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postBodyDataLength));

        return connection;
    }

}

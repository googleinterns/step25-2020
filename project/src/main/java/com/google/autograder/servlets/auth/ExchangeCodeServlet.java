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

// @WebServlet("/exchangeAuthCode")
public final class ExchangeCodeServlet extends HttpServlet {

    private static final String CLIENT_ID = "361755208772-l0oo78304ot5ded6rb0tgbhjhrqgmc53.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "jbzCa4-vkwu394TEk9PEnqNj";
    
    private static String BASE_URL = "https://8080-778d1d95-8447-4f8a-990b-b90da194d107.us-east1.cloudshell.dev"; // "https://step25-2020.uc.r.appspot.com"; 
    private static String ACCESS_TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static String REDIRECT_URI = "/pages/auth/googleAuthenticator.html";
    private static String GRANT_TYPE = "authorization_code";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String requestURL = (String) request.getHeader("Referer");
        String authCode = requestURL.substring (requestURL.indexOf("&code=") + 6, requestURL.indexOf("&scope="));

        if (authCode != null && authCode.length() > 0) {
            try {
                byte[] postBodyData = buildPostBody(authCode).getBytes(StandardCharsets.UTF_8);
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

    private String buildPostBody(String authCode) throws UnsupportedEncodingException {
        StringBuilder postBody = new StringBuilder();
        
        postBody.append("code=" + authCode);
        postBody.append("&client_id=" + URLEncoder.encode(CLIENT_ID, API.UTF_8));
        postBody.append("&client_secret=" + URLEncoder.encode(CLIENT_SECRET, API.UTF_8));
        postBody.append("&redirect_uri=" + URLEncoder.encode(BASE_URL + REDIRECT_URI, API.UTF_8));
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

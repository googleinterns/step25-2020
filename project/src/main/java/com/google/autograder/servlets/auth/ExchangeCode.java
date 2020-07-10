package com.google.autograder.servlets.auth;

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.io.OutputStream;
import java.util.Collections;
import java.io.BufferedReader;
import java.net.URLConnection;
import java.lang.StringBuilder;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.MalformedURLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import java.nio.charset.StandardCharsets;
import javax.servlet.annotation.WebServlet;
import java.io.UnsupportedEncodingException;
import com.google.api.client.http.GenericUrl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserService;
import com.google.api.client.json.gson.GsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.http.BasicAuthentication;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.autograder.servlets.auth.AccessTokenResponse;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeCallbackServlet;

@WebServlet("/exchangeAuthCode")
public final class ExchangeCode extends HttpServlet {

    private static final String CLIENT_ID = "361755208772-l0oo78304ot5ded6rb0tgbhjhrqgmc53.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "jbzCa4-vkwu394TEk9PEnqNj";

    private static String HOST_URL = "https://8080-778d1d95-8447-4f8a-990b-b90da194d107.us-east1.cloudshell.dev";
    private static String ACCESS_TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static String REDIRECT_URI = "/pages/auth/googleAuthenticator.html";
    private static String GRANT_TYPE = "authorization_code";

    private static String UTF_8 = StandardCharsets.UTF_8.name();

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
                    String json = getJSON(connection);

                    AccessTokenResponse accessTokenResponse = AccessTokenResponse.getAccessTokenResponseObject(json);

                    System.out.println("\n" + json);

                    System.out.println("\n" + accessTokenResponse.toString());

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

        response.setHeader("next-page", (HOST_URL + "/pages/courses.html"));
    }

    private String buildPostBody(String authCode) throws UnsupportedEncodingException {
        StringBuilder postBody = new StringBuilder();
        postBody.append("code=" + authCode);
        postBody.append("&client_id=" + URLEncoder.encode(CLIENT_ID, UTF_8));
        postBody.append("&client_secret=" + URLEncoder.encode(CLIENT_SECRET, UTF_8));
        postBody.append("&redirect_uri=" + URLEncoder.encode((HOST_URL + REDIRECT_URI), UTF_8));
        postBody.append("&grant_type=" + URLEncoder.encode(GRANT_TYPE, UTF_8));

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
    
    private String getJSON(HttpURLConnection connection) throws IOException, UnsupportedEncodingException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), UTF_8));
        StringBuilder jsonBuffer = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            jsonBuffer.append(line);
            jsonBuffer.append(System.lineSeparator());
        }

        bufferedReader.close();

        return jsonBuffer.toString();
    }

}

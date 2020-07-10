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
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import java.nio.charset.StandardCharsets;
import javax.servlet.annotation.WebServlet;
import com.google.api.client.http.GenericUrl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.api.client.json.JsonFactory;
import com.google.autograder.servlets.auth.API;
import com.google.api.client.http.HttpTransport;
import com.google.autograder.servlets.auth.Utils;
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

    private static String HOST_URL = "http://localhost:8080";
    private static String REDIRECT_URI = "/pages/auth/googleAuthenticator.html";

    private static String ACCESS_TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static String GRANT_TYPE = "authorization_code";

    private static String CHAR_SET = StandardCharsets.UTF_8.name();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // get the code

        String requestURL = (String) request.getHeader("Referer");

        String tempAuthCode = requestURL.substring (requestURL.indexOf("&code=") + 6, requestURL.indexOf("&scope="));

        boolean receivedValidAuthCode = API.setAPIAuthCode(tempAuthCode);

        if (receivedValidAuthCode) {
            System.out.println("\n" + "START");

            System.out.println("\n" + "REQUEST ORIGIN : " + requestURL);

            System.out.println("\n" + "AUTH CODE : " + API.getAPIAuthCode());

            StringBuilder postBodyBuffer = new StringBuilder();
            postBodyBuffer.append("code=" + API.getAPIAuthCode());
            postBodyBuffer.append("&client_id=" + URLEncoder.encode(CLIENT_ID, CHAR_SET));
            postBodyBuffer.append("&client_secret=" + URLEncoder.encode(CLIENT_SECRET, CHAR_SET));
            postBodyBuffer.append("&redirect_uri=" + URLEncoder.encode((HOST_URL + REDIRECT_URI), CHAR_SET));
            postBodyBuffer.append("&grant_type=" + URLEncoder.encode(GRANT_TYPE, CHAR_SET));

            String postBody = postBodyBuffer.toString();

            byte[] postBodyData = postBody.getBytes(StandardCharsets.UTF_8);
            int postBodyDataLength = postBodyData.length;

            HttpURLConnection connection = (HttpURLConnection) new URL(ACCESS_TOKEN_ENDPOINT).openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(postBodyDataLength));

            connection.getOutputStream().write(postBodyData);

            System.out.println("\n" + "ENDPOINT : " + ACCESS_TOKEN_ENDPOINT);

            System.out.println("\n" + "BODY : " + postBody);

            try {

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {

                    // Initiate the post request
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHAR_SET));
                    StringBuilder jsonBuffer = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        jsonBuffer.append(line);
                        jsonBuffer.append(System.lineSeparator());
                    }

                    bufferedReader.close();

                    String json = jsonBuffer.toString();

                    System.out.println("\n" + "JSON: " + json);
                    System.out.println("\n" + "REQUEST SUCCESS");
            
                } else {
                    System.out.println("\n" + "FAILED OUT : INVALID RESPONSE CODE : " + responseCode);
                } 

                connection.disconnect();

            } catch(Exception e) {

                System.out.println(e);
                e.printStackTrace();

            }

        } else {
            
            System.out.println("\n" + "FAILED OUT : INVALID AUTH CODE");

        }

        response.setHeader("next-page", (HOST_URL + "/pages/courses.html"));

        System.out.println("\n" + "STOP" + "\n");

        //     HashMap params = new HashMap<String, String>();
        //     params.put("code", API.getAPIAuthCode());
        //     params.put("client_id", CLIENT_ID);
        //     params.put("client_secret", CLIENT_SECRET);
        //     params.put("redirect_uri", (BASE_URL + REDIRECT_URI));
        //     params.put("grant_type", GRANT_TYPE);
        //
        //     ObjectMapper objectMapper = new ObjectMapper();
        //     String requestBody = objectMapper.writeValueAsString(values);
        //
        //     HttpClient client = HttpClient.newHttpClient();
        //     HttpRequest request = HttpRequest.newBuilder()
        //         .uri(URI.create(TOKEN_ENDPOINT))
        //         .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        //         .build();
        //
        //     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //
        //     System.out.println(response.body());

    }

}

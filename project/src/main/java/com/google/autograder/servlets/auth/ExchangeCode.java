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

@WebServlet("/exchangeCode")
public final class ExchangeCode extends HttpServlet {

    private static final String CLIENT_ID = "361755208772-l0oo78304ot5ded6rb0tgbhjhrqgmc53.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "jbzCa4-vkwu394TEk9PEnqNj";

    private static String BASE_URL = "https://8080-778d1d95-8447-4f8a-990b-b90da194d107.us-east1.cloudshell.dev";
    private static String SCOPE = "https://www.googleapis.com/auth/classroom.courses.readonly";
    private static String TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static String REDIRECT_URI = "/pages/courses.html";
    private static String GRANT_TYPE = "authorization_code";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // get the code

        String requestURL = (String) request.getHeader("Referer");

        String authCode = requestURL.substring (requestURL.indexOf("?code=") + 6, requestURL.indexOf("&"));

        boolean receivedValidAuthCode = API.setAPIAuthCode(authCode);

        System.out.println("\n" + "START");

        System.out.println("\n" + "AUTH CODE : " + API.getAPIAuthCode());

        if (receivedValidAuthCode) {

            String charset = StandardCharsets.UTF_8.name();

            // String postBody = String.format("code=" + API.getAPIAuthCode() + "&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&redirect_uri=%s&grant_type=%s",
            //     // URLEncoder.encode(CLIENT_ID, charset), 
            //     // URLEncoder.encode(CLIENT_SECRET, charset),
            //     URLEncoder.encode((BASE_URL + REDIRECT_URI), charset),
            //     URLEncoder.encode(GRANT_TYPE, charset)
            // );

            // StringBuilder postBodyBuffer = new StringBuilder();
            // postBodyBuffer.append("code=" + API.getAPIAuthCode());
            // postBodyBuffer.append("&client_id=" + CLIENT_ID);
            // postBodyBuffer.append("&client_secret=" + CLIENT_SECRET);
            // postBodyBuffer.append("&redirect_uri=" + (BASE_URL + REDIRECT_URI));
            // postBodyBuffer.append("&grant_type=" + GRANT_TYPE);

            // String postBody = postBodyBuffer.toString();

            HashMap parameters = new HashMap<String, String>();
            parameters.put("code", API.getAPIAuthCode());
            parameters.put("client_id", CLIENT_ID);
            parameters.put("client_secret", CLIENT_SECRET);
            parameters.put("redirect_uri", BASE_URL + REDIRECT_URI);
            parameters.put("grant_type", GRANT_TYPE);
        
            ObjectMapper objectMapper = new ObjectMapper();
            String postBody = objectMapper.writeValueAsString(parameters);

            byte[] postBodyData = postBody.getBytes(StandardCharsets.UTF_8);
            int postBodyLength = postBodyData.length;

            HttpURLConnection connection = (HttpURLConnection) new URL(TOKEN_ENDPOINT).openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("charset", charset);
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(postBodyLength));
            connection.setRequestProperty("Content-Type", "application/json");

            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            output.write(postBodyData);
            output.close();

            System.out.println("\n" + "ENDPOINT : " + TOKEN_ENDPOINT);
            System.out.println("\n" + "BODY : " + postBody);

            int responseCode = connection.getResponseCode();

            // Initiate the post request
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));

            if (responseCode == 200) {

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

        } else {
            
            System.out.println("\n" + "FAILED OUT : INVALID AUTH CODE");

        }

        System.out.println("\n" + "STOP" + "\n");

        response.setHeader("next-page", (BASE_URL + REDIRECT_URI));

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

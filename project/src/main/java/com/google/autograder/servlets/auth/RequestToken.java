package com.google.autograder.servlets.auth;

import java.net.URL;
import java.io.IOException;
import java.util.Collections;
import java.lang.StringBuilder;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import com.google.api.client.http.GenericUrl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.autograder.servlets.auth.Utils;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserService;
import com.google.api.client.json.gson.GsonFactory;
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

import com.google.autograder.servlets.auth.API;

@WebServlet("/requestToken")
public final class RequestToken extends HttpServlet {

    private static final String CLIENT_ID = "361755208772-l0oo78304ot5ded6rb0tgbhjhrqgmc53.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "jbzCa4-vkwu394TEk9PEnqNj";

    private static String BASE_URL = "https://8080-778d1d95-8447-4f8a-990b-b90da194d107.us-east1.cloudshell.dev";
    private static String SCOPE = "https://www.googleapis.com/auth/classroom.courses.readonly";
    private static String AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static String REDIRECT_URI = "/pages/auth/exchangeCode.html";
    private static String ACCESS_TYPE = "offline";
    private static String RESPONSE_TYPE = "code";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder authEndpointBuffer = new StringBuilder(AUTH_ENDPOINT);

        authEndpointBuffer.append("?client_id=" + CLIENT_ID);

        authEndpointBuffer.append("&redirect_uri=" + (BASE_URL + REDIRECT_URI));

        authEndpointBuffer.append("&response_type=" + RESPONSE_TYPE);

        authEndpointBuffer.append("&scope=" + SCOPE);

        authEndpointBuffer.append("&access_type=" + ACCESS_TYPE);

        response.setHeader("next-page", authEndpointBuffer.toString());
    }

}

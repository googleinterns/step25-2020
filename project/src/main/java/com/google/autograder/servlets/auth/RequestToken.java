package com.google.autograder.servlets.auth;

import java.io.IOException;
import java.util.Collections;
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

@WebServlet("/googleAuthorizationRequestToken")
public final class RequestToken extends AbstractAppEngineAuthorizationCodeServlet {

    private static final String CLIENT_ID = "361755208772-l0oo78304ot5ded6rb0tgbhjhrqgmc53.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "jbzCa4-vkwu394TEk9PEnqNj";

    private static String BASE_URL = "https://www.8080-778d1d95-8447-4f8a-990b-b90da194d107.us-east1.cloudshell.dev";
    private static String SCOPE = "https://www.googleapis.com/auth/classroom.courses.readonly";
    private static String REDIRECT_URI = "/pages/courses.html";

    private static AppEngineDataStoreFactory DATA_STORE_FACTORY = AppEngineDataStoreFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT = UrlFetchTransport.getDefaultInstance();
    private static UserService USER_SERVCE = UserServiceFactory.getUserService();
    private static JsonFactory JSON_FACTORY = new GsonFactory();
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("\n\n\n\n" + "FOUR" + "\n\n\n\n");
    }
    
    @Override
    public String getRedirectUri(HttpServletRequest request) throws ServletException, IOException {
        System.out.println("\n\n\n\n\n" + "THREE" + "\n\n\n\n\n");

        GenericUrl url = new GenericUrl(request.getRequestURL().toString());
        url.setRawPath(REDIRECT_URI);
        return url.build();
    }
    
    @Override
    public AuthorizationCodeFlow initializeFlow() throws IOException {
        System.out.println("\n\n\n\n\n" + "TWO" + "\n\n\n\n\n");

        return new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT,
            JSON_FACTORY,
            CLIENT_ID,
            CLIENT_SECRET,
            Collections.singleton(ClassroomScopes.CLASSROOM_COURSES_READONLY)
        ).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
    }

    @Override
    public String getUserId(HttpServletRequest request) throws ServletException, IOException {
        System.out.println("\n\n\n\n\n" + "ONE" + "\n\n\n\n\n");

        if (Utils.getUserService().isUserLoggedIn()) {
            System.out.println("\n\n\n\n\n" + "ID: " + Utils.getUserService().getCurrentUser().getUserId() + "\n\n\n\n\n");
            return Utils.getUserService().getCurrentUser().getUserId();
        }

        return null;
    }
}

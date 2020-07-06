package com.google.autograder.servlets.auth;

import java.io.IOException;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.appengine.api.users.UserService;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;

public final class Utils {
    private static AppEngineDataStoreFactory DATA_STORE_FACTORY = AppEngineDataStoreFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT = UrlFetchTransport.getDefaultInstance();
    private static UserService USER_SERVCE = UserServiceFactory.getUserService();
    private static JsonFactory JSON_FACTORY = new GsonFactory();

    static String getRedirectUri(HttpServletRequest request, String baseURL, String redirectURI) {
        GenericUrl url = new GenericUrl(baseURL);
        url.setRawPath(redirectURI);
        return url.build();
    }
    
    static GoogleAuthorizationCodeFlow newFlow(String clientID, String clientSecret) throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT,
            JSON_FACTORY,
            clientID,
            clientSecret,
            Collections.singleton(
                ClassroomScopes.CLASSROOM_COURSES_READONLY
            )
        ).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
    }

    static UserService getUserService() {
        return USER_SERVCE;
    }
}

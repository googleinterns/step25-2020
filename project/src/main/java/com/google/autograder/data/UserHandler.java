package com.google.autograder.data;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

// Wrapper class around UserService functionality

public final class UserHandler {

    private static final UserService USER_SERVICE = UserServiceFactory.getUserService();

    public static String getCurrentUserEmail() {
        return USER_SERVICE.getCurrentUser().getEmail();
    }

    public static String getCurrentUserID() {
        return USER_SERVICE.getCurrentUser().getUserId();
    }

    public static boolean isUserLoggedIn() {
        return USER_SERVICE.isUserLoggedIn();
    }

    public static String createLoginURL(String urlToRedirectToAfterUserLogsIn) {
        return USER_SERVICE.createLoginURL(urlToRedirectToAfterUserLogsIn);
    }

    public static String createLogoutURL(String urlToRedirectToAfterUserLogsOut) {
        return USER_SERVICE.createLogoutURL(urlToRedirectToAfterUserLogsOut);
    }
    
}
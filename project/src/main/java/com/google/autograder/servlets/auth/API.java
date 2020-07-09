package com.google.autograder.servlets.auth;

public final class API {

    private static String API_AUTH_CODE = null;
    private static String API_ACCESS_TOKEN = null;

    // refreash token

    public static boolean setAPIAuthCode(String apiAuthCode) {
        if (apiAuthCode != null && apiAuthCode.length() > 0) {
            API_AUTH_CODE = apiAuthCode;
            return true;
        }

        return false;
    }

    public static String getAPIAuthCode() {
        if (API_AUTH_CODE != null) {
            return API_AUTH_CODE;
        }

        return null;
    }
    
    public static boolean setAPIAccessToken(String apiAccessToken) {
        if (apiAccessToken != null && apiAccessToken.length() > 0) {
            API_ACCESS_TOKEN = apiAccessToken;
            return true;
        }

        return false;
    }

    public static String getAPIAccessToken() {
        if (API_ACCESS_TOKEN != null) {
            return API_ACCESS_TOKEN;
        }

        return null;
    }
}

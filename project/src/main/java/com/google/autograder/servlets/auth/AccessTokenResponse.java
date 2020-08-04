package com.google.autograder.servlets.auth;

import com.google.gson.Gson;
import com.google.appengine.api.datastore.Entity;

public final class AccessTokenResponse {

    private String scope;
    private long expires_in;
    private String token_type;
    private String access_token;
    
    public AccessTokenResponse(String access_token, String token_type, String scope, long expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.scope = scope;
    }

    public String getAccess_Token() {
        return access_token;
    }

    public String getToken_Type() {
        return token_type;
    }

    public long getExpires_In() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setAccess_Token(String access_token) {
        this.access_token = access_token;
    }

    public void setToken_Type(String token_type) {
        this.token_type = token_type;
    }

    public void setExpires_In(long expires_in) {
        this.expires_in = expires_in;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public static Entity createDatastoreAccessTokenResponseEntity(AccessTokenResponse accessTokenResponse, String userEmail) {
        Entity accessTokenResponseEntity = new Entity("AccessTokenResponse");
        accessTokenResponseEntity.setProperty("access_token", accessTokenResponse.access_token);
        accessTokenResponseEntity.setProperty("token_type", accessTokenResponse.token_type);
        accessTokenResponseEntity.setProperty("expires_in", accessTokenResponse.expires_in);
        accessTokenResponseEntity.setProperty("scope", accessTokenResponse.scope);
        accessTokenResponseEntity.setProperty("user_email", userEmail);
        return accessTokenResponseEntity;
    }
    
    public static AccessTokenResponse buildAccessTokenResponseFromDatastoreEntity(Entity entity) {
        if (entity.getProperty("access_token") == null) {
            return null;
        }
        
        String scope = (String) entity.getProperty("scope");
        long expires_in = (long) entity.getProperty("expires_in");
        String token_type = (String) entity.getProperty("token_type");
        String access_token = (String) entity.getProperty("access_token");
        return new AccessTokenResponse(access_token, token_type, scope, expires_in);
    }

    public static AccessTokenResponse getAccessTokenResponseObjectFromJSON(String json) {
        return new Gson().fromJson(json, AccessTokenResponse.class);
    } 

    @Override
    public String toString() {
        return "AccessTokenResponse [access_token=" + access_token + ", expires_in=" + expires_in + ", scope=" + scope + ", token_type=" + token_type + "]";
    }

}
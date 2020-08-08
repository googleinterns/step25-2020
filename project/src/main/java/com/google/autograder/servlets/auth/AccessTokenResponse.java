package com.google.autograder.servlets.auth;

import java.time.Instant;
import com.google.gson.Gson;
import java.time.temporal.ChronoUnit;
import com.google.appengine.api.datastore.Entity;

public final class AccessTokenResponse {
    private String access_token;
    private String token_type;
    private long expires_in;
    private String scope;

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

        Instant expires = Instant.now().plus(accessTokenResponse.expires_in, ChronoUnit.SECONDS);
        accessTokenResponseEntity.setProperty("expires", expires.toString());
        
        return accessTokenResponseEntity;
    }
    
    public static AccessTokenResponse buildAccessTokenResponseFromDatastoreEntity(Entity entity) {
        if (entity.getProperty("access_token") == null) {
            return null;
        }

        Instant expires = Instant.parse((String) entity.getProperty("expires"));

        if (Instant.now().isAfter(expires)) {
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

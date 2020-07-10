package com.google.autograder.servlets.auth;

import com.google.gson.Gson;

public final class AccessTokenResponse {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String scope;

    public AccessTokenResponse(String access_token, String token_type, String scope, int expires_in) {
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

    public int getExpires_In() {
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

    public void setExpires_In(int expires_in) {
        this.expires_in = expires_in;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public static AccessTokenResponse getAccessTokenResponseObject(String json) {
        return new Gson().fromJson(json, AccessTokenResponse.class);
    } 

    @Override
    public String toString() {
        return "AccessTokenResponse [access_token=" + access_token + ", expires_in=" + expires_in + ", scope=" + scope + ", token_type=" + token_type + "]";
    }
}

package com.hetero.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("user")
    private User user;

    @JsonProperty("message")
    private String message;


    public AuthenticationResponse (String accessToken, String refreshToken, User user, String message) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
        this.message = message;
    }

    public String getAccessToken () {
        return accessToken;
    }

    public void setAccessToken (String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken () {
        return refreshToken;
    }

    public void setRefreshToken (String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getUser () {
        return user;
    }

    public void setUser (User user) {
        this.user = user;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }
}

package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetNotificationResponseBean {

    @JsonProperty("usr_token")
    private String usr_token;

    @JsonProperty("usr_tokenexpirydate")
    private String usr_tokenexpirydate;

    @JsonProperty("response_code")
    private String responseCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("Username")
    private String username;

    public String getUserToken() {
        return usr_token;
    }

    public void setUserToken(String usr_token) {
        this.usr_token = usr_token;
    }
    public String getUserTokenExpiry() {
        return usr_tokenexpirydate;
    }

    public void setUserTokenExpiry(String usr_tokenexpirydate) {
        this.usr_tokenexpirydate = usr_tokenexpirydate;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 08122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponseBean {
    @JsonProperty("response_code")
    private String Response_code;

    @JsonProperty("message")
    private String Message;

    public String getResponse_code() {
        return Response_code;
    }

    public void setResponse_code(String response_code) {
        Response_code = response_code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}

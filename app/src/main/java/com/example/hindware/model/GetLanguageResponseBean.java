package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by SandeepY on 09122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetLanguageResponseBean {

    @JsonProperty("LanguageResponseList")
    private ArrayList<LanguageResponseList> languageResponseList;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("Message")
    private String message;

    public ArrayList<LanguageResponseList> getLanguageResponseList() {
        return languageResponseList;
    }

    public void setLanguageResponseList(ArrayList<LanguageResponseList> languageResponseList) {
        this.languageResponseList = languageResponseList;
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
}

package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 09122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class LanguageResponseList {

    @JsonProperty("LNG_CODE")
    private String lNG_CODE;

    @JsonProperty("LNG_ENGLISHNAME")
    private String lNG_ENGLISHNAME;

    @JsonProperty("LNG_NAME")
    private String lNG_NAME;

    public String getlNG_CODE() {
        return lNG_CODE;
    }

    public void setlNG_CODE(String lNG_CODE) {
        this.lNG_CODE = lNG_CODE;
    }

    public String getlNG_ENGLISHNAME() {
        return lNG_ENGLISHNAME;
    }

    public void setlNG_ENGLISHNAME(String lNG_ENGLISHNAME) {
        this.lNG_ENGLISHNAME = lNG_ENGLISHNAME;
    }

    public String getlNG_NAME() {
        return lNG_NAME;
    }

    public void setlNG_NAME(String lNG_NAME) {
        this.lNG_NAME = lNG_NAME;
    }
}

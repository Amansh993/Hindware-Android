package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 08122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseRequestBean {
    @JsonProperty("usr_data")
    private String Usr_data;

    public String getUsr_data() {
        return Usr_data;
    }

    public void setUsr_data(String usr_data) {
        Usr_data = usr_data;
    }
}

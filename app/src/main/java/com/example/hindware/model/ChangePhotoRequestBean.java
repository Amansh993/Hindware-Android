package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 10032021
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangePhotoRequestBean {

    @JsonProperty("athdata")
    private String Athdata;

    @JsonProperty("photodata")
    private String Photodata;

    public String getAthdata() {
        return Athdata;
    }

    public void setAthdata(String athdata) {
        Athdata = athdata;
    }

    public String getPhotodata() {
        return Photodata;
    }

    public void setPhotodata(String photodata) {
        Photodata = photodata;
    }
}

package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 08122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckAppVersionResponseBean extends BaseResponseBean {

    @JsonProperty("data")
    private String myData;

    public String getMyData() {
        return myData;
    }

    public void setMyData(String myData) {
        this.myData = myData;
    }
}

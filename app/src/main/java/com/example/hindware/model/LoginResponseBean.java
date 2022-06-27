package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 08122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponseBean extends BaseResponseBean {

    @JsonProperty("usr_token")
    private String Usr_token;

    @JsonProperty("usr_tokenexpirydate")
    private String Usr_tokenexpirydate;

    public String getUsr_token() {
        return Usr_token;
    }

    public void setUsr_token(String usr_token) {
        Usr_token = usr_token;
    }

    public String getUsr_tokenexpirydate() {
        return Usr_tokenexpirydate;
    }

    public void setUsr_tokenexpirydate(String usr_tokenexpirydate) {
        Usr_tokenexpirydate = usr_tokenexpirydate;
    }
}

package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 11122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class LogoutRequestBean {

    @JsonProperty("_tk")
    private String _tk;

    @JsonProperty("_ts")
    private String _ts;

    public String get_tk() {
        return _tk;
    }

    public void set_tk(String _tk) {
        this._tk = _tk;
    }

    public String get_ts() {
        return _ts;
    }

    public void set_ts(String _ts) {
        this._ts = _ts;
    }
}

package com.example.hindware.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SandeepY on 09122020
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendOTPRequestBean {

    @JsonProperty("_m")
    private String _M;

    @JsonProperty("_c")
    private String _C;

    public String get_M() {
        return _M;
    }

    public void set_M(String _M) {
        this._M = _M;
    }

    public String get_C() {
        return _C;
    }

    public void set_C(String _C) {
        this._C = _C;
    }
}
